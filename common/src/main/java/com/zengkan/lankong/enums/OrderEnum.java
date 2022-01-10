package com.zengkan.lankong.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/10/17:28
 * @Description : 订单状态枚举
 * @modified By :
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderEnum {

    /**
     * 状态
     * 初始阶段：1、未付款、未发货；初始化所有数据，更改库存
     * 付款阶段：2、已付款、未发货；更改付款时间
     * 发货阶段：3、已发货，未确认；更改发货时间、物流名称、物流单号
     * 成功阶段：4、已确认，未评价；更改交易结束时间
     * 关闭阶段：5、关闭； 更改更新时间，交易关闭时间，更改库存。
     * 评价阶段：6、已评价
     */
    UNPAID_NOT_SHIPPED(1, "未付款、未发货;"),
    PAID_NOT_SHIPPED(2, "已付款、未发货"),
    SHIPPED_NOT_CONFIRMED(3, "已发货，未确认"),
    CONFIRMED_NOT_RATED(4, "已确认，未评价"),
    CLOSED(5, "关闭"),
    EVALUATED(6, "已评价");
    private final int code;
    private final String value;

    OrderEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "OrderEnum{" +
                "code=" + code +
                ", value='" + value + '\'' +
                '}';
    }
}
