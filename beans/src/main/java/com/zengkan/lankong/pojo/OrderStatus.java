package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengkan
 * @Description: 订单状态实体
 */
@Data
public class OrderStatus implements Serializable {



  private static final long serialVersionUID = -6846969348590108015L;
  /**
   * 订单ID
   * */
  private String orderId;
  /**
   * 状态
   * 初始阶段：1、未付款、未发货；初始化所有数据，更改库存
   * 付款阶段：2、已付款、未发货；更改付款时间
   * 发货阶段：3、已发货，未确认；更改发货时间、物流名称、物流单号
   * 成功阶段：4、已确认，未评价；更改交易结束时间
   * 关闭阶段：5、关闭； 更改更新时间，交易关闭时间，更改库存。
   * 评价阶段：6、已评价
   */
  private long status;
  /**
   * 创建时间
   */
  private LocalDateTime createTime;
  /**
   * 付款时间
   */
  private LocalDateTime paymentTime;
  /**
   *  发货时间
   */
  private LocalDateTime consignTime;
  /**
   * 交易结束时间
   */
  private LocalDateTime endTime;
  /**
   * 交易关闭时间
   */
  private LocalDateTime closeTime;
  /**
   * 评价时间
   */
  private LocalDateTime commentTime;

}
