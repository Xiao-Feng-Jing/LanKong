package com.zengkan.lankong.pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/15:40
 * @Description:
 **/
@Data
public class UserRole {

    private long id;
    private String username;
    private String password;
    private String role;
}
