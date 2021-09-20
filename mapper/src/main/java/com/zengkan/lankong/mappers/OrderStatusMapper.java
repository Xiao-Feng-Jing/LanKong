package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.OrderStatus;
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
public interface OrderStatusMapper {

    /**
     * 保存订单状态
     * @param orderStatus 订单状态对象
     * */
    @Insert("insert into order_status(order_id, status, create_time) " +
            "values(#{orderId}, #{status}, #{createTime})")
    void save(OrderStatus orderStatus);
}
