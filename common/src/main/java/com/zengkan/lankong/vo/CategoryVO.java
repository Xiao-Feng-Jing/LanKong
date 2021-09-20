package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.GoodsCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryVO extends GoodsCategory {
    private static final long serialVersionUID = 7309818923058079242L;
    /**
     * 实现首页的类别显示
     * */
    private List<GoodsCategory> categories;
    /**
     * 实现首页分类商品推荐
     * */
    private List<SpuVo> products;

}
