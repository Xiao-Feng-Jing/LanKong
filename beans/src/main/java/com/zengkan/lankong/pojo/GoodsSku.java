package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengkan
 */
@Data
public class GoodsSku implements Serializable {

  private static final long serialVersionUID = -4085282177076263971L;

  private String skuId;
  private String spuId;
  private String title;
  private String images;
  private long price;
  private long salePrice;
  private boolean enable;
  private String indexes;
  private String ownSpec;
  private LocalDateTime createTime;
  private LocalDateTime lastUpdateTime;
  //非数据库信息
  private long stock;
}
