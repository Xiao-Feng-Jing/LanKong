package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.IndexImg;
import com.zengkan.lankong.service.IndexImageService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/19/21:58
 * @Description:
 **/
@RestController
@RequestMapping("/indexImage")
@Api(tags = "首页轮播图管理")
public class IndexImageController {

    private final IndexImageService indexImageService;

    @Autowired
    public IndexImageController(IndexImageService indexImageService) {
        this.indexImageService = indexImageService;
    }

    @GetMapping
    @ApiOperation(value = "获取轮播图", notes = "获取轮播图，权限需要：无")
    public ResponseBean imageAddr(){
        return new ResponseBean(CodeEnum.SUCCESS, indexImageService.listIndexImages());
    }

    @DeleteMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "删除轮播图", notes = "根据轮播图id删除，权限需求：管理员")
    @ApiImplicitParam(name = "id", value = "轮播图ID", required = true, paramType = "query", dataType = "String")
    public ResponseBean deleteImageAddr(@RequestParam("id") String id) {
        indexImageService.deleteById(id);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    @PostMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "添加轮播图", notes = "根据轮播图数据模型更新，权限需求：管理员")
    public ResponseBean saveImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, indexImageService.save(indexImg));
    }

    @PutMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "更新轮播图", notes = "根据轮播图数据模型更新，权限需求：管理员")
    public ResponseBean updateImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, indexImageService.update(indexImg));
    }
}
