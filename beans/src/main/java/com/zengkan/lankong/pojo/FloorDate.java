package com.zengkan.lankong.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zengkan
 */
@Data
public class FloorDate {

  private long id;
  private long floorId;
  private long index;
  private long type;
  private String spuId;
  private String skuId;
  private long categeoryId;
  private Date createTime;
  private Date updateTime;

}
