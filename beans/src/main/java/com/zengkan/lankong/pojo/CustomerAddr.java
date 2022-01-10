package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengkan
 * @Description: 收货地址实体
 */
@Data
public class CustomerAddr implements Serializable {

  private static final long serialVersionUID = 5697064959506464097L;
  /**
   * 收货地址ID
   * */
  private long customerAddrId;
  /**
   * 用户id
   */
  private long customerId;
  /**
   * 收货人
   * */
  private String name;
  /**
   * 收货电话
   * */
  private String phone;
  /**
   * 省
   * */
  private long province;
  /**
   * 市
   * */
  private long city;
  /**
   * 地区
   * */
  private long district;
  /**
   * 具体地址
   * */
  private String address;
  /**
   * 是否是默认地址
   * */
  private boolean isDefault;
  /**
   * 最后修改时间
   * */
  private LocalDateTime modifiedTime;

}
