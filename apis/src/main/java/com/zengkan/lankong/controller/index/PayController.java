package com.zengkan.lankong.controller.index;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.utils.UUIDUtil;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("portal/pay")
@Api(tags = "支付接口")
public class PayController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";

    private final OrderService orderService;

    @Autowired
    public PayController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * 生成支付宝
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseBean generateUrl(@PathVariable("id") String orderId) {
        return new ResponseBean(CodeEnum.SUCCESS, orderService.generateUrl(orderId));
    }

    @Value("${alipay.returnUrl}")
    private String returnUrl;

    /**
     * 支付宝测试
     * */
    /**
     * 下单支付
     * 当用户点击支付的时候，应该是要传参的，我这里就直接省略了，免得修改麻烦，大家应该可以自己实现
     */
    @SneakyThrows
    @PostMapping(value = "pay")
    public String pay() {
        AlipayTradePagePayResponse response = Factory.Payment
                //选择网页支付平台
                .Page()
                //调用支付方法:订单名称、订单号、金额、回调页面
                .pay("测试商品", UUIDUtil.uuid(), "5","");
        //这里可以加入业务代码：比如生成订单等等。。
        return response.body;
    }

    /**
     * 支付后异步回调的接口
     * 这个接口需要内网穿透，因为要别人访问你的本地网站
     * @param request
     * @return
     */
    @PostMapping(value = "notify")
    public String notifyAsync(HttpServletRequest request) {
        String result = "fail";
        Map<String, String> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        //验签
        try {
            if (Factory.Payment.Common().verifyNotify(map)) {
                //验证用户的支付结果
                String trade_status = request.getParameter("trade_status");
                if ("TRADE_SUCCESS".equals(trade_status)) {
                    //这里可以更新订单的状态等等。
                    result += "success";
                }
            } else {
                return result;
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }
}
