package com.zengkan.lankong.controller.index;

import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.service.PayLogService;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:46
 * @Description:
 **/
@RestController
@RequestMapping("portal/order")
@Api(tags = "订单接口")
public class OrderController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";

    private final OrderService orderService;

    private final PayLogService payLogService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    public OrderController(OrderService orderService, PayLogService payLogService) {
        this.orderService = orderService;
        this.payLogService = payLogService;
    }

    /**
     * 创建订单
     * @param orderMaster 订单对象
     * @return 订单编号
     * */
    @PostMapping
    @RequiresRoles("user")
    public ResponseBean createOrder(@RequestBody @Valid OrderMaster orderMaster){
        List<String> skuIds = orderService.queryStock(orderMaster);
        if (skuIds != null && !skuIds.isEmpty()) {
            orderMaster = orderService.createOrder(orderMaster);
            return new ResponseBean(200,SUCCESS_MESSAGE,orderMaster);
        }

        return new ResponseBean(400,"以下商品库存不足", skuIds);
    }

    /**
     * 查询订单
     * */
    @GetMapping("{id}")
    @RequiresRoles(logical = Logical.OR,value = {
        "user", "admin"
    })
    public ResponseBean queryOrderById(@PathVariable("id") String id) {
        OrderMaster orderMaster = orderService.queryOrderById(id);
        if (orderMaster == null) {
            return new ResponseBean(404,"订单不存在",null);
        }
        return new ResponseBean(200,SUCCESS_MESSAGE,orderMaster);
    }

    /**
     * 分页查询所有的用户订单
     * */

    /**
     * 分页查询当前已经登录的用户订单
     * */
}
