package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.IndexImg;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/21/0:38
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
public interface IndexImgMapper {

    @Select("select id,url,create_time,update_time from index_img")
    List<IndexImg> queryUrl();

    @Insert("insert into index_img(id,url,bg_color,create_time,update_time) values(#{id},#{url},#{createTime},#{modifiedTime})")
    int saveIndexImg(IndexImg carouselMap);

    @Update("update index_img set url = #{url},update_time = #{updateTime} where id = #{id}")
    int updateIndexImg(IndexImg carouselMap);

    @Delete("delete from index_img where id = #{id}")
    int deleteById(String id);
}
