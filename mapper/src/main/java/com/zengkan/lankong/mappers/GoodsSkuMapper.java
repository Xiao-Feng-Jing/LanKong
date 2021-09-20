package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.GoodsSku;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/12/19:33
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface GoodsSkuMapper {

    /**
     * 保存sku信息
     * @param sku sku实体类
     * */
    @Insert("insert goods_sku(sku_id, spu_id,title, images, price, sale_price, enable, indexes, own_spec, create_time, last_update_time) " +
            "values(#{skuId},#{spuId},#{title},#{images},#{price},#{salePrice},#{enable},#{indexes},#{ownSpec},#{createTime},#{lastUpdateTime})")
    void save(GoodsSku sku);

    /**
     * 根据spuId集合查询sku
     * @param ids spuId集合
     * @return sku集合
     * */
    @Select("<script>select sku_id,spu_id,title,images, price, sale_price, enable, indexes, own_spec, create_time, last_update_time " +
            "from goods_sku where spu_id in " +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    List<GoodsSku> selectSkuBySpuIds(List<String> ids);

    /**
     * 根据spuId查询sku
     * @param spuId spuId
     * @return sku集合
     * */
    @Select("select sku_id,spu_id,title,images, price, sale_price, enable, indexes, own_spec, create_time, last_update_time from goods_sku where spu_id = #{spuId}")
    List<GoodsSku> selectSkuBySpuId(String spuId);

    /**
     * 更新sku数据
     * @param sku sku实体类
     * */
    @Update("update goods_sku set title = #{title},images = #{images},price = #{price},sale_price = #{sale_price},last_update_time = #{lastUpdateTime} where sku_id = #{skuId}")
    void updateBySkuId(GoodsSku sku);

    /**
     * 根据skuId 删除sku数据
     * @param skuId sku_id
     * */
    @Delete("delete from goods_sku where sku_id = #{skuId}")
    void deleteBySkuId(String skuId);

    /**
     * 根据spuId 删除sku数据
     * @param ids spuId集合
     * */
    @Delete("<script>delete from goods_sku where spu_id in" +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    void deleteBySpuId(List<String> ids);

    /**
     * 根据skuId查询sku
     * @param skuId skuId
     * */
    @Select("select sku_id,spu_id,title,images, price, sale_price, enable, indexes, own_spec, create_time, last_update_time from goods_sku where sku_id = #{skuId}")
    GoodsSku querySkuBySkuId(String skuId);
}
