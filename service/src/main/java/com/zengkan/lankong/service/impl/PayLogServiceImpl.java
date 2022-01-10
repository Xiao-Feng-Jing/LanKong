package com.zengkan.lankong.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.enums.OrderEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.service.OrderService;
import com.zengkan.lankong.service.PayLogService;
import com.zengkan.lankong.utils.UUIDUtil;
import com.zengkan.lankong.vo.PayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:52
 * @Description:
 **/
@Service
public class PayLogServiceImpl implements PayLogService {

    private final OrderService orderService;


    @Autowired
    public PayLogServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public String pay(PayVO payVO) {
        try {
            AlipayTradePagePayResponse response = Factory.Payment
                    .Page()
                    .optional("request_from_url", "http://127.0.0.1:8848/%E6%9B%BE%E4%BE%83%E7%9A%84JavaWeb%E4%BD%9C%E4%B8%9A/return.html")
                    .pay(payVO.getPayName(), payVO.getOrderId(), payVO.getTotalAmount(), "http://127.0.0.1:8848/%E6%9B%BE%E4%BE%83%E7%9A%84JavaWeb%E4%BD%9C%E4%B8%9A/return.html");
            return response.body;
        } catch (Exception e) {
            throw new MyException(ExceptionEnum.ALIPAY_ORDER_FAIL);
        }
    }

    @Override
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
                String tradeStatus = request.getParameter("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    //这里可以更新订单的状态等等。
                    String orderId = map.get("out_trade_no");
                    orderService.updateOrderStatus(orderId, OrderEnum.PAID_NOT_SHIPPED.getCode());
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
