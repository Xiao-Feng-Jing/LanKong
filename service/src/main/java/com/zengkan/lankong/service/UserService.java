package com.zengkan.lankong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.vo.ResponseBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/07/22:47
 * @Description:
 **/
public interface UserService {
    /**
     * 用户登录
     * */
    String login(User user);

    /**
    * 退出登录
    * */
    void loginOut(String token);
    /**
     * 用户查询
     * */
    User getUser(String username);

    /**
     * 获取用户身份
     * */
    UserRole getUserRole(User user);

    /**
     * 保存token
     * */
    void saveToken(String newToken, User user);

}
