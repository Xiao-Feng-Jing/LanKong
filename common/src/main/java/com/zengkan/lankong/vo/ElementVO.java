package com.zengkan.lankong.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/17:55
 * @Description : 门户展示数据
 * @modified By :
 **/
@Data
public class ElementVO {
    private int id;
    private String name;
    private String picString;
    private int index;
    private Date startDate;
    private Date endDate;
    private String spec;
}
