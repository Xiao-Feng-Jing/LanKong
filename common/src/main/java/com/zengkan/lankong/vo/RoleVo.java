package com.zengkan.lankong.vo;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/18:03
 * @Description:
 **/
@Getter
public enum RoleVo {
    /**
     * {@code 1 普通用户}
     * */
    USER_ROLE(1,"user"),
    /**
     * {@code 2 管理员}
     * */
    AMIN_ROLE(2,"admin");

    private final int code;
    
    private final String role;

    RoleVo(int code, String role) {
        this.code = code;
        this.role = role;
    }
}
