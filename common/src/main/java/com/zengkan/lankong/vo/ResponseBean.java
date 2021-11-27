package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/01/25/17:14
 * @Description:
 **/
@Data
@NoArgsConstructor
@ApiModel("数据响应模型")
public class ResponseBean {

    @ApiModelProperty("响应状态码，响应消息")
    private Object code;
    @ApiModelProperty("响应数据")
    private Object data;

    public ResponseBean(Object code , Object data){
        this.code = code;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean(code=" + this.getCode()
                + ", data=" + this.getData() + ")";
    }
}
