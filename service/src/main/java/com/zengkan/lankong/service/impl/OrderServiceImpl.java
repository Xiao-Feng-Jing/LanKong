package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.mappers.OrderDetailMapper;
import com.zengkan.lankong.mappers.OrderMasterMapper;
import com.zengkan.lankong.mappers.OrderStatusMapper;
import com.zengkan.lankong.mappers.WarehouseGoodsMapper;
import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.pojo.OrderStatus;
import com.zengkan.lankong.pojo.WarehouseGoods;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OrderServiceImpl implements OrderService {

    private final OrderMasterMapper orderMasterMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderStatusMapper orderStatusMapper;
    private final WarehouseGoodsMapper warehouseGoodsMapper;

    @Autowired
    public OrderServiceImpl(OrderMasterMapper orderMasterMapper, OrderDetailMapper orderDetailMapper, OrderStatusMapper orderStatusMapper, WarehouseGoodsMapper warehouseGoodsMapper){
        this.orderMasterMapper = orderMasterMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.warehouseGoodsMapper = warehouseGoodsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMaster createOrder(OrderMaster orderMaster) {
        String orderId = UUIDUtil.uuid();
        orderMaster.setOrderId(orderId);
        orderMaster.setCreateTime(new Date());
        orderMaster.setBuyerRate(0);
        orderMasterMapper.save(orderMaster);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(new Date());
        orderStatus.setStatus(1);
        orderStatusMapper.save(orderStatus);

        orderMaster.getOrderDetails().forEach(orderDetail -> {
            orderDetail.setOrderDetailId(UUIDUtil.uuid());
            orderDetail.setOrderId(orderId);
            orderDetail.setModifiedTime(new Date());
        });
        orderDetailMapper.saveList(orderMaster.getOrderDetails());
        orderMaster.getOrderDetails().forEach(orderDetail -> {
            warehouseGoodsMapper.reduceStock(orderDetail.getSkuId(),orderDetail.getProductCnt());
        });
        return orderMaster;
    }

    @Override
    public String generateUrl(String orderId) {
        return null;
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
            if (warehouseGoods.getCurrentCnt() - orderDetail.getProductCnt() < 0) {
                skuIds.add(orderDetail.getSkuId());
            }
        });
        return skuIds;
    }

    @Override
    public OrderMaster queryOrderById(String id) {
        return null;
    }
}
