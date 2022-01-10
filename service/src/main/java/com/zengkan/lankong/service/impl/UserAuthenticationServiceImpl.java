package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.CustomerInfMapper;
import com.zengkan.lankong.mappers.RoleMapper;
import com.zengkan.lankong.mappers.UserMapper;
import com.zengkan.lankong.mappers.UserRoleMapper;
import com.zengkan.lankong.pojo.CustomerInf;
import com.zengkan.lankong.pojo.Role;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.service.UserAuthenticationService;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.utils.ShaUtil;
import com.zengkan.lankong.vo.RegisteredVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/07/23:51
 * @Description:
 **/
@PropertySource(value = "classpath:user.properties")
@Service
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final String STRING = "userToken";
    
    private final RedisUtil redisUtil;

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final CustomerInfMapper customerInfMapper;

    private final RoleMapper roleMapper;

    @Value(value = "${user.AvatarUrl}")
    private String headerUrl;

    @Autowired
    public UserAuthenticationServiceImpl(RedisUtil redisUtil, UserMapper userMapper, UserRoleMapper roleMapper, CustomerInfMapper customerInfMapper, RoleMapper roleMapper1) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
        this.userRoleMapper = roleMapper;
        this.customerInfMapper = customerInfMapper;
        this.roleMapper = roleMapper1;
    }

    @Override
    public String login(User user){
        user.setPassword(ShaUtil.getSha(user.getPassword()));
        User users=userMapper.login(user);
        if (users != null) {
            return generateToken(users);
        }
        throw new MyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
    }

    @Override
    public void loginOut(String token) {
        User user = (User) redisUtil.getString(token);
        String isToken = (String) redisUtil.hGet(STRING,user.getId());
        if (token != null) {
            redisUtil.del(isToken);
            redisUtil.hDelete("user", user.getId());
        }
    }

    @Override
    public List<Role> getUserRole(User user) {
        List<UserRole> userRole = userRoleMapper.findRoleId(user.getId());
        List<Role> roles = new ArrayList<>();
        for (UserRole userRole1 : userRole) {
            roles.add(roleMapper.findRole(userRole1.getRoleId()));
        }
        return roles;
    }

    /**
     * 将token保存到Redis中
     * @param token token
     * @param user 用户对象
     */
    @Override
    public void saveToken(String token, User user){
        redisUtil.setString(token, user, 1800);
        String oldToken = (String) redisUtil.hGet("user", String.valueOf(user.getId()));
        if (oldToken != null) {
            redisUtil.del(oldToken);
        }
        redisUtil.setHash("user", String.valueOf(user.getId()), token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registered(RegisteredVO registeredVO) {
        if (registeredVO.getUsername() == null || registeredVO.getUsername().length() == 0 ||
                registeredVO.getPassword() == null || registeredVO.getPassword().length() == 0 ||
                !registeredVO.getPassword().equals(registeredVO.getPassword2())) {
            throw new MyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        User user = new User();
        user.setUsername(registeredVO.getUsername());
        user.setPassword(ShaUtil.getSha(registeredVO.getPassword()));
        user.setStats(true);
        user.setUrl(headerUrl);
        user.setModifiedTime(LocalDateTime.now());
        user=userMapper.registered(user);

        Role role = roleMapper.findRoleByName("user");
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleMapper.save(userRole);

        CustomerInf customerInf = new CustomerInf();
        customerInf.setCustomerId(user.getId());
        customerInf.setCustomerName(registeredVO.getName());
        customerInf.setIdentityCardType(registeredVO.getCardType());
        customerInf.setMobilePhone(registeredVO.getMobilePhone());
        customerInf.setRegisterTime(user.getModifiedTime());
        customerInfMapper.save(customerInf);
        return user;
    }

    @Override
    public String generateToken(User user) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            map.put("password", user.getPassword());
            //生成token
            String token = JwtUtil.createToken(map);
            saveToken(token, user);
            // 成功
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("user 转 Map 失败");
            // 失败
            throw new MyException(ExceptionEnum.ERROR);
        }
    }
}
