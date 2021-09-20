package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/1:53
 * @Description:
 **/
@Repository
@CacheNamespace(blocking = true)
public interface UserMapper {

    /**
     * 普通用户登录
     * @param user 用户登录信息
     * @return 登录
     * */
    @Select("select id, username, password from user where username = #{username} and #{password};")
    User login(User user);

    /**
     * 查询用户是否存在
     * @param username 用户名
     * @return 用户信息
     * */
    @Select("select id, username, password from `user` where username = #{username};")
    User query(String username);
}
