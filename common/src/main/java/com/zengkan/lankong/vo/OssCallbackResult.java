package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/17:32
 * @Description:
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("回调数据模型")
public class OssCallbackResult {

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private String fileSize;

    /**
     * 文件的绝对路径
     */
    @ApiModelProperty("文件的绝对路径")
    private String fileUrl;

    /**
     * 文件的web访问地址
     */
    @ApiModelProperty("文件的web访问地址")
    private String webUrl;

    /**
     * 文件后缀
     */
    @ApiModelProperty("文件后缀")
    private String fileSuffix;

    /**
     * 存储的bucket
     */
    @ApiModelProperty("存储的bucket")
    private String fileBucket;

    /**
     * 原文件名
     */
    @ApiModelProperty("原文件名")
    private String oldFileName;

    /**
     * 存储的文件夹
     */
    @ApiModelProperty("存储的文件夹")
    private String folder;
}
