package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.GoodsSpu;
import com.zengkan.lankong.vo.SpuQueryByPage;
import com.zengkan.lankong.vo.SpuVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/21:30
 * @Description: spu数据
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface GoodsSpuMapper {
    /**
     * 根据条件分页查询
     * */
    @Select("<script>" +
            "SELECT spu_id, goods_name, title,cid1,cid2,cid3,saleable,is_new,create_date,modified_time" +
            " FROM goods_spu " +
            "<where>" +
            "<if test='key != null'> MATCH(goods_name,title) AGAINST (#{key} IN NATURAL LANGUAGE MODE)</if> " +
            "<if test='saleable != null'> AND saleable=#{saleable}</if> " +
            "<if test='isNew != null'> AND is_new=#{isNew}</if> >" +
            "</where>" +
            "</script>")
    List<GoodsSpu> pageList(SpuQueryByPage spuQueryByPage);

    /**
     * 保存spu数据
     * @param spuVo spu实体类
     * */
    @Insert("insert goods_spu(spu_id,goods_name,title,cid1,cid2,cid3,is_new,saleable,create_date,modified_time) " +
            "values(#{spuId},#{goodsName},#{title},#{cid1},#{cid2},#{cid3},#{valid},#{saleable},#{createDate},#{modifiedTime})")
    void save(SpuVo spuVo);

    /**
     * 更新spu数据
     * @param spuVo spu实体类
     * */
    @Update("update goods_spu set goods_name = #{goodsName},title = #{title},cid1=#{cid1},cid2=#{cid2},cid3=#{cid3},modified_time = #{modifiedTime} " +
            "where spu_id = #{spuId}")
    void updateBySpuId(SpuVo spuVo);

    /**
     * 根据spuId 查询商品
     * @param spuId spuId
     * @return GoodsSpu 商品信息
     * */
    @Select("SELECT spu_id, goods_name, title,cid1,cid2,cid3,saleable,is_new,create_date,modified_time from goods_spu where spu_id = #{spuId}")
    GoodsSpu selectBySpuId(String spuId);

    /**
     * 上下架商品
     * @param goodsSpu 商品实体类
     * */
    @Update("update goods_spu set saleable = #{saleable} where spu_id = #{spuId}")
    void updateBySpuIdSelective(GoodsSpu goodsSpu);

    /**
     * 根据spuId集合删除商品
     * @param ids spuId集合
     * */
    @Delete("<script>delete from goods_spu where spu_id in" +
            "<foreach collection='collection' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    void deleteGoods(List<String> ids);

    /**
     * 根据spuId删除商品
     * @param spuId spuId
     * */
    @Delete("delete from goods_spu where spu_id = #{spuId}")
    void deleteGoodsById(String spuId);

}
