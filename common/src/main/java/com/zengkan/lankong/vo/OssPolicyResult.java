package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/17:30
 * @Description:
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("签名数据模型")
public class OssPolicyResult {
    @ApiModelProperty("访问身份验证中用到用户标识")
    private String accessKeyId;
    @ApiModelProperty("用户表单上传的策略，经过base64编码过的字符串")
    private String policy;
    @ApiModelProperty("签名过期时间")
    private Date expire;
    @ApiModelProperty("对policy签名后的字符串")
    private String signature;
    @ApiModelProperty("上传文件夹路径前缀")
    private String dir;
    @ApiModelProperty("oss对外服务的访问域名")
    private String host;
    @ApiModelProperty("上传成功后的回调设置")
    private String callback;
}
