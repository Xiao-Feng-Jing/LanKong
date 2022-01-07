package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserAuthenticationService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.RegisteredVO;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "用户认证相关接口")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * 无权限获取相应的数据
     * */
    @GetMapping("/noAuth")
    @ApiOperation(value = "无权限访问接口", notes = " 权限需要：无")
    public ResponseBean noAuth() {
        return new ResponseBean(ExceptionEnum.NO_AUTHORIZED,null);
    }

    /**
     * 退出登录
     * @param request http请求
     * @return 退出登录是否成功
     * */
    @DeleteMapping("/login")
    @RequiresRoles(logical = Logical.OR,value = {"user","admin"})
    @ApiOperation(value = "退出登录接口", notes = "权限需要：管理员或用户")
    public void outLogin(HttpServletRequest request) {
        userAuthenticationService.loginOut(request.getHeader("Authorization"));

    }

    /**
     * 登录
     * @param user 账号密码
     * @return 登录是否成功
     * */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口", notes = "权限需要：无")
    public ResponseBean login(@RequestBody User user){
        return new ResponseBean(CodeEnum.SUCCESS, userAuthenticationService.login(user));
    }

    /**
     * 注册
     * */
    @PostMapping("/registered")
    @ApiOperation(value = "用户注册接口", notes = "权限需要：无")
    public ResponseBean registered(@RequestBody RegisteredVO registeredVO) {
        return new ResponseBean(CodeEnum.SUCCESS, userAuthenticationService.registered(registeredVO));
    }


}
