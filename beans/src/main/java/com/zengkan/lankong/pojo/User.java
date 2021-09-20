package com.zengkan.lankong.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/1:37
 * @Description:
 **/
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 7967668819462924968L;

    private long id;
    private String username;
    private String password;
    private String url;
    private boolean stats;
    private Date modifiedTime;
}
