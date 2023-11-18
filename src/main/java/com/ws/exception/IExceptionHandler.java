package com.ws.exception;

import com.ws.GlobalParam;
import com.ws.base.result.ResultBody;
import com.ws.enu.DefaultInfo;
import com.ws.excepion.IException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @author GSF
 * <p>自定义异常处理及错误日志记录</p>
 */
@ControllerAdvice
@Slf4j
public class IExceptionHandler {

    @ExceptionHandler(IException.class)
    @ResponseBody
    public String iExceptionHandler(IException e) {
        log.error("错误接口: {}", Objects.requireNonNull(GlobalParam.getRequest()).getServletPath());
        log.error("错误信息", e);
        return ResultBody.error(e.getErrorCode(), e.getMessage()).toJsonyMdHms();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        log.error("错误接口: {}", Objects.requireNonNull(GlobalParam.getRequest()).getServletPath());
        log.error("错误信息", e);
        return ResultBody.error(DefaultInfo.INTERNAL_SERVER_ERROR).toJsonyMdHms();
    }

}


