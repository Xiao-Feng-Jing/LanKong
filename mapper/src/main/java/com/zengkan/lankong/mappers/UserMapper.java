package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.vo.PasswordVO;
import com.zengkan.lankong.vo.UserVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/1:53
 * @Description:
 **/
@Repository
@Mapper
public interface UserMapper {

    /**
     * 活跃用户登录
     * @param user 用户登录信息
     * @return 登录
     * */
    @Select("select id, username, password from user where username = #{username} and #{password} and suer_stats = true;")
    User login(User user);

    /**
     * 查询用户是否存在或活跃
     * @param username 用户名
     * @return 用户信息
     * */
    @Select("select id, username, password, url from `user` where username = #{username}")
    User query(String username);

    /**
     * 注册用户
     * @param user 用户实体类
     * @return 用户信息
     * */
    @Insert("insert `user`(username, password, header_url, modified_time) values(#{username}, #{password}, #{url}, #{modifiedTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    User registered(User user);

    /**
     * 更新基础资料
     * */
    @Update("<script>" +
            "update `user` set " +
            "<if test = 'username != null and username != \"\" '> username = #{username} </if>" +
            "<if test = 'url != null and url != \"\"'> header_url = #{url} </if>" +
            "modified_time = #{modifiedTime} where id = #{id};" +
            "</script>")
    int update(User user);

    /**
     * 静默或启用用户
     * */
    @Update("<script>" +
            "update `user` set user_stats = !user_stats where id in" +
            "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach> " +
            "</script>")
    int updateById(List<Long> ids);

     /**
      * 查询用户
      * */
     @Select("<script>SELECT `user`.id as id, `user`.username as username, " +
             "`user`.header_url as url, `user`.user_stats as status, role.role as role " +
             " FROM user_role " +
             "inner join `user` on user_role.user_id = `user`.id " +
             "inner join role on role.id = user_role.role_id " +
             " where user_role.user_id in " +
             "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
             "#{ids}"+
             "</foreach> " +
             "<if test = 'status !=null and status !=\"\"'> and `user`.user_stats = #{status} </if>"+
             "</script>")
    List<UserVO> selectByIds(Set<Object> ids, Boolean status);

     /**
      * 查询用户
      * */
     @Select("<script>SELECT `user`.id as id, `user`.username as username, " +
             "`user`.header_url as url, `user`.user_stats as status, role.role as role " +
             " FROM user_role " +
             "inner join `user` on user_role.user_id = `user`.id " +
             "inner join role on role.id = user_role.role_id " +
             " <where> " +
             "<if test = 'status !=null and status !=\"\"'> and `user`.user_stats = #{status} </if>"+
             "</where>" +
             "</script>")
     List<UserVO> select(Boolean status);

    /**
     * 查询用户
     * */
    @Select("<script>SELECT `user`.id as id, `user`.username as username, " +
            "`user`.header_url as url, `user`.status as status, role.role as role " +
            " FROM user_role " +
            "inner join `user` on user_role.user_id = `user`.id " +
            "inner join role on role.id = user_role.role_id " +
            " where user_role.user_id not exists " +
            "<foreach collection='ids' item='ids' index='index' open='(' separator=',' close=')'>"+
            "#{ids}"+
            "</foreach> " +
            "<if test = 'status !=null and status !=\"\"'> and `user`.user_stats = #{status} </if>"+
            "</script>")
    List<UserVO> selectNotByIds(Set<Object> ids, Boolean status);

    /**
     * 获取用户信息
     * */
    @Select("select id, username, password, url, modified_time from `user` where id = #{id}")
    User selectbyId(long id);

    /**
     * 修改密码
     * */
    @Update("update `user` set password = #{newPassword} where id = #{id}")
    int updatePassword(PasswordVO passwordVO);
}
