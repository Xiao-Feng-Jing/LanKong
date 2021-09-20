package com.zengkan.lankong.shiro.filter;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.shiro.token.JwtToken;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.MapUtils;
import com.zengkan.lankong.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/08/31/21:38
 * @Description:
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private final UserService userService;
    private final RedisUtil redisUtil;

    public JwtFilter(UserService userService, RedisUtil redisUtil) {
        this.userService = userService;
        this.redisUtil = redisUtil;
    }


    //是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)){
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
            }catch (Exception e){
                //token 错误
                responseError(response,e.getMessage());
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String token=getAuthzHeader(request);
        return token!=null && !"null".equals(token);
    }

    /**
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "token 为空";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if(StringUtils.isNotBlank(jwtToken)&&!JwtUtil.isTokenExpired(jwtToken)) {
            return new JwtToken(jwtToken);
        }
        return null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JwtToken){
            JwtToken jwtToken = (JwtToken) token;
            User user = (User) redisUtil.getString(jwtToken.getToken());
            if (user != null) {
                if (!token.equals(redisUtil.hGet("userToken",user.getId()))) {
                    throw new AuthenticationException("token 是非法的");
                }
                boolean shouldRefresh = shouldTokenRefresh(jwtToken.getToken());
                if(shouldRefresh) {
                    newToken = JwtUtil.createToken(MapUtils.getObjectToMap(user));
                    userService.saveToken(newToken,user);
                }

            }else {
                throw new AuthenticationException("token 过期");
            }

        }
        if(StringUtils.isNotBlank(newToken)) {
            httpResponse.setHeader("Authorization", newToken);
        }
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected boolean shouldTokenRefresh(String token){
        long time = redisUtil.getExpire(token);
        return time > 0 && time <= 300;
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }

    /**
     * 将非法请求跳转到 /noAuth/{}
     * */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            log.error(message);
            httpServletResponse.sendRedirect("/noAuth/" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
