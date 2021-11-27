package com.zengkan.lankong.controller;

import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.service.FileUploadService;
import com.zengkan.lankong.service.OssService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.FileUploadResult;
import com.zengkan.lankong.vo.OssPolicyResult;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
public class OssController {

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
        return new ResponseBean(CodeEnum.SUCCESS, ossPolicyResult);
    }

    @ApiOperation("oss上传成功回调")
    @PostMapping("/callback")
    public ResponseBean callback(HttpServletRequest request){
        /*System.out.println(request);
        System.out.println(ossCallbackResult);*/
        return new ResponseBean(CodeEnum.SUCCESS, ossService.callback(request));
    }

    @ApiOperation("文件在后端上传")
    @PostMapping("/upload")
    public ResponseBean upload(@RequestParam(value = "files") MultipartFile[] files, @RequestParam(value = "isImage",required = false) boolean isImage){
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, fileUploadService.upload(files,isImage));
    }

    @ApiOperation("追加上传文件")
    @PostMapping("/append")
    public ResponseBean append(@RequestParam("file") MultipartFile file,@RequestParam(value = "fileName", required = false) String fileName){
        FileUploadResult fileUploadResult = fileUploadService.append(file, fileName);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, fileUploadResult);
    }

    @ApiOperation("根据文件名删除oss上的文件")
    @PostMapping("/delete")
    public ResponseBean delete(@RequestParam("fileName") String fileName){
        fileUploadService.delete(fileName);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    @ApiOperation("根据文件名下载oss上的文件")
    @PostMapping("/download")
    public ResponseBean download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        fileUploadService.download(fileName, response);
        return new ResponseBean(CodeEnum.SUCCESS, null);
    }

    @ApiOperation("根据文件名和参数在一定范围内下载文件")
    @PostMapping("/range")
    public ResponseBean range(String fileName,Long minSize,Long maxSize){
        return new ResponseBean(CodeEnum.SUCCESS, fileUploadService.range(fileName, minSize, maxSize));
    }
}
