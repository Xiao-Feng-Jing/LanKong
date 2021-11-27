package com.zengkan.lankong.pojo;


import lombok.Data;

import java.util.Date;

/**
 * @author zengkan
 */
@Data
public class IndexImg {

  private String id;
  private String url;
  private String bgColor;
  private String spuId;
  private String skuId;
  private long categoryId;
  private long indexType;
  private long seq;
  private long status;
  private Date createTime;
  private Date updateTime;

}
