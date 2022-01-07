package com.zengkan.lankong.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengkan.lankong.config.OssConfig;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.service.OssService;
import com.zengkan.lankong.vo.OssCallbackParam;
import com.zengkan.lankong.vo.OssCallbackResult;
import com.zengkan.lankong.vo.OssPolicyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/17:34
 * @Description:
 **/
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private OSS ossClient;

    /**
     * 签名生成
     * @return OssPolicyResult
     */
    @Override
    public OssPolicyResult policy() throws MyException {
        OssPolicyResult ossPolicyResult = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = ossConfig.getALIYUN_OSS_DIR_PREFIX_ONE() + sdf.format(new Date());
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ossConfig.getALIYUN_OSS_EXPIRE() * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        int maxSize = ossConfig.getALIYUN_OSS_MAX_SIZE() * 1024 * 1024;
        // 回调
        OssCallbackParam ossCallbackParam = new OssCallbackParam();
        ossCallbackParam.setCallbackUrl(ossConfig.getALIYUN_OSS_CALLBACK());
        ossCallbackParam.setCallbackBody("{\"fileSize\":${fileSize},\"fileUrl\":${fileUrl},\"webUrl\":${webUrl},\"fileSuffix\":${fileSuffix},\"fileBucket\":${fileBucket},\"oldFileName\":${oldFileName},\"folder\":${folder}");
        ossCallbackParam.setCallbackBodyType(Callback.CalbackBodyType.JSON);
        // 提交节点
        String action = "https://"+ ossConfig.getALIYUN_OSS_ENDPOINT();

        try {
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String  postPolicy = ossClient.generatePostPolicy(expiration,policyConditions);
            byte[] binaryData;
            binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            ObjectMapper mapper=new ObjectMapper();
            String callbackData = BinaryUtil.toBase64String(mapper.writeValueAsString(ossCallbackParam).getBytes(StandardCharsets.UTF_8));
            // 返回结果
            ossPolicyResult.setAccessKeyId(ossConfig.getALIYUN_OSS_ACCESSKEYID());
            ossPolicyResult.setPolicy(policy);
            ossPolicyResult.setSignature(signature);
            ossPolicyResult.setDir(dir);
            ossPolicyResult.setExpire(expiration);
            ossPolicyResult.setCallback(callbackData);
            ossPolicyResult.setHost(action);
        } catch (JsonProcessingException e) {
            throw new MyException(ExceptionEnum.SIGNATURE_FILE_ERROR);
        }
        return ossPolicyResult;
    }

    @Override
    public OssCallbackResult callback( HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = new OssCallbackResult();
        ossCallbackResult.setWebUrl(request.getAttribute("fileSize").toString());
        ossCallbackResult.setFileUrl(request.getAttribute("fileUrl").toString());
        ossCallbackResult.setFileSize(request.getAttribute("fileSize").toString());
        ossCallbackResult.setFileBucket(request.getAttribute("fileBucket").toString());
        ossCallbackResult.setFileSuffix(request.getAttribute("fileSuffix").toString());
        ossCallbackResult.setFolder(request.getAttribute("folder").toString());
        ossCallbackResult.setOldFileName(request.getAttribute("oldFileName").toString());
        return ossCallbackResult;
    }

}
