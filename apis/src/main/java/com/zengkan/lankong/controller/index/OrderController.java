package com.zengkan.lankong.controller.index;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.service.PayLogService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
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


    private final OrderService orderService;

    private final PayLogService payLogService;
    @Autowired
    public OrderController(OrderService orderService, PayLogService payLogService) {
        this.orderService = orderService;
        this.payLogService = payLogService;
    }

    /**
     * 创建订单
     * @param orderMaster 订单对象
     * @return 成功时返回订单编号，否则返回库存不足的商品
     * */
    @PostMapping
    @RequiresRoles("user")
    public ResponseBean createOrder(@RequestBody @Valid OrderMaster orderMaster){
        List<String> skuIds = orderService.queryStock(orderMaster);
        if (skuIds == null || skuIds.isEmpty()) {
            orderMaster = orderService.createOrder(orderMaster);
            return new ResponseBean(CodeEnum.SAVE_SUCCESS, orderMaster);
        }
        return new ResponseBean(ExceptionEnum.STOCK_NOT_ENOUGH, skuIds);
    }

    /**
     * 查询订单
     * */
    @GetMapping("{id}")
    @RequiresRoles(logical = Logical.OR,value = {"user", "admin"})
    public ResponseBean queryOrderById(@PathVariable("id") String id) {
        return new ResponseBean(CodeEnum.SUCCESS, orderService.queryOrderById(id));
    }

    /**
     * 分页查询所有的用户订单
     * */
    @PostMapping("page")
    public ResponseBean pageOrder(){
        return new ResponseBean();
    }

    /**
     * 分页查询当前用户订单
     * */
    @PostMapping("page/{uid}")
    public ResponseBean pageOrderByUid() {
        return new ResponseBean();
    }
}
