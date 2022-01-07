package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.UserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/02/15:40
 * @Description:
 **/
@Repository
@Mapper
public interface UserRoleMapper {

    @Select("select id, user_id, role_id  FROM user_role WHERE user_id = #{id};")
    List<UserRole> findRoleId(long id);

    @Insert("insert user_role (user_id, role_id) values(#{userId}, #{roleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(UserRole userRole);

    @Delete("<script>" +
            "delete from user_role where user_id in " +
            "<foreach collection='userRoles' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids.userId}"+
            "</foreach> " +
            "</script>")
    void deleteByUserIds(List<UserRole> userRoles);

    @Insert("<script>" +
            "insert user_role (user_id, role_id) values " +
            "<foreach collection='userRoles' item='ids' index='index' separator=','>"+
            "(#{ids.userId}, #{ids.roleId})" +
            "</foreach> " +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveList(List<UserRole> userRoles);
}
