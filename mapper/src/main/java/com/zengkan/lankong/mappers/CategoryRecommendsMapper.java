package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.vo.CategoryRecommendsVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:40
 * @Description : 推荐分类
 * @modified By :
 **/
@Repository
@Mapper
public interface CategoryRecommendsMapper {
    /**
     * 添加推荐分类
     * */
    @Insert("INSERT INTO category_recommends(category_id, create_time, update_time) VALUES(#{cid}, #{createTime}, #{updateTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveCategory(CategoryRecommends cid);

    /**
     * 修改推荐分类
     * */
    @Update("update category_recommends set category_id = #{categoryId}, ipdate_time = #{updateTime} where id = #{id}")
    void updateCategory(CategoryRecommends categoryRecommends);

    /**
     * 删除推荐分类
     * */
    @Delete("delete from category_recommends where id = #{id}")
    void deleteById(long id);

    /**
     * 查询所有推荐分类
     * */
    @Select("select id, rec.category_id, create_time, update_time, category_name " +
            "from category_recommends as rec left join goods_category goods " +
            "on rec.category_id = goods.category_id")
    List<CategoryRecommendsVO> queryCategoriesc();
}
