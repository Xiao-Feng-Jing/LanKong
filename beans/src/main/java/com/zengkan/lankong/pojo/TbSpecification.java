package com.zengkan.lankong.pojo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zengkan
 */
@Data
public class TbSpecification implements Serializable {

  private static final long serialVersionUID = -3597509845275509308L;
  private long categoryId;
  private String specifications;

}
