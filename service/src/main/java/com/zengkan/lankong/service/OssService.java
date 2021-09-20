package com.zengkan.lankong.service;


import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.vo.OssCallbackResult;
import com.zengkan.lankong.vo.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/17:34
 * @Description:
 **/
public interface OssService {
    /**
     * oss 上传生成策略
     * @return
     */
    OssPolicyResult policy() throws MyException;

    /**
     * oss上传成功回调
     * @param request
     * @return
     */
    OssCallbackResult callback(HttpServletRequest request);


}
