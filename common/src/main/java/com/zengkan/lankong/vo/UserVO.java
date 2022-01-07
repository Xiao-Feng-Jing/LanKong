package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/15:46
 * @Description : 用户管理数据模型
 * @modified By :
 **/
@Data
@ApiModel("用户管理数据模型")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 460934633329309811L;

    private long id;
    private String username;
    private String url;
    private Boolean status;
    private String role;
    private Boolean isOnline;
}
