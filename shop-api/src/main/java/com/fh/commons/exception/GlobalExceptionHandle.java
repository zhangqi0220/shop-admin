package com.fh.commons.exception;

import com.fh.commons.ResponseServer;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandle {

    //定义要捕捉的异常
    @ExceptionHandler(AjaxException.class)
    //@ResponseBody
    public ResponseServer handException2(AjaxException ex) {
        ex.printStackTrace();
        return ResponseServer.tokenError(ex.getResponseEnum());

    }

    @ExceptionHandler(Exception.class)
    // @ResponseBody
    public ResponseServer handException(Exception ex) {
        ex.printStackTrace();
        return ResponseServer.error();

    }


}
