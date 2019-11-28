package com.fh.commons;

public enum ResponseEnum {
    SUCCESS(200,"操作成功"),
    ERROR(1000,"操作失败"),
    CODE_ERROR(1001,"验证码过期"),
    PSW_ERROR(1002,"密码不正确"),
    PHOTO_CODE_ERROR(1004,"验证码错误"),


    TOKEN_IS_NULL(1006,"token为空"),
    REPETITIVE_IS_NULL(1010,"‘车票’为空"),
    REPETITIVE_DISABLE(1011,"禁止重复提交"),
    TOKEN_CHECK_ERROR(1007,"token解析失败"),
    PAY_INFO_ISNULL(1023,"没有订单提交"),
    USER_ERROR(1003,"用户不存在")
    ;
    private int code;
    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
