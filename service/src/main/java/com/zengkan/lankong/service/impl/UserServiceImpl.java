package com.zengkan.lankong.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/07/23:51
 * @Description:
 **/
@Service
public class UserServiceImpl implements UserService {

    private static final String string = "userToken";
    
    private final RedisUtil redisUtil;

    private final ShaUtil shaUtil;

    private final UserMapper userMapper;

    private final UserRoleMapper roleMapper;

    @Autowired
    public UserServiceImpl(RedisUtil redisUtil, ShaUtil shaUtil, UserMapper userMapper, UserRoleMapper roleMapper) {
        this.redisUtil = redisUtil;
        this.shaUtil = shaUtil;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public ResponseBean login(User user){
        ResponseBean result = new ResponseBean();
        user.setPassword(shaUtil.getSha(user.getPassword()));
        User users=userMapper.login(user);
        if (users != null) {
            try {
                Map<String, Object> map = MapUtils.getObjectToMap(users);
                //生成token
                String token = JwtUtil.createToken(map);
                saveToken(token, user);
                result.setData(user);
                result.setMsg(token);
                return result;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void loginOut(String token) {
        User user = (User) redisUtil.getString(token);
        String isToken = (String) redisUtil.hGet(string,user.getId());
        if (token != null) {
            redisUtil.del(isToken);
        }
        redisUtil.hDelete(string,user.getId());
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
        String oldToken = (String) redisUtil.hGet(string,user.getId());
        if (oldToken != null){
            redisUtil.del(oldToken);
        }
        redisUtil.setHash(string,user.getId(),token);
    }
}
