package com.zengkan.lankong.vo;

import com.aliyun.oss.model.Callback;
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
@ApiModel("回调参数模型")
public class OssCallbackParam {

    @ApiModelProperty("请求的回调地址")
    private String callbackUrl;
    @ApiModelProperty("回调是传入request中的参数")
    private String callbackBody;
    @ApiModelProperty("回调时传入参数格式，比如表单提交形式")
    private Callback.CalbackBodyType callbackBodyType;
}
