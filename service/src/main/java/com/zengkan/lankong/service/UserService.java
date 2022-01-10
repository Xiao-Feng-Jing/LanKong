package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.CustomerInf;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.pojo.UserRole;
import com.zengkan.lankong.vo.PasswordVO;
import com.zengkan.lankong.vo.UserInfoVO;
import com.zengkan.lankong.vo.UserRoleVO;
import com.zengkan.lankong.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/14:17
 * @Description :
 * @modified By :
 **/
public interface UserService {
    UserInfoVO update(UserInfoVO userInfoVO, HttpServletRequest request);

    void status(List<Long> id);

    List<UserVO> userList(Integer pageNum, Integer rows, Boolean status, Boolean online);

    List<UserRole> reviseRole(UserRoleVO userRoleVO);

    UserInfoVO selectUser(HttpServletRequest request);

    String updatePassword(PasswordVO passwordVO, HttpServletRequest request);
}
