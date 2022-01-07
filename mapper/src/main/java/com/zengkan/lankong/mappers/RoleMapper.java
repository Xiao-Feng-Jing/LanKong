package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.Role;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/28/17:40
 * @Description :
 * @modified By :
 **/
@Repository
@Mapper
public interface RoleMapper {

    /**
     * 根据角色id查询角色
     * */
    @Select("select id, role from role where id = #{id}")
    Role findRole(long id);

    /**
     * 根据角色名查询角色
     * */
    @Select("select id, role from role where role = #{user}")
    Role findRoleByName(String user);
}
