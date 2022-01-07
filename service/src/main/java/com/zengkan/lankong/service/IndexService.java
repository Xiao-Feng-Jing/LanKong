package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.vo.CategoryVO;
import com.zengkan.lankong.vo.GoodsVO;
import com.zengkan.lankong.vo.PageResult;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:50
 * @Description :
 * @modified By :
 **/
public interface IndexService {

    /**
     * 首页获取推荐分类
     * */
    List<CategoryVO> listCategoryVO();

    /**
     * 获取热门商品
     * */
    List<GoodsVO> listBestSellingGoods();

    /**
     * 获取精品商品
     * */
    List<GoodsVO> listBoutiqueGoods();

    /**
     * 获取热搜关键词
     *
     * @return*/
    Set<Object> listSearchKey();

    PageResult<GoodsVO> querySpuByCategoryId(long cid, int pageNum, int rows);

    PageResult<GoodsVO> searchGoodsSpu(String key, int pageNum, int rows);
}
