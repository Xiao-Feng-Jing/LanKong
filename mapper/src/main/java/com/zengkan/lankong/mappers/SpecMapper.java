package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.TbSpecification;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/19:10
 * @Description:
 **/
@Repository
@Mapper
public interface SpecMapper {

    /**
     * 添加分类下的规格模板
     * @param tbSpecification 添加的数据
     * */
    @Insert("insert into tb_specification(category_id,specifications) values(#{categoryId},#{specifications})")
    void save(TbSpecification tbSpecification);

    /**
     * 查询分类id下的规格参数模板
     * @param id 分类id
     * @return TbSpecification 查询结果
     * */
    @Select("select category_id,specifications from tb_specification where category_id = #{id}")
    TbSpecification queryById(long id);

    /**
     * 修改分类id下的规格参数模板
     * @param tbSpecification 查询数据
     * */
    @Update("update tb_specification set specifications = #{specifications} where category_id = #{categoryId}")
    void updateById(TbSpecification tbSpecification);

    /**
     * 删除分类id下的规格参数模板
     * @param id 分类id
     * */
    @Delete("delete from tb_specification where category_id = #{id}")
    void deleteById(long id);

}
