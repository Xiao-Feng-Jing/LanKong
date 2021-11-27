package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/08/22:10
 * @Description: 管理员账号相关
 **/
@RestController
@Api(tags = "登录接口")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 退出登录
     * @param request http请求
     * @return 退出登录是否成功
     * */
    @DeleteMapping("/login")
    @ApiOperation("退出登录接口")
    @RequiresRoles(logical = Logical.OR,value = {"user","admin"})
    public void outLogin(HttpServletRequest request) {
        userService.loginOut(request.getHeader("Authorization"));

    }

    /**
     * 登录
     * @param user 账号密码
     * @return 登录是否成功
     * */
    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public ResponseBean login(@RequestBody User user){
        return new ResponseBean(CodeEnum.SUCCESS, userService.login(user));
    }
}
