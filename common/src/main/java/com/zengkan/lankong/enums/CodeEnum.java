package com.zengkan.lankong.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/27/13:17
 * @Description : 状态码
 * @modified By :
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CodeEnum {
    /**
     * 请求成功
     * */
    SUCCESS(200, "ok"),
    /**
     * 保存成功
     * */
    SAVE_SUCCESS(201, "save ok"),
    /**
     * 修改成功
     * */
    UPDATE_SUCCESS(205, "update ok"),
    /**
     * 删除成功
     * */
    DELETE_SUCCESS(205, "delete ok"),
    /**
     *
     * */
    UNAUTHORIZED(401,"Unauthorized"),
    /**
     * 资源不存在
     * */
    FAIL(404, "FAIL"),
    /**
     * 服务器错误
     * */
    ERROR(500, "error");

    private final int value;
    private final String msg;

    CodeEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Code{" +
                "value=" + value +
                ", msg='" + msg + '\'' +
                '}';
    }
}
