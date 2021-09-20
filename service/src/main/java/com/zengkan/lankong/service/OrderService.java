package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.OrderMaster;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:48
 * @Description:
 **/
public interface OrderService {
    OrderMaster createOrder(OrderMaster orderMaster);

    String generateUrl(String orderId);

    List<String> queryStock(OrderMaster orderMaster);

    OrderMaster queryOrderById(String id);
}
