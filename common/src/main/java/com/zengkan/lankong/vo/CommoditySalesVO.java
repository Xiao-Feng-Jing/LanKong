package com.zengkan.lankong.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/15/20:54
 * @Description :
 * @modified By :
 **/
@Data
public class CommoditySalesVO implements Serializable {
    private static final long serialVersionUID = 4952216903974932643L;

    private String spuId;
    private Long sales;
}
