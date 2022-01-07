package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.GoodsSpu;
import com.zengkan.lankong.vo.CommoditySalesVO;
import com.zengkan.lankong.vo.GoodsVO;
import com.zengkan.lankong.vo.SpuQueryByPage;
import com.zengkan.lankong.vo.SpuVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/21:30
 * @Description: spu数据
 **/
@Repository
@Mapper
public interface GoodsSpuMapper {
    /**
     * 根据条件分页查询
     * */
    @Select("<script>" +
            "SELECT spu_id, goods_name, title, cid1, cid2, cid3, " +
            "saleable, is_new, create_date, modified_time " +
            " FROM goods_spu " +
            "<where>" +
            "<if test='key != null and key != \"\"'> MATCH(goods_name,title) AGAINST (#{key} IN NATURAL LANGUAGE MODE)</if> " +
            "<if test='saleable != null and saleable != \"\"'> AND saleable=#{saleable}</if> " +
            "<if test='isNew != null and isNew != \"\"'> AND is_new=#{isNew}</if> " +
            "</where>" +
            "</script>")
    List<GoodsSpu> pageList(SpuQueryByPage spuQueryByPage);

    /**
     * 保存spu数据
     * @param spuVo spu实体类
     * */
    @Insert("insert goods_spu(spu_id, goods_name, title, cid1, " +
            "cid2, cid3, is_new, saleable, create_date, modified_time) " +
            "values(#{spuId}, #{goodsName}, #{title}, #{cid1}, #{cid2}, " +
            "#{cid3}, #{valid}, #{saleable}, #{createDate}, #{modifiedTime})")
    void save(SpuVO spuVo);

    /**
     * 更新spu数据
     * @param spuVo spu实体类
     * */
    @Update("update goods_spu set goods_name = #{goodsName}, " +
            "title = #{title}, cid1=#{cid1}, cid2=#{cid2}, cid3=#{cid3}, " +
            "modified_time = #{modifiedTime} " +
            "where spu_id = #{spuId}")
    void updateBySpuId(SpuVO spuVo);

    /**
     * 根据spuId 查询商品
     * @param spuId spuId
     * @return GoodsSpu 商品信息
     * */
    @Select("SELECT spu_id, goods_name, title, cid1, " +
            "cid2, cid3, saleable, is_new, create_date, " +
            "modified_time from goods_spu where spu_id = #{spuId}")
    GoodsSpu selectBySpuId(String spuId);

    /**
     * 上下架商品
     * @param spuIds spuId集合
     * */
    @Update("<script> update goods_spu set saleable = !saleable" +
            "<foreach collection='spuIds' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach>"+
            "</script>")
    void updateBySpuIdSelective(List<String> spuIds);

    /**
     * 根据spuId集合删除商品
     * @param ids spuId集合
     * */
    @Delete("<script>" +
            "delete from goods_spu where spu_id in" +
            "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
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

    /**
     * 根据cid查询商品
     * @param cid 分类id
     * */
    @Select("select spu.spu_id as spu_id, goods_name, spu.title as title," +
            " cid1, cid2, cid3, saleable, is_new, create_date," +
            " modified_time, sku_id, sku.title as sku_title, price, sale_price, images " +
            "from goods_spu spu right join goods_sku sku on spu.spu_id = sku.spu_id " +
            " where cid1 = #{cid} or cid2 = #{cid} or cid3 = #{cid} group by spu_id")
    List<GoodsVO> pageListByCategoryId(long cid);

    /**
     * 根据关键字查询商品
     * */
    @Select("select spu.spu_id as spu_id, goods_name, spu.title as title," +
            " cid1, cid2, cid3, saleable, is_new, create_date, modified_time, " +
            "sku_id, sku.title as sku_title, price, sale_price, images " +
            "from goods_spu spu right join goods_sku sku on spu.spu_id = sku.spu_id " +
            "where MATCH(goods_name,title) AGAINST (#{key} IN NATURAL LANGUAGE MODE) " +
            "group by sou_id")
    List<GoodsVO> pageListByKey(String key);

    /**
     * 根据分类查询商品，显示部分数据
     * */
    @Select("select spu.spu_id as spu_id, goods_name, spu.title as title," +
            " cid1, cid2, cid3, saleable, is_new, create_date," +
            " modified_time, sku_id, sku.title as sku_title, price, sale_price, images " +
            "from goods_spu spu right join goods_sku sku on spu.spu_id = sku.spu_id " +
            " where cid1 = #{cid} or cid2 = #{cid} or cid3 = #{cid} " +
            "group by spu_id limit 0, #{totals}")
    List<GoodsVO> goods(long cid, int totals);

    /**
     * 根据spuId集合
     * */
    @Select("<script>select spu.spu_id as spu_id, goods_name, " +
            "spu.title as title, cid1, cid2, cid3, saleable, " +
            "is_new, create_date, modified_time, sku_id, " +
            "sku.title as sku_title, price, sale_price, images " +
            "from goods_spu spu right join goods_sku sku " +
            "on spu.spu_id = sku.spu_id where spu.spu_id in" +
            "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach> and saleable = 1 group by spu_id"+
            "</script>")
    List<GoodsVO> listGoodsBySpuIds(Collection<Object> ids);

    /**
     * 根据skuId集合查询商品
     * */
    @Select("<script>select spu.spu_id as spu_id, goods_name, " +
            "spu.title as title, cid1, cid2, cid3, saleable, " +
            "is_new, create_date, modified_time, sku_id, " +
            "sku.title as sku_title, price, sale_price, images " +
            "from goods_spu spu right join goods_sku sku " +
            "on spu.spu_id = sku.spu_id where sku.sku_id in" +
            "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach> group by spu_id"+
            "</script>")
    List<GoodsVO> listGoodsBySkuIds(List<String> skuIds);
}
