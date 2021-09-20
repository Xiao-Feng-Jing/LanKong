package com.zengkan.lankong.controller;

import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.service.FileUploadService;
import com.zengkan.lankong.service.OssService;
import com.zengkan.lankong.vo.FileUploadResult;
import com.zengkan.lankong.vo.OssPolicyResult;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/19:16
 * @Description:
 **/
@RestController
@Api(tags = "阿里云oss上传管理")
@RequestMapping("/aliyun/oss")
@Slf4j
public class OssController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";

    private final OssService ossService;

    private final FileUploadService fileUploadService;

    @Autowired
    public OssController(OssService ossService, FileUploadService fileUploadService) {
        this.ossService = ossService;
        this.fileUploadService = fileUploadService;
    }

    @ApiOperation("oss上传签名生成")
    @GetMapping("/policy")
    public ResponseBean policy() throws MyException {
        OssPolicyResult ossPolicyResult = ossService.policy();
        return new ResponseBean(200,SUCCESS_MESSAGE,ossPolicyResult);
    }

    @ApiOperation("oss上传成功回调")
    @PostMapping("callback")
    public ResponseBean callback(HttpServletRequest request){
        /*System.out.println(request);
        System.out.println(ossCallbackResult);*/
        return new ResponseBean(200,SUCCESS_MESSAGE,ossService.callback(request));
    }

    @ApiOperation("文件在后端上传")
    @PostMapping(value = "/upload")
    public ResponseBean upload(@RequestParam(value = "files") MultipartFile[] files, @RequestParam(value = "isImage",required = false) boolean isImage) throws ExecutionException, InterruptedException {
        log.info("文件上传");
        return new ResponseBean(200,SUCCESS_MESSAGE,fileUploadService.upload(files,isImage));
    }

    @ApiOperation("追加上传文件")
    @PostMapping("/append")
    public ResponseBean append(@RequestParam("file") MultipartFile file,@RequestParam(value = "fileName", required = false) String fileName){
        log.info("追加上传文件");
        FileUploadResult fileUploadResult = null;
        try {
            fileUploadResult = fileUploadService.append(file, fileName);
            log.info("上传成功");
        } catch (IOException e) {
            log.error("追加上传失败");
        }
        return new ResponseBean(200,SUCCESS_MESSAGE,fileUploadResult);
    }

    @ApiOperation("根据文件名删除oss上的文件")
    @PostMapping("/delete")
    public ResponseBean delete(@RequestParam("fileName") String fileName){
        fileUploadService.delete(fileName);
        return new ResponseBean(200, SUCCESS_MESSAGE, null);
    }

    @ApiOperation("根据文件名下载oss上的文件")
    @PostMapping("/download")
    public ResponseBean download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        log.info("下载文件");
        fileUploadService.download(fileName, response);
        return new ResponseBean(200, SUCCESS_MESSAGE, null);
    }

    @ApiOperation("根据文件名和参数在一定范围内下载文件")
    @PostMapping("/range")
    public ResponseBean range(String fileName,Long minSize,Long maxSize){
        log.info("范围下载");
        String file = null;
        String message;
        try {
            file = fileUploadService.range(fileName, minSize, maxSize);
            message = SUCCESS_MESSAGE;
            log.info("下载成功");
        } catch (IOException e) {
            message = FAIL_MESSAGE;
            log.error("下载失败");
        }
        return new ResponseBean(200, message, file);
    }
}
