package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zengkan
 */
@Data
public class GoodsSpu implements Serializable {

  private static final long serialVersionUID = -1023417011661039794L;
  private String spuId;
  private String goodsName;
  private String title;
  private long cid1;
  private long cid2;
  private long cid3;
  private boolean saleable;
  private boolean isNew;
  private Date createDate;
  private Date modifiedTime;
}
