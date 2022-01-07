package com.zengkan.lankong.pojo;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengkan
 */
@Data
public class CategoryRecommends {

  private long id;
  private long categoryId;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

}
