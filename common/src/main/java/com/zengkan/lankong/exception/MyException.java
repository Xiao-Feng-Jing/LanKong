package com.zengkan.lankong.exception;

import com.zengkan.lankong.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/15/22:16
 * @Description : 自定义异常
 * @modified By :
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MyException extends RuntimeException{
    private static final long serialVersionUID = 4437827886870252240L;
    private final ExceptionEnum exceptionEnum;

    private final transient Object value;

    public MyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
        this.value = null;
    }
}
