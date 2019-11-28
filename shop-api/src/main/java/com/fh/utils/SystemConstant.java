package com.fh.utils;

public class SystemConstant {

    public static final  String PAY_INFO_ISNULL="支付信息为空";
    public static final  String PAY_INFO_EXCEPTION="支付异常，请刷新再次尝试";

    public static final  String PHONE_IS_NONULL="手机号不为空";
    public static final  String USER_IS_NONULL="用户名已存在";
    public static final  String PHONE_CODE_ERROR="验证码错误";
    public static final  String TOKEN_KEY="token:";

    public static final  String PHOTO_CODE="photo-code";

    public static final  String TEMPLATE_PATH="/template";
    public static final  String EXCEL_FILE_PATH="D:/outFile/";
    public static final  String EXCEL_TIMPATE_FILE_PATH="bbb.xml";
    public static final  String WORD_TIMPATE_FILE_PATH="word-template.xml";
    public static final  String PDF_TIMPATE_FILE_PATH="pdf-template.html";
    public static final  String UPLOAD_FILE_PATH="upload";
    public static final  int LOGGIN_USERNAME_ERROR=1;
    public static final  int LOGGIN_PASSWORD_ERROR=2;
    public static final  int LOGGIN_SUCCESS=3;
    public static final  String LOGGIN_CURRENT_USER="user";
    public static final  String COOKIE_KEY="login";
    public static final  int COOKIE_OUT_TIME=7*24*60*60;//记住cookie一周
    public static final  int COOKIE_OUT_TIME_DEFAULT=30*60;//使cookie有效30分钟

    public static final  int LOG_ERROR=0;
    public static final  int LOG_SUCCESS=1;
    public static final  String LOGIN_PAGE="/login.jsp";
    public static final  String AJAX_SESSION_OUT="timeOut";
    public static final  int ADD_PRODUCT_LIST_SIZE =10;
        /*权限*/
    public static final  String RESOURCES_ALL="allResources";
    public static final  String RESOURCES_USER="resources";
    /*短信验证*/
    public static final  String PHONE_CODE="phoneCode";
    /*查询分类的key*/
    public static final  String CATEGORYLIST="gategoryList";
    /*查询商品的key*/
    public static final  String PRODUCT_LIST="productList";
    public static final  String PRODUCT_IS_NULL="商品不存在";
    public static final  String PRODUCT_SOLD_OUT_="商品已下架";
    /*操作分类的 锁key*/
    public static final  String CATEGORY_LOCK="gategoryLock";
    public static final  String USER_SHOP_CART="userShopCart";

    public static final  String SHOPCART_IS_NULL="购物车为空";
    public static final  String ALLPRODUCT_IS_NULL="商品库存全部不足";


    //订单状态
    public static final  int ORDER_PAY_STATUS=1;//账单支付状态

    public static final  String PAY_LOG_KEY="payLogKey";//支付日志

    public static final  int PAY_WAITING=1;//等待支付中
    public static final  int PAY_ERROR=2;//支付失败
    public static final  int PAY_SUCCESS=3;//支付成功
}
