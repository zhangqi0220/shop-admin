package com.fh.commons;

public class ResponseServer {
    private int status;
    private String msg;
    private Object data;

    private ResponseServer() {
    }
    public static ResponseServer success(){
        return new ResponseServer(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }
    public static ResponseServer loginPswError(){
        return new ResponseServer(ResponseEnum.PSW_ERROR.getCode(), ResponseEnum.PSW_ERROR.getMsg());
    }
    public static ResponseServer loginUserError(){
        return new ResponseServer(ResponseEnum.USER_ERROR.getCode(), ResponseEnum.USER_ERROR.getMsg());
    }
    public static ResponseServer loginPhotoError(){
        return new ResponseServer(ResponseEnum.PHOTO_CODE_ERROR.getCode(), ResponseEnum.PHOTO_CODE_ERROR.getMsg());
    }

    public static ResponseServer error(){
        return new ResponseServer(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMsg());
    }
    public static ResponseServer error(String msg){
        return new ResponseServer(ResponseEnum.ERROR.getCode(), msg);
    }
    public static ResponseServer tokenError(ResponseEnum responseEnum){

        return new ResponseServer(responseEnum.getCode(),responseEnum.getMsg());
    }
    public static ResponseServer codeError(){
        return new ResponseServer(ResponseEnum.CODE_ERROR.getCode(), ResponseEnum.CODE_ERROR.getMsg());
    }

    public static ResponseServer success(Object data){
        return new ResponseServer(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),data);
    }

    private ResponseServer(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    public ResponseServer(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
