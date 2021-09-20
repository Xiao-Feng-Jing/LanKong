package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.WarehouseGoods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/12/20:11
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface WarehouseGoodsMapper {


    /**
     * 保存库存
     * @param warehouseGoods 库存实体类
     */
    @Insert("insert into warehouse_goods(sku_id,current_cnt) values(#{skuId},#{currentCnt})")
    void save(WarehouseGoods warehouseGoods);

    /**
     * 根据skuId集合删除库存
     * @param ids skuId集合
     * */
    @Delete("<script>delete from warehouse_goods where sku_id in " +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    void deleteBySkuIds(List<String> ids);

    /**
     * 根据skuId更新库存信息
     * @param warehouseGoods 库存实体类
     * */
    @Update("update warehouse_goods set current_cnt = #{currentCnt} where sku_id = #{skuId}")
    void updateBySkuId(WarehouseGoods warehouseGoods);

    /**
     * 根据skuId删除库存
     * @param skuId skuId
     * */
    @Delete("delete from warehouse_goods where sku_id = #{skuId}")
    void deleteBySkuId(String skuId);

    /**
     * 根据skuId查询库存
     * @param skuId 商品ID
     * @return 库存信息
     * */
    @Select("select sku_id, current_cnt from warehouse_goods where sku_id = #{sku_id}")
    WarehouseGoods queryBySkuId(String skuId);

    /**
     * 根据skuId集合查询库存
     * @param ids skuId集合
     * @return 库存集合
     * */
    @Select("<script>select sku_id, current_cnt from warehouse_goods where sku_id in " +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    List<WarehouseGoods> queryBySpuIds(List<String> ids);

    /**
     * 根据skuId减少库存信息
     * @param skuId 商品id
     * @param num 减少的库存量
     * */
    @Update("update warehouse_goods set current_cnt =current_cnt - #{num} where sku_id = #{skuId}")
    void reduceStock(String skuId, long num);
}
