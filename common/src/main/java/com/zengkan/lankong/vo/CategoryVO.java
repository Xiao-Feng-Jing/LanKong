package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.pojo.GoodsCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version : 1.0.0
 * @Date : 2021/09/16/1:54
 * @Description : 推荐商品分类对外接口
 * @modified By :
 **/
@Data
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 7309818923058079242L;

    /**
     * 推荐分类
     * */
    private CategoryRecommendsVO categoryRecommend;

    /**
     * 推荐分类的子分类
     * */
    private List<GoodsCategory> categories;

    /**
     * 实现首页分类商品推荐
     * */
    private List<GoodsVO> products;
}
