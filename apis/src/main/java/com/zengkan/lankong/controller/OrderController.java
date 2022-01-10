package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("order")
@Api(tags = "订单接口")
public class OrderController {


    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     * @param orderMaster 订单对象
     * @return 成功时返回订单编号，否则返回库存不足的商品
     * */
    @PostMapping
    @RequiresRoles("user")
    @ApiOperation(value = "创建订单", notes = "根据订单数据模型创建订单, 权限需要：用户")
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
    @ApiOperation(value = "查询订单", notes = "根据订单主表ID查询, 权限需要：管理员或用户")
    @ApiImplicitParam(name = "id", value = "订单主表ID", required = true, paramType = "query", dataType = "String")
    public ResponseBean queryOrderById(@PathVariable("id") String id) {
        return new ResponseBean(CodeEnum.SUCCESS, orderService.queryOrderById(id));
    }

    /**
     * 分页查询所有的用户订单
     * */
    @GetMapping("page")
    @RequiresRoles("admin")
    @ApiOperation(value = "分页查询订单", notes = "分页查询所有的订单数据, 权限需要：管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "5", paramType = "query", dataType = "Integer")
    })
    public ResponseBean pageOrder(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("rows") Integer rows){
        return new ResponseBean(CodeEnum.SUCCESS, orderService.queryOrder(pageNum, rows));
    }

    /**
     * 分页查询当前用户订单
     * */
    @GetMapping("user/page")
    @RequiresRoles("user")
    @ApiOperation(value = "分页查询当前用户订单", notes = "分页查询当前用户所有的订单数据, 权限需要：用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", type = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "5", paramType = "query", type = "Integer"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "header", type = "String")
    })
    public ResponseBean pageOrderByUid(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("rows") Integer rows,
                                       HttpServletRequest request) {

        return new ResponseBean(CodeEnum.SUCCESS, orderService.queryOrderByUserId(pageNum, rows, request));
    }

    /**
     * 更新订单状态
     * */
    @PutMapping
    @RequiresRoles(logical = Logical.OR, value = {"user", "admin"})
    @ApiOperation(value = "更新订单状态", notes = "根据orderId更新订单状态，权限需要：用户或管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单编号", type = "String"),
            @ApiImplicitParam(name = "status", value = "订单状态：1未付款，" +
                    "2已付款未发货，" +
                    "3已发货未确认，" +
                    "4已确认未评价，" +
                    "5交易关闭，" +
                    "6交易成功，已评价", defaultValue = "1", type = "Integer")
    })
    public ResponseBean updateOrderStatus(@RequestParam("orderId") String orderId,
                                          @RequestParam("status") Integer status) {
        orderService.updateOrderStatus(orderId, status);
        return new ResponseBean(CodeEnum.SUCCESS, null);
    }
}
