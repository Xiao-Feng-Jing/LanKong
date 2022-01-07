package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.OrderStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:34
 * @Description:
 **/
@Repository
@Mapper
public interface OrderStatusMapper {

    /**
     * 保存订单状态
     * @param orderStatus 订单状态对象
     * */
    @Insert("insert into order_status(order_id, status, create_time) " +
            "values(#{orderId}, #{status}, #{createTime})")
    void save(OrderStatus orderStatus);

    /**
     * 查询订单状态
     * @param id 订单ID
     * */
    @Select("select order_id, status, create_time, payment_time, consign_time, " +
            "end_time, close_time, comment_time from order_status where order_id = #{id}")
    OrderStatus queryByOrderId(String id);

    /**
     * 更新付款时间
     * */
    @Update("update order_status set payment_time = #{date}, status = #{status} where order_id = #{orderId}")
    int updatePayTime(String orderId, Integer status, Date date);

    /**
     * 更新发货时间
     * */
    @Update("update order_status set consign_time = #{date}, status = #{status} where order_id = #{orderId}")
    int updateConsignTime(String orderId, Integer status, Date date);

    /**
     * 更新收货时间
     * */
    @Update("update order_status set end_time = #{date}, status = #{status} where order_id = #{orderId}")
    int updateEndTime(String orderId, Integer status, Date date);

    /**
     * 更新评论时间
     * */
    @Update("update order_status set comment_time = #{date}, status = #{status} where order_id = #{orderId}")
    int updateCommentTime(String orderId, Integer status, Date date);

    /**
     * 更新订单关闭时间
     * */
    @Update("update order_status set close_time = #{date}, status = #{status} where order_id = #{orderId}")
    int updateCloseTime(String orderId, Integer status, Date date);
}
