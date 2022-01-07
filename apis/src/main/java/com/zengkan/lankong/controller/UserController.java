package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.pojo.CustomerInf;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.UserService;
import com.zengkan.lankong.vo.ResponseBean;
import com.zengkan.lankong.vo.UserInfoVO;
import com.zengkan.lankong.vo.UserRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/14:14
 * @Description : 用户相关管理
 * @modified By :
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "用户数据管理")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 更新用户基础资料
     * */
    @PostMapping("/update")
    @RequiresRoles(logical = Logical.OR, value = {
        "user", "admin"
    })
    @ApiOperation(value = "更新用户信息", notes = "角色：用户或管理员")
    public ResponseBean update(@RequestBody UserInfoVO userInfoVO, HttpServletRequest request) {
        return new ResponseBean(CodeEnum.SUCCESS, userService.update(userInfoVO, request));
    }

    /**
     * 获取用户资料
     * */
    @GetMapping("/info")
    @RequiresRoles(logical = Logical.OR, value = {"admin","user"})
    @ApiOperation(value = "获取用户本人信息", notes = "角色：用户或管理员")
    public ResponseBean getInfo(HttpServletRequest request) {
        return new ResponseBean(CodeEnum.SUCCESS, userService.selectUser(request));
    }

    /**
     * 静默或启用用户
     * */
    @GetMapping("/status")
    @RequiresRoles( value = "admin")
    @ApiOperation(value = "静默或启用用户", notes = "角色：管理员")
    @ApiImplicitParam(value = "用户ID", name = "ids", required = true, paramType = "path", dataType = "Long")
    public ResponseBean status(@RequestParam("ids") List<Long> ids) {
        userService.status(ids);
        return new ResponseBean(CodeEnum.SUCCESS, null);
    }

    /**
     * 分页显示所有的用户
     * */
    @GetMapping("/userList")
    @RequiresRoles(value = "admin")
    @ApiOperation(value = "分页显示所有的用户", notes = "角色：管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "10", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "账号状态", required = false, paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "online", value = "用户状态", required = false, paramType = "query", dataType = "Boolean"),
    })
    public ResponseBean userList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                 @RequestParam(value = "status", required = false) Boolean status,
                                 @RequestParam(value = "online", required = false) Boolean online) {
        return new ResponseBean(CodeEnum.SUCCESS, userService.userList(pageNum, rows, status, online));
    }

    /**
     * 修改用户角色
     **/
    @PostMapping("/reviseRole")
    @RequiresRoles(value = "admin")
    @ApiOperation(value = "修改用户角色", notes = "角色：管理员")
    public ResponseBean reviseRole(@RequestBody UserRoleVO userRoleVO) {
        return new ResponseBean(CodeEnum.SUCCESS, userService.reviseRole(userRoleVO));
    }
}
