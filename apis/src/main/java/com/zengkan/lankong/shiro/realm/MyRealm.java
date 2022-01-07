package com.zengkan.lankong.shiro.realm;

import com.zengkan.lankong.pojo.Role;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserAuthenticationService;
import com.zengkan.lankong.shiro.token.JwtToken;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/1:30
 * @Description:
 **/
@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {


    private final UserAuthenticationService userAuthenticationService;
    private final RedisUtil redisUtil;

    @Autowired
    public MyRealm(UserAuthenticationService userAuthenticationService, RedisUtil redisUtil) {
        this.userAuthenticationService = userAuthenticationService;
        this.redisUtil = redisUtil;
    }

    //根据token判断此Authenticator是否使用该realm
    //必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如@RequiresRoles,@RequiresPermissions之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token=principals.toString();
        User user = (User) redisUtil.getString(token);
        if (user == null) {
            redisUtil.hDelete("user",JwtUtil.checkToken(token).get("id"));
            throw new IllegalArgumentException("token 过期");
        }
        List<Role> userRoles = userAuthenticationService.getUserRole(user);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //查询数据库来获取用户的角色
        info.addRoles(userRoles.stream().map(Role::getRole).collect(Collectors.toList()));
        return info;
    }


    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws IllegalArgumentException{
        String jwt= ((JwtToken) token).getToken();
        User user = (User) redisUtil.getString(jwt);
        if (user != null) {
            boolean shouldRefresh = shouldTokenRefresh(jwt);
            if(shouldRefresh) {
                userAuthenticationService.saveToken(jwt, user);
            }
            return new SimpleAuthenticationInfo(jwt,jwt,"MyRealm");
        }else {
            redisUtil.hDelete("user",JwtUtil.checkToken(jwt).get("id"));
            throw new IllegalStateException("token 过期或不存在");
        }
    }

    protected boolean shouldTokenRefresh(String token){
        long time = redisUtil.getExpire(token);
        return time > 0 && time <= 300;
    }
}
