package com.zengkan.lankong.mappers;


import com.zengkan.lankong.pojo.OrderDetail;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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
@CacheNamespace(blocking = true)
public interface OrderDetailMapper {

    @Insert("<script>insert into order_detail(order_detail_id, order_id, sku_id, title, own_spec, " +
            "product_cnt,product_price,image,modified_time) values " +
            "<foreach collection='list' item='item' index='index' separator=','> " +
            "(#{item.orderDetailId}, #{item.orderId}, #{item.skuId}, #{item.title}, #{item.ownSpec}, " +
            "#{item.productCnt}, #{item.productPrice}, #{item.image}, #{item.modifiedTime})" +
            "</foreach>" +
            "</script>")
    void saveList(List<OrderDetail> orderDetails);
}
