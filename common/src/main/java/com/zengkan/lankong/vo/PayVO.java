package com.zengkan.lankong.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/10/17:44
 * @Description :
 * @modified By :
 **/
@Data
public class PayVO implements Serializable {
    private static final long serialVersionUID = 5601909190088627863L;

    private String orderId;

    private String totalAmount;

    private String payName;

    private String requestFromUrl;

    private String returnUrl;
}
