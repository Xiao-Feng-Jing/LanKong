package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.pojo.GoodsCategory;
import com.zengkan.lankong.vo.CategoryRecommendsVO;
import com.zengkan.lankong.vo.CategoryVO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:28
 * @Description : 推荐分类接口
 * @modified By :
 **/
public interface CategoryRecommendsService {

    /**
     * 获取所有推荐分类以及子分类，同时返回14个销量最高的商品
     * */
    List<CategoryRecommendsVO> queryCategories();

    /**
     * 根据id删除推荐分类
     * */
    void deleteById(long id);

    /**
     * 添加推荐分类
     * */
    CategoryRecommends saveCategory(long cid);

    /**
     * 更新推荐分类
     * */
    void updateCategory(CategoryRecommendsVO categoryRecommendsVO);
}
