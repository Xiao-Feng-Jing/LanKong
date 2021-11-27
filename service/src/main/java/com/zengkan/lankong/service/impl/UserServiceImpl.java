package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.UserMapper;
import com.zengkan.lankong.mappers.UserRoleMapper;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.MapUtils;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.utils.ShaUtil;
import com.zengkan.lankong.vo.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/07/23:51
 * @Description:
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String STRING = "userToken";
    
    private final RedisUtil redisUtil;

    private final UserMapper userMapper;

    private final UserRoleMapper roleMapper;

    @Autowired
    public UserServiceImpl(RedisUtil redisUtil, UserMapper userMapper, UserRoleMapper roleMapper) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public String login(User user){
        user.setPassword(ShaUtil.getSha(user.getPassword()));
        User users=userMapper.login(user);
        if (users != null) {
            try {
                Map<String, Object> map = MapUtils.getObjectToMap(users);
                //生成token
                String token = JwtUtil.createToken(map);
                saveToken(token, user);
                // 成功
                return token;
            } catch (Exception e) {
                log.error("user 转 Map 失败");
                // 失败
                throw new MyException(ExceptionEnum.ERROR);
            }
        }
        throw new MyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
    }

    @Override
    public void loginOut(String token) {
        User user = (User) redisUtil.getString(token);
        String isToken = (String) redisUtil.hGet(STRING,user.getId());
        if (token != null) {
            redisUtil.del(isToken);
        }
    }

    @Override
    public User getUser(String username) {
        return userMapper.query(username);
    }

    @Override
    public UserRole getUserRole(User user) {
        UserRole userRole = roleMapper.queryUserRole(user.getId());
        userRole.setUsername(user.getUsername());
        userRole.setPassword(user.getPassword());
        userRole.setId(user.getId());
        return userRole;
    }

    /**
     * 将token保存到Redis中
     * @param token token
     * @param user 用户对象
     */
    @Override
    public void saveToken(String token, User user){
        this.redisUtil.setString(token,user,1800);
    }
}
