package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.CustomerInf;
import com.zengkan.lankong.pojo.User;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/23:04
 * @Description : 用户展示数据模型
 * @modified By :
 **/
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 3734340583397313044L;

    private CustomerInf customerInf;

    private User user;
}
