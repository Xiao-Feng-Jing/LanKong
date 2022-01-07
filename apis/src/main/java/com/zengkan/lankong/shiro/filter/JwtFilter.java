package com.zengkan.lankong.shiro.filter;

import com.zengkan.lankong.shiro.token.JwtToken;
import com.zengkan.lankong.utils.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/08/31/21:38
 * @Description:
 **/
@Slf4j
public class JwtFilter extends AuthenticatingFilter {


    // 是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 判断请求的请求头是否带上 "Token"

        if (isLoginAttempt(request)){
            // 如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                responseError(response);
                return false;
            }
        }
        // 如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    private boolean isLoginAttempt(ServletRequest request) {
        return getAuthzHeader(request) != null;
    }

    /**
     * 检测是否需要认证
     * */
    private String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return httpRequest.getHeader("Authorization");
    }

    /**
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws IllegalStateException{
        // 交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        AuthenticationToken token = createToken(request, response);

        if (token == null) {
            throw new IllegalStateException("token过期或者为空");
        }

        Subject subject = getSubject(request, response);
        subject.login(token);
        return onLoginSuccess(token, subject, request, response);
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if(StringUtils.isNotBlank(jwtToken)&&JwtUtil.isTokenExpired(jwtToken)) {
            return new JwtToken(jwtToken);
        }
        return null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse){
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String jwt = ((JwtToken) token).getToken();
        if(StringUtils.isNotBlank(jwt)) {
            httpResponse.setHeader("Authorization", jwt);
        }
        return true;
    }

    /**
     * 将非法请求跳转到 /noAuth/{}
     * */
    private void responseError(ServletResponse response) {
        log.error("无权限访问");
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 设置编码，否则中文字符在重定向时会变为空字符串
            httpServletResponse.sendRedirect("/noAuth");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}