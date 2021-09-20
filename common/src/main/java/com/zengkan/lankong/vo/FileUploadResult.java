package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/22:44
 * @Description:
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("文件成功上传模型")
public class FileUploadResult {

    @ApiModelProperty("文件唯一标识")
    private String uid;
    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty("文件大小")
    private Long fileSize;
    @ApiModelProperty("起始位置")
    private Long filePosition;
    @ApiModelProperty("状态有：uploading done error removed")
    private String status;
    @ApiModelProperty("服务端响应内容，如：'{\"status\": \"success\"}'")
    private String response;
}
