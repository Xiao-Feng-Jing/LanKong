package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * 无权限获取相应的数据
     * */
    @GetMapping("/noAuth/{message}")
    @ApiOperation("非法请求接口")
    public ResponseBean noAuth(@PathVariable("message") String message) {
        return new ResponseBean(401,message,null);
    }

    /**
     * 退出登录
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
        ResponseBean result = userService.login(user);
        if (result != null) {
            result.setCode(200);
            log.info("{},登录成功",user.getUsername());
        }else {
            result = new ResponseBean(412,"账号或密码错误",null);
            log.info("{},登录失败",user.getUsername());
        }
        return result;
    }
}
