package com.zengkan.lankong.pojo;



import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zengkan
 * @Description: 订单主体实体类
 */
@Data
public class OrderMaster implements Serializable {

  private static final long serialVersionUID = -1118692367752965687L;

  /**
   * 订单ID
   * */
  private String orderId;
  /**
   * 总金额，单位元
   * */
  @NotNull
  private double totalPay;
  /**
   * 实付金额，单位元
   * */
  @NotNull
  private double actualPay;
  /**
   * 促销id集合
   * */
  private String promotionIds;
  /**
   * 支付类型，1、在线支付，2、货到付款
   */
  @NotNull
  private long paymentType;
  /**
   * 邮费，单位元
   * */
  private double postFee;
  /**
   * 订单创建时间
   * */
  private Date createTime;
  /**
   * 物流名称
   * */
  private String shippingName;
  /**
   * 物流单号
   * */
  private String shippingCode;
  /**
   * 用户id
   * */
  private long userId;
  /**
   * 卖家留言
   * */
  private String buyerMessage;
  /**
   * 买家昵称
   * */
  private String buyerNick;
  /**
   * 买家是否已经评价
   * */
  private long buyerRate;
  /**
   * 收货地址省
   * */
  private String receiverState;
  /**
   * 收货地址市
   * */
  private String receiverCity;
  /**
   * 收货地址区
   * */
  private String receiverDistrict;
  /**
   * 收货地址，详细地址
   * */
  private String receiverAddress;
  /**
   * 收货人手机
   * */
  private String receiverMobile;
  /**
   * 收货人
   * */
  private String receiver;
  /**
   * 发票类型(0无发票1普通发票，2电子发票，3增值税发票)
   * */
  private long invoiceType;

  /**
   * 订单详情id集合
   * */
  private transient List<OrderDetail> orderDetails;

  /**
   * 订单状态
   * */
  private transient Integer status;
}
