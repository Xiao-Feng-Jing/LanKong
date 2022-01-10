package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.OrderMaster;
import com.zengkan.lankong.vo.PayVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:51
 * @Description:
 **/
public interface PayLogService {
    String pay(PayVO payVO);

    String notifyAsync(HttpServletRequest request);
}
