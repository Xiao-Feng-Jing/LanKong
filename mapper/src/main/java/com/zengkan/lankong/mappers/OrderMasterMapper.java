package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.OrderMaster;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:34
 * @Description:
 **/
@Repository
@Mapper
@CacheNamespace(blocking = true)
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
}
