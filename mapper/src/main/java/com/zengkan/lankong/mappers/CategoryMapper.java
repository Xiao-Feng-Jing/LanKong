package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.GoodsCategory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/25/16:59
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface CategoryMapper{

    /**
     * 根据pid查询商品分类
     * @param pid 父分类id
     * @return 商品分类实体类集合
     */
    @Select("select category_id, category_name, parent_id, is_parent, category_level,category_status from goods_category where parent_id = #{pid}")
    List<GoodsCategory> category(long pid);

    /**
     * 根据商品id查询商品分类
     * @param cid 商品id
     * @return 商品分类实体类
     */
    @Select("select category_id, category_name, parent_id, is_parent, category_level,category_status from goods_category where category_id = #{cid}")
    GoodsCategory findById(long cid);

    /**
     * 根据商品id删除商品分类
     * @param cid 商品id
     */
    @Delete("delete from goods_category where category_id = #{cid}")
    void deleteById(long cid);

    /**
     * 根据商品id修改商品分类是否是父节点
     * @param cid 商品id
     * @param b 表示是否为父节点
     */
    @Update("update goods_category set is_parent = #{b} where category_id = #{cid}")
    void updateByIdAndIsParent(long cid, boolean b);

    /**
     * 添加商品分类
     * @param goodsCategory 全新商品分类
     */
    @Insert("INSERT INTO goods_category(category_name,parent_id,is_parent,category_level) VALUES(#{categoryName}, #{parentId},false,#{categoryLevel});")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId", keyColumn = "category_id")
    void insert(GoodsCategory goodsCategory);

    /**
     * 根据商品id修改商品分类
     * @param goodsCategory 新的商品数据
     */
    @Update("update goods_category set category_name = #{categoryName} where category_id = #{categoryId}")
    void updateByIdAndName(GoodsCategory goodsCategory);

}
