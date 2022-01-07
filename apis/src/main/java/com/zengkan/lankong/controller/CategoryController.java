package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.GoodsCategory;
import com.zengkan.lankong.service.CategoryService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/25/16:02
 * @Description:
 **/
@RestController
@RequestMapping("/category")
@Api(tags = "商品分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 根据父分类查询商品类目
     * @param pid 父分类
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "商品分类查询", notes = "根据层级显示，权限需要：无")
    @ApiImplicitParam(name = "pid", value = "父分类id", required = true, paramType = "query", dataType = "Long", defaultValue = "0")
    public ResponseBean listCategory(@RequestParam(value = "pid", defaultValue = "0")Long pid){
        return new ResponseBean(CodeEnum.SUCCESS, categoryService.category(pid));
    }

    /**
     * 删除分类
     * @param cid 分类ID
     * @return
     */
    @DeleteMapping("/cid/{cid}")
    @RequiresRoles("admin")
    @ApiOperation(value = "删除商品分类", notes = "根据分类ID删除, 权限需要：管理员")
    @ApiImplicitParam(name = "cid", value = "分类id", required = true, paramType = "path", dataType = "Long")
    public ResponseBean deleteCategory(@PathVariable("cid") long cid) {
        categoryService.deleteById(cid);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 添加商品分类
     * @param goodsCategory 分类数据
     * */
    @PostMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "添加商品分类", notes = "前端传入分类的数据模型，权限需要：管理员")
    public ResponseBean saveCategory(@RequestBody GoodsCategory goodsCategory){
        GoodsCategory category = categoryService.saveCategory(goodsCategory);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, category);
    }

    /**
     * 修改分类数据
     * @param goodsCategory 分类数据
     * */
    @PutMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "修改商品分类", notes = "前端传入分类的数据模型，权限需要：管理员")
    public ResponseBean updateCategory(@RequestBody GoodsCategory goodsCategory){
        categoryService.updateCategory(goodsCategory);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }
}
