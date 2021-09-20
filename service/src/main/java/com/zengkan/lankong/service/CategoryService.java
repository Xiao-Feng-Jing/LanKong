package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.GoodsCategory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/25/16:04
 * @Description:
 **/
public interface CategoryService {

    List<GoodsCategory> category(long pid);

    void deleteById(long cid);

    GoodsCategory saveCategory(GoodsCategory goodsCategory);

    void updateCategory(GoodsCategory goodsCategory);

    List<String> queryNameByIds(List<Long> asList);
}
