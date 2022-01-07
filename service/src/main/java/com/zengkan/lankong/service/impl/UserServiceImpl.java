package com.zengkan.lankong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.CustomerInfMapper;
import com.zengkan.lankong.mappers.UserMapper;
import com.zengkan.lankong.mappers.UserRoleMapper;
import com.zengkan.lankong.pojo.CustomerInf;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.utils.ShaUtil;
import com.zengkan.lankong.vo.UserInfoVO;
import com.zengkan.lankong.vo.UserRoleVO;
import com.zengkan.lankong.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/14:18
 * @Description : 用户相关管理
 * @modified By :
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final CustomerInfMapper customerInfMapper;
    private final RedisUtil redisUtil;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper, CustomerInfMapper customerInfMapper, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.customerInfMapper = customerInfMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO update(UserInfoVO userInfoVO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = userInfoVO.getUser();
        CustomerInf customerInf = userInfoVO.getCustomerInf();
        if (!redisUtil.hGet("user", user.getId()).equals(token)) {
            throw new MyException(ExceptionEnum.INVALID_TOKEN);
        }
        user.setModifiedTime(LocalDateTime.now());
        user.setPassword(ShaUtil.getSha(user.getPassword()));
        if (userMapper.update(user) && customerInfMapper.updateInfo(customerInf)) {
            redisUtil.setString(token, userInfoVO);
            return userInfoVO;
        }
        throw new MyException(ExceptionEnum.ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void status(List<Long> ids) {
        if (!userMapper.updateById(ids)) {
            throw new MyException(ExceptionEnum.ERROR);
        }
    }

    @Override
    public List<UserVO> userList(Integer pageNum, Integer rows, Boolean status, Boolean online) {
        Set<Object> ids = redisUtil.hGet("user");
        PageHelper.startPage(pageNum, Math.min(rows, 100));
        Page<UserVO> userPage = null;
        if (online != null) {
            /*
             true: 显示在线用户，
             false: 显示不在线用户
            */
            if (online) {
                userPage = (Page<UserVO>) userMapper.selectByIds(ids, status);
            }else {
                userPage = (Page<UserVO>) userMapper.selectNotByIds(ids, status);
            }
        }else {
            userPage = (Page<UserVO>) userMapper.select(status);
        }

        List<UserVO> userVOList = userPage.getResult();

        userVOList.forEach(userVO -> userVO.setIsOnline(ids.contains(userVO.getId())));

        // 建立userId -> UserVO的映射
        Map<Long, UserVO> map = new HashMap<>();

        // 将相同的角色进行处理
        Iterator<UserVO> it = userVOList.iterator();
        while (it.hasNext()) {
            UserVO userVO = it.next();
            if (!map.containsKey(userVO.getId())) {
                map.put(userVO.getId(), userVO);
                continue;
            }
            UserVO userVo = map.get(userVO.getId());
            userVo.setRole(userVo.getRole()+"/"+userVO.getRole());
            it.remove();
        }

        return userVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserRole> reviseRole(UserRoleVO userRoleVO) {
        List<UserRole> userRoles = userRoleVO.getUserRoleList();
        userRoleMapper.deleteByUserIds(userRoles);
        userRoleMapper.saveList(userRoles);
        return userRoles;
    }

    @Override
    public UserInfoVO selectUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = (User) redisUtil.getString(token);
        user = userMapper.selectbyId(user.getId());
        CustomerInf customerInf = customerInfMapper.selectByUserId(user.getId());
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user);
        userInfoVO.setCustomerInf(customerInf);
        return userInfoVO;
    }
}
