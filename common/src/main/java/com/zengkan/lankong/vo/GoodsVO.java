package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.GoodsSpu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/28/23:18
 * @Description : 门户商品展示数据模型
 * @modified By :
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends GoodsSpu {

    private static final long serialVersionUID = 5626792709361926784L;
    private String images;
    private List<String> imageUrls;
    private String skuId;
    private String skuTitle;
    private Long price;
    private Long salePrice;
}
