package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zengkan
 */
@Data
public class SpuDetail implements Serializable {

  private static final long serialVersionUID = -7564526495606926149L;
  private String spuId;
  private String description;
  private String specifications;
  private String specTemplate;
  private String packingList;
  private String afterService;
}
