package com.zengkan.lankong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.enums.OrderEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.OrderDetailMapper;
import com.zengkan.lankong.mappers.OrderMasterMapper;
import com.zengkan.lankong.mappers.OrderStatusMapper;
import com.zengkan.lankong.mappers.WarehouseGoodsMapper;
import com.zengkan.lankong.pojo.*;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.utils.UUIDUtil;
import com.zengkan.lankong.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:49
 * @Description:
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMasterMapper orderMasterMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderStatusMapper orderStatusMapper;
    private final WarehouseGoodsMapper warehouseGoodsMapper;
    private final RedisUtil redisUtil;

    @Autowired
    public OrderServiceImpl(OrderMasterMapper orderMasterMapper, OrderDetailMapper orderDetailMapper, OrderStatusMapper orderStatusMapper, WarehouseGoodsMapper warehouseGoodsMapper, RedisUtil redisUtil){
        this.orderMasterMapper = orderMasterMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.warehouseGoodsMapper = warehouseGoodsMapper;
        this.redisUtil = redisUtil;
    }

    /**
     * 创建订单
     * @param orderMaster 订单数据对象
     * @return 订单对象
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMaster createOrder(OrderMaster orderMaster) {
        String orderId = UUIDUtil.uuid();
        orderMaster.setOrderId(orderId);
        orderMaster.setCreateTime(LocalDateTime.now());
        orderMaster.setBuyerRate(0);
        orderMasterMapper.save(orderMaster);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(orderMaster.getCreateTime());
        orderStatus.setStatus(OrderEnum.UNPAID_NOT_SHIPPED.getCode());
        orderStatusMapper.save(orderStatus);

        orderMaster.getOrderDetails().forEach(orderDetail -> {
            orderDetail.setOrderDetailId(UUIDUtil.uuid());
            orderDetail.setOrderId(orderId);
            orderDetail.setModifiedTime(orderMaster.getCreateTime());
        });
        orderDetailMapper.saveList(orderMaster.getOrderDetails());
        orderMaster.getOrderDetails().forEach(orderDetail -> warehouseGoodsMapper.reduceStock(orderDetail.getSkuId(),orderDetail.getProductCnt()));
        return orderMaster;
    }

    /**
     * 查询商品数量是否足够
     * @param orderMaster 订单对象
     * @return 库存不足的商品集合
     * */
    @Override
    public List<String> queryStock(OrderMaster orderMaster) {
        List<String> skuIds = new ArrayList<>();
        orderMaster.getOrderDetails().forEach(orderDetail -> {
            WarehouseGoods warehouseGoods = warehouseGoodsMapper.queryBySkuId(orderDetail.getSkuId());
            if (warehouseGoods == null) {
                throw new MyException(ExceptionEnum.SKU_NOT_FOUND);
            }
            if (warehouseGoods.getCurrentCnt() - orderDetail.getProductCnt() < 0) {
                skuIds.add(orderDetail.getSkuId());
            }
        });
        return skuIds;
    }

    /**
     * 根据订单id查询订单数据
     * @param id 订单id
     * @return 订单对象
     * */
    @Override
    public OrderMaster queryOrderById(String id) {
        OrderMaster orderMaster = orderMasterMapper.queryByOrderId(id);
        if (orderMaster == null) {
            throw new MyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(id);
        orderMaster.setOrderDetails(orderDetailList);
        OrderStatus orderStatus = orderStatusMapper.queryByOrderId(id);
        orderMaster.setStatus(orderStatus.getStatus());
        return orderMaster;
    }

    /**
     * 分页查询所有的订单数据
     * @param pageNum 当前页
     * @param rows 每页大小，最大100
     * @return 订单数据集合
     * */
    @Override
    public PageResult<OrderMaster> queryOrder(Integer pageNum, Integer rows) {
        PageHelper.startPage(pageNum, Math.min(rows, 100));
        Page<OrderMaster> pageInfo = (Page<OrderMaster>) orderMasterMapper.queryOrder();
        return listOrder(pageInfo);
    }

    /**
     * 分页查询当前用户订单
     * @param pageNum 当前页
     * @param rows 每页大小，最大100
     * @param request http请求头
     * @return 订单数据集合
     * */
    @Override
    public PageResult<OrderMaster> queryOrderByUserId(Integer pageNum, Integer rows, HttpServletRequest request) {
        PageHelper.startPage(pageNum, Math.min(rows, 100));
        String token = request.getHeader("Authorization");
        User user = (User) redisUtil.getString(token);
        Page<OrderMaster> pageInfo = (Page<OrderMaster>) orderMasterMapper.queryOrderByUserId(user.getId());
        return listOrder(pageInfo);
    }

    /**
     * 根据订单ID跟新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(String orderId, Integer status) {
        OrderStatus orderStatus = orderStatusMapper.queryByOrderId(orderId);
        if (orderStatus == null) {
            log.error("订单: {}不存在", orderId);
            throw new MyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 根据状态更新相应时间
        int count;
        switch (status) {
            case 2 :
                // 1.付款时间
                count = orderStatusMapper.updatePayTime(orderId, status, new Date());
                break;
            case 3 :
                // 2.发货时间
                count = orderStatusMapper.updateConsignTime(orderId, status, new Date());
                break;
            case 4 :
                // 3.确认收货，订单结束
                count = orderStatusMapper.updateEndTime(orderId, status, new Date());
                break;
            case 5 :
                // 4.交易失败，订单关闭
                count = orderStatusMapper.updateCloseTime(orderId, status, new Date());
                break;
            case 6 :
                // 5.评论时间
                count = orderStatusMapper.updateCommentTime(orderId, status, new Date());
                orderMasterMapper.updateBuyerRate(1, orderId);
                break;
            default :
                log.error("订单状态不存在");
                throw new MyException(ExceptionEnum.INVALID_PARAM);
        }
        return count == 1;
    }

    /**
     * 根据需要返回订单信息
     * */
    private PageResult<OrderMaster> listOrder(Page<OrderMaster> pageInfo) {
        if (pageInfo.isEmpty()) {
            throw new MyException(ExceptionEnum.NOT_FOUND_ORDER);
        }
        List<OrderMaster> list = pageInfo.getResult();
        list.forEach(orderMaster -> {
            List<OrderDetail> orderDetail = orderDetailMapper.queryByOrderId(orderMaster.getOrderId());
            orderMaster.setOrderDetails(orderDetail);
            OrderStatus orderStatus = orderStatusMapper.queryByOrderId(orderMaster.getOrderId());
            orderMaster.setStatus(orderStatus.getStatus());
        });
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(),
                pageInfo.getPageNum(), pageInfo.getPageSize(), list);
    }
}
