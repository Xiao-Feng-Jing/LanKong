package com.zengkan.lankong.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengkan
 */
@Data
@ApiModel(value = "商品分类数据模型")
public class GoodsCategory implements Serializable {

    private static final long serialVersionUID = 1294716227726790234L;
    @ApiModelProperty("分类id")
    private long categoryId;
    @ApiModelProperty("分类名")
    private String categoryName;
    @ApiModelProperty(value = "父分类id")
    private long parentId;
    @ApiModelProperty(value = "是否是父分类", name = "parent")
    private boolean isParent;
    @ApiModelProperty("分类层级")
    private int categoryLevel;
}
