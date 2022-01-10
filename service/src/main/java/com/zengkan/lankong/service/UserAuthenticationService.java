package com.zengkan.lankong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zengkan.lankong.pojo.Role;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.vo.RegisteredVO;
import com.zengkan.lankong.vo.ResponseBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/07/22:47
 * @Description:
 **/
public interface UserAuthenticationService {
    /**
     * 用户登录
     * */
    String login(User user);

    /**
    * 退出登录
    * */
    void loginOut(String token);

    /**
     * 获取用户身份
     * */
    List<Role> getUserRole(User user);

    /**
     * 保存token
     * */
    void saveToken(String newToken, User user);

    User registered(RegisteredVO registeredVO);

    String generateToken(User user);
}
