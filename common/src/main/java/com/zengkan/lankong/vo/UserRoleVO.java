package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.UserRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/19:40
 * @Description :
 * @modified By :
 **/
@Data
public class UserRoleVO implements Serializable {
    private static final long serialVersionUID = -1647667981594179898L;

    private List<UserRole> userRoleList;
}
