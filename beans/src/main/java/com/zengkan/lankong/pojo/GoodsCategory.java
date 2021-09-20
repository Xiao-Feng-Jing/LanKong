package com.zengkan.lankong.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zengkan
 */
@Data
public class GoodsCategory implements Serializable {

  private static final long serialVersionUID = 1294716227726790234L;
  private long categoryId;
  private String categoryName;
  private long parentId;
  private boolean isParent;
  private int categoryLevel;
  private int categoryStatus;
}
