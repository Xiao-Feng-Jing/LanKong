package com.zengkan.lankong.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.vo.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/09/01/2:52
 * @Description:
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    /**
     * 捕捉shiro的异常
     * */
    @ExceptionHandler(ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        log.error(e.getMessage());
        return new ResponseBean(ExceptionEnum.NO_AUTHORIZED, null);
    }

    /**
     * 捕捉shiro的异常
     * */
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseBean handle401(UnauthenticatedException e) {
        log.error("无权限访问");
        return new ResponseBean(ExceptionEnum.NO_AUTHORIZED, null);
    }

    /**
     * @Validated 校验错误异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseBean handler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //这一步是把异常的信息最简化
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        log.error("运行时异常:-------------->{}",objectError);
        return new ResponseBean(ExceptionEnum.INVALID_PARAM,null);
    }

    /**
     * token已经过期
     * */
    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseBean handler(TokenExpiredException e){
        log.error(e.getMessage());
        return new ResponseBean(ExceptionEnum.NO_AUTHORIZED,null);
    }

    /**
     * 自定义异常
     * */
    @ExceptionHandler(value = MyException.class)
    public ResponseBean handler(MyException e) {
        log.error(e.getExceptionEnum().getMsg());
        return new ResponseBean(e.getExceptionEnum(), e.getValue());
    }

}
