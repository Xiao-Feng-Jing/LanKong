package com.zengkan.lankong.mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
@CacheNamespace(blocking = true)
public interface CategoryRecommendsMapper {
}
