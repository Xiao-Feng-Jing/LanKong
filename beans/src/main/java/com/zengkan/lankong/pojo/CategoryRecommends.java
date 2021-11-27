package com.zengkan.lankong.pojo;


import lombok.Data;

import java.util.Date;

/**
 * @author zengkan
 */
@Data
public class CategoryRecommends {

  private long id;
  private long categoryId;
  private Date createTime;
  private Date updateTime;

}
