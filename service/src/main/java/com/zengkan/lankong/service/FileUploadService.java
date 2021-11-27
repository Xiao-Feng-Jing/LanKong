package com.zengkan.lankong.service;

import com.zengkan.lankong.vo.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/13:08
 * @Description:
 **/
public interface FileUploadService {
    List<FileUploadResult> upload(MultipartFile[] files, boolean isImage);

    void delete(String fileName);

    void download(String fileName, HttpServletResponse response);

    FileUploadResult append(MultipartFile file,String fileName);

    String range(String fileName, Long minSize, Long maxSize);
}
