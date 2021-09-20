package com.zengkan.lankong.vo;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ResponseBean {

    private long code;
    private String msg;
    private long enCode;
    private Object data;

    public ResponseBean(long code, String msg ,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseBean(long code, String msg, long enCode){
        this.code = code;
        this.msg = msg;
        this.enCode = enCode;
    }
}
