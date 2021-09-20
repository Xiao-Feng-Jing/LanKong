package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.SpuDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/12/19:37
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface SpuDetailMapper {

    /**
     * 保存spu详情信息
     * @param spuDetail 商品详情实体类
     * */
    @Insert("insert spu_detail(spu_id,description,specifications, spec_template, packing_list, after_service) " +
            "values(#{spuId},#{description},#{specifications},#{specTemplate},#{packingList},#{afterService})")
    void save(SpuDetail spuDetail);

    /**
     * 根据spuId查询详情信息
     * @param spuId spuId
     * @return 详情信息
     * */
    @Select("select spu_id,description,specifications,spec_template,packing_list,after_service from spu_detail where spu_id = #{spuId}")
    SpuDetail queryById(String spuId);

    /**
     * 更新spu详情信息
     * @param spuDetail 商品详情实体类
     * */
    @Update("update spu_detail set description = #{description},specifications = #{specifications}, " +
            "spec_template = #{specTemplate}, packing_list = #{packingList}, after_service = #{afterService} " +
            "where spu_id = #{spuId}")
    void updateBySpuId(SpuDetail spuDetail);

    /**
     * 根据spuId集合删除商品详情数据
     * @param ids spuId集合
     * */
    @Delete("<script>delete from spu_detail where spu_id in " +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    void deleteBySpuIds(List<String> ids);

    /**
     * 根据spuId删除商品详情数据
     * @param spuId spuId
     * */
    @Delete("delete from spu_detail where spu_id = #{spuId}")
    void deleteBySpuId(String spuId);

}
