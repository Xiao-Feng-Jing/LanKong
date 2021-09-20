package com.zengkan.lankong.service;

import com.zengkan.lankong.vo.FileUploadResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/13:50
 * @Description:
 **/
public interface OSSUploadService {
    ListenableFuture<FileUploadResult> upload(String fileName, MultipartFile file);

    void delete(String fileName);
}
