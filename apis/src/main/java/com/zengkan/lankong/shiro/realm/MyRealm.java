package com.zengkan.lankong.shiro.realm;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.shiro.token.JwtToken;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.MapUtils;
import com.zengkan.lankong.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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


    private final UserService userService;
    private final RedisUtil redisUtil;

    @Autowired
    public MyRealm(UserService userService, RedisUtil redisUtil) {
        this.userService = userService;
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
        UserRole userRole = userService.getUserRole(user);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //查询数据库来获取用户的角色
        info.addRole(userRole.getRole());
        return info;
    }


    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        String jwt= ((JwtToken) token).getToken();
        User user = (User) redisUtil.getString(jwt);
        if (user != null) {
            if (!token.equals(redisUtil.hGet("userToken",user.getId()))) {
                throw new AuthenticationException("token 是非法的");
            }
            return new SimpleAuthenticationInfo(jwt,jwt,"MyRealm");
        }else {
            throw new AuthenticationException("token 过期");
        }
    }
}
