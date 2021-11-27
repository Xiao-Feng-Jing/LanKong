package com.zengkan.lankong.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zengkan
 */
@Data
@ApiModel("商品spu数据模型")
public class GoodsSpu implements Serializable {

  private static final long serialVersionUID = -1023417011661039794L;
  @ApiModelProperty(value = "spuId",required = true)
  private String spuId;
  @ApiModelProperty(value = "商品名称",required = true)
  private String goodsName;
  @ApiModelProperty(value = "商品标题",required = true)
  private String title;
  private long cid1;
  private long cid2;
  private long cid3;
  private boolean saleable;
  private boolean isNew;
  private Date createDate;
  private Date modifiedTime;
}
