package com.zengkan.lankong.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/07/16:47
 * @Description : 修改密码数据模型
 * @modified By :
 **/
@Data
public class PasswordVO implements Serializable {
    private static final long serialVersionUID = -1502408254053934708L;

    private long id;
    private String username;
    private String oldPassword;
    private String newPassword;
}
