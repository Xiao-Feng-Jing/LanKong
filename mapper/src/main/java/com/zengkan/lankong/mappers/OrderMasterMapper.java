package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.OrderMaster;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:34
 * @Description:
 **/
@Repository
@Mapper
public interface OrderMasterMapper {

    /**
     * 保存订单信息
     * @param orderMaster 订单对象
     * */
    @Insert("insert into order_master(order_id, total_pay, actual_pay, promotion_ids, payment_type, post_fee, " +
            "create_time, shipping_name, shipping_code, user_id, buyer_message, buyer_nick, buyer_rate, receiver_state, " +
            "receiver_city, receiver_district, receiver_address, receiver_mobile, receiver, invoice_type) " +
            "values(#{orderId}, #{totalPay}, #{actualPay}, #{promotionIds}, #{paymentType}, #{postFee}, " +
            "#{createTime}, #{shippingName}, #{shippingCode}, #{userId}, #{buyerMessage}, #{buyerNick}, #{buyerRate}, #{receiverState}, " +
            "#{receiverCity}, #{receiverDistrict}, #{receiverAddress}, #{receiverMobile}, #{receiver}, #{invoiceType})")
    void save(OrderMaster orderMaster);

    /**
     * 查询订单信息
     * @param id 订单id
     * @return 订单对象
     * */
    @Select("select order_id, total_pay, actual_pay, promotion_ids, payment_type, post_fee, create_time, shipping_name, " +
            "shipping_code, user_id, buyer_message, buyer_nick, buyer_rate, receiver_state, receiver_city, receiver_district, " +
            "receiver_address, receiver_mobile, receiver, invoice_type from order_master where order_id = #{id}")
    OrderMaster queryByOrderId(String id);

    /**
     * 查询订单信息
     * @return 订单对象集合
     * */
    @Select("select order_id, total_pay, actual_pay, promotion_ids, payment_type, post_fee, create_time, shipping_name, " +
            "shipping_code, user_id, buyer_message, buyer_nick, buyer_rate, receiver_state, receiver_city, receiver_district, " +
            "receiver_address, receiver_mobile, receiver, invoice_type from order_master")
    List<OrderMaster> queryOrder();

    /**
     * 根据用户id查询订单
     * @param userId 用户ID
     * @return 订单对象集合
     * */
    @Select("select order_id, total_pay, actual_pay, promotion_ids, payment_type, post_fee, create_time, shipping_name, " +
            "shipping_code, user_id, buyer_message, buyer_nick, buyer_rate, receiver_state, receiver_city, receiver_district, " +
            "receiver_address, receiver_mobile, receiver, invoice_type from order_master where user_id = #{userId}")
    List<OrderMaster> queryOrderByUserId(long userId);

    /**
     * 更新订单主表评论状态
     * */
    @Update("update order_master set buyer_rate = #{i} where order_id = #{orderId}")
    void updateBuyerRate(int i, String orderId);
}
