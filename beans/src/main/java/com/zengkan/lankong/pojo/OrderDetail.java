package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zengkan
 * @Description: 订单详情信息实体类
 */
@Data
public class OrderDetail implements Serializable {

  private static final long serialVersionUID = 4868132021052202347L;
  /**
   * 订单详情ID
   * */
  private String orderDetailId;
  /**
   * 订单id
   * */
  private String orderId;
  /**
   * 商品ID
   * */
  private String skuId;
  /**
   *商品名称
   * */
  private String title;
  /**
   * 商品动态属性键值集
   * */
  private String ownSpec;
  /**
   * 商品购买数量
   * */
  private long productCnt;
  /**
   * 商品单价
   * */
  private double productPrice;
  /**
   * 图片
   * */
  private String image;
  /**
   * 最后修改时间
   * */
  private Date modifiedTime;

}
