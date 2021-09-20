package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
  private BigDecimal price;
  private BigDecimal salePrice;
  private boolean enable;
  private String indexes;
  private String ownSpec;
  private Date createTime;
  private Date lastUpdateTime;
  //非数据库信息
  private long stock;

}
