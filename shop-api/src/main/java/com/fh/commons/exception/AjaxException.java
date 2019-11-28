package com.fh.commons.exception;

import com.fh.commons.ResponseEnum;

public class AjaxException extends RuntimeException {
    private ResponseEnum responseEnum;

    public AjaxException(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }


    public ResponseEnum getResponseEnum() {
        return this.responseEnum;
    }
}
