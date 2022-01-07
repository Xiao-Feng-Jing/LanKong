package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengkan
 */
@Data
public class CustomerCollect implements Serializable {

  private static final long serialVersionUID = -3869845825750358626L;
  private long customerId;
  private String spuId;
  private LocalDateTime addTime;
}
