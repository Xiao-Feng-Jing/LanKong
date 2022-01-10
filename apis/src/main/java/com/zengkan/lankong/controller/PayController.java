package com.zengkan.lankong.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.service.PayLogService;
import com.zengkan.lankong.utils.UUIDUtil;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.PayVO;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version : 1.0.0
 * @Date : 2021/09/15/16:18
 * @Description : 支付宝支付接口
 * @modified By :
 **/
@RestController
@RequestMapping("/pay")
@Api(tags = "支付接口")
public class PayController {

    private final PayLogService payLogService;

    @Autowired
    public PayController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    /**
     * 下单支付
     */
    @PostMapping
    @RequiresRoles(value = {
            "user"
    })
    @ApiOperation(value = "下单支付", notes = "角色：用户")
    @ApiImplicitParam(name = "payVO", value = "下单数据模型")

    public ResponseBean pay(@RequestBody PayVO payVO
                      ) {
        return new ResponseBean(CodeEnum.SUCCESS, payLogService.pay(payVO));
    }

    /**
     * 支付后异步回调的接口
     * 这个接口需要内网穿透，因为要别人访问你的本地网站
     * @param request
     * @return
     */
    @PostMapping(value = "notify")
    @ApiOperation(value = "支付回调接口")
    public String notifyAsync(HttpServletRequest request) {
        return payLogService.notifyAsync(request);
    }
}
