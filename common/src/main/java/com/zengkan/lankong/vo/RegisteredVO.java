package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/11:05
 * @Description : 注册数据模型
 * @modified By :
 **/
@Data
@ApiModel("注册账号数据模型")
public class RegisteredVO implements Serializable {
    private static final long serialVersionUID = 6705036344904628407L;

    private String username;
    private String password;
    private String password2;
    private String name;
    private long cardType;
    private String cardNo;
    private long mobilePhone;
}
