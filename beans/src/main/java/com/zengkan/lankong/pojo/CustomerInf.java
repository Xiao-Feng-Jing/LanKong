package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengkan
 */
@Data
public class CustomerInf implements Serializable {

  private static final long serialVersionUID = 3471542369088587062L;
  private long customerInfId;
  private long customerId;
  private String customerName;
  private long identityCardType;
  private String identityCardNo;
  private long mobilePhone;
  private String customerEmail;
  private String gender;
  private LocalDateTime registerTime;
  private LocalDateTime modifiedTime;

}
