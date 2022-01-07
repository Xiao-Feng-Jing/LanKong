package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.vo.PageResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:48
 * @Description:
 **/
public interface OrderService {
    /**
     * 创建订单
     * @param orderMaster 订单数据对象
     * @return 订单对象
     * */
    OrderMaster createOrder(OrderMaster orderMaster);

    /**
     * 查询商品数量是否足够
     * @param orderMaster 订单对象
     * @return 库存不足的商品集合
     * */
    List<String> queryStock(OrderMaster orderMaster);

    /**
     * 根据订单id查询订单数据
     * @param id 订单id
     * @return 订单对象
     * */
    OrderMaster queryOrderById(String id);

    /**
     * 分页查询所有的订单数据
     * @param pageNum 当前页
     * @param rows 每页大小，最大100
     * @return 订单数据集合
     * */
    PageResult<OrderMaster> queryOrder(Integer pageNum, Integer rows);

    /**
     * 分页查询当前用户订单
     * @param pageNum 当前页
     * @param rows 每页大小，最大100
     * @param request http请求头
     * @return 订单数据集合
     * */
    PageResult<OrderMaster> queryOrderByUserId(Integer pageNum, Integer rows, HttpServletRequest request);

    /**
     * 根据订单ID跟新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     * */
    boolean updateOrderStatus(String orderId, Integer status);
}
