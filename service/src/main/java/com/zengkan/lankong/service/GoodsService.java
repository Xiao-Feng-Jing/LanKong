package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.pojo.SpuDetail;
import com.zengkan.lankong.vo.GoodsVO;
import com.zengkan.lankong.vo.PageResult;
import com.zengkan.lankong.vo.SpuQueryByPage;
import com.zengkan.lankong.vo.SpuVO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/20:17
 * @Description:
 **/
public interface GoodsService {
    PageResult<SpuVO> pageList(SpuQueryByPage spuQueryByPage);

    void saveGoods(SpuVO spuVo);

    void updateGoods(SpuVO spuVo);
    

    void goodsSoldOut(List<String> spuId);

    void deleteGoods(List<String> spuId);

    GoodsSku querySkuBySkuId(String skuId);

    SpuVO querySpuById(String spuId);

    List<GoodsSku> querySkuBySpuId(String spuId);

    SpuDetail querySpuDetailBySpuId(String spuId);

    List<GoodsSku> querySkuBySkuIds(List<String> skuIds);
}
