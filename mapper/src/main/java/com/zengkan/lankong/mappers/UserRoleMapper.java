package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.UserRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/02/15:40
 * @Description:
 **/

@Repository
public interface UserRoleMapper {


    @Select("select role as role FROM role left join user_role as a on a.role_id = role.id WHERE a.user_Id = #{id};")
    UserRole queryUserRole(long id);
}
