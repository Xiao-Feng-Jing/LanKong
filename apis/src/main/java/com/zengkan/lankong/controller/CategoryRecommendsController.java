package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.service.CategoryRecommendsService;
import com.zengkan.lankong.vo.CategoryRecommendsVO;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version : 1.0.0
 * @Date : 2021/09/16/1:36
 * @Description : 推荐分类接口管理
 * @modified By :
 **/
@RestController
@RequestMapping("/category-recommends")
@Api(tags = "推荐分类接口")
public class CategoryRecommendsController {

    private final CategoryRecommendsService categoryRecommendsService;

    @Autowired
    public CategoryRecommendsController(CategoryRecommendsService categoryRecommendsService) {
        this.categoryRecommendsService = categoryRecommendsService;
    }

    @PostMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "添加推荐分类", notes = "通过分类ID添加，权限需要：管理员")
    @ApiImplicitParam(name = "categoryRecommends", value = "推荐分类对象")
    public ResponseBean addRecommendsCategory(@RequestBody CategoryRecommends categoryRecommends){
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, categoryRecommendsService.saveCategory(categoryRecommends));
    }

    @PutMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "修改推荐分类", notes = "通过推荐分类数据模型修改，权限需要：管理员")
    @ApiImplicitParam(name = "categoryRecommendsVO", value = "推荐分类对象对外接口")
    public ResponseBean updateRecommendsCategory(@RequestBody CategoryRecommendsVO categoryRecommendsVO) {
        categoryRecommendsService.updateCategory(categoryRecommendsVO);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    @DeleteMapping("{id}")
    @RequiresRoles("admin")
    @ApiOperation(value = "删除推荐分类", notes = "通过分类ID删除，权限需要：管理员")
    @ApiImplicitParam(value = "分类ID", name = "cid", required = true, paramType = "path", dataType = "Long")
    public ResponseBean deleteRecommendsCategory(@PathVariable("id") Long id) {
        categoryRecommendsService.deleteById(id);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    @GetMapping
    @ApiOperation(value = "查询推荐分类", notes = "获取所有的推荐，权限需要：无")
    @ApiImplicitParam
    public ResponseBean listRecommendsCategory(){
        return new ResponseBean(CodeEnum.SUCCESS, categoryRecommendsService.queryCategories());
    }
}
