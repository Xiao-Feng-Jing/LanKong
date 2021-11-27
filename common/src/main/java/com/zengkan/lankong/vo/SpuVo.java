package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.pojo.GoodsSpu;
import com.zengkan.lankong.pojo.SpuDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/20:29
 * @Description:
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "商品数据模型")
public class SpuVo extends GoodsSpu {

    private static final long serialVersionUID = 8863789918388526100L;
    /**
     * 商品分类名称
     */
    private String cname;

    /**
     * 商品详情
     */
    private SpuDetail spuDetail;
    /**
     * sku列表
     */
    private List<GoodsSku> skus;
}
