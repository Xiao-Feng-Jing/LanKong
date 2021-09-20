package com.zengkan.lankong.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.zengkan.lankong.config.OssConfig;
import com.zengkan.lankong.service.OSSUploadService;
import com.zengkan.lankong.vo.FileUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/13:17
 * @Description:
 **/
@Service
@Slf4j
public class OSSUploadServiceImpl implements OSSUploadService {

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    @Override
    @Async("taskExecutor")
    public ListenableFuture<FileUploadResult> upload(String fileName, MultipartFile file) {
        FileUploadResult fileUploadResult = new FileUploadResult();
        try {
            PutObjectResult result = ossClient.putObject(ossConfig.getALIYUN_OSS_BUCKET_NAME(), fileName, new ByteArrayInputStream(file.getBytes()));
            fileUploadResult.setStatus("done");
            fileUploadResult.setName(ossConfig.getALIYUN_OSS_URLPREFIX()+fileName);
            fileUploadResult.setResponse("success");
            fileUploadResult.setFileSize(file.getSize());
            fileUploadResult.setUid(result.getRequestId());
            log.info(fileName + ":上传成功");
            return new AsyncResult<>(fileUploadResult);
        }catch (Exception e) {
            log.error(fileName + ":上传失败");
            fileUploadResult.setStatus("error");
            return new AsyncResult<>(fileUploadResult);
        }
    }

    @Override
    @Async("taskExecutor")
    public void delete(String fileName) {
        try {
            ossClient.deleteObject(ossConfig.getALIYUN_OSS_BUCKET_NAME(),fileName);
            log.info(fileName+":删除成功");
        } catch (Exception e) {
            log.error(fileName+":删除失败");
        }

    }
}
