package com.zengkan.lankong.controller.admin;

import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.service.CategoryRecommendsService;
import com.zengkan.lankong.vo.CategoryRecommendsVO;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
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
@RequestMapping("manage/category-recommends")
@Api(tags = "推荐分类接口")
public class CategoryRecommendsController {

    private final CategoryRecommendsService categoryRecommendsService;

    @Autowired
    public CategoryRecommendsController(CategoryRecommendsService categoryRecommendsService) {
        this.categoryRecommendsService = categoryRecommendsService;
    }

    @PostMapping
    public ResponseBean addRecommendsCategory(@RequestParam("cid") Long cid){
        CategoryRecommends categoryRecommends = categoryRecommendsService.saveCategory(cid);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, categoryRecommends);
    }

    @PutMapping
    public ResponseBean updateRecommendsCategory(@RequestBody CategoryRecommendsVO categoryRecommendsVO) {
        categoryRecommendsService.updateCategory(categoryRecommendsVO);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    @DeleteMapping
    public ResponseBean deleteRecommendsCategory(@PathVariable("id") Long id) {
        categoryRecommendsService.deleteById(id);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    @GetMapping
    public ResponseBean listRecommendsCategory(){
        return new ResponseBean(CodeEnum.SUCCESS, categoryRecommendsService.queryCategories());
    }


}
