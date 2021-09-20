package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zengkan
 */
@Data
public class WarehouseGoods implements Serializable {

  private static final long serialVersionUID = 5496072132804436553L;
  private String skuId;
  private long currentCnt;
}
