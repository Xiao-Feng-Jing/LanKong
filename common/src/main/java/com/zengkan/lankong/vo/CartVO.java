package com.zengkan.lankong.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/28/21:50
 * @Description :
 * @modified By :
 **/
@Data
public class CartVO {

    // 用户id
    private Long userId;
    // 商品id
    private Long skuId;
    // 标题
    private String title;
    // 图片
    private String image;
    // 加入购物车时的价格
    private Long price;
    // 购买数量
    private Integer num;
    // 商品规格参数
    private String ownSpec;
    // 是否有效，逻辑删除用
    private Boolean enable;
}
