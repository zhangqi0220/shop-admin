package com.fh.wxpay.sdk;

import com.fh.wxpay.sdk.MyConfig;
import com.fh.wxpay.sdk.WXPay;

import java.util.HashMap;
import java.util.Map;

public class WXPayExample {

    public static void main(String[] args) throws Exception {
        //统一下单：
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "腾讯充值中心-QQ会员充值"); //商品描述
        data.put("out_trade_no", "2016090910595900000012"); //商户订单号
        data.put("total_fee", "1");//  订单总金额，单位为分
        data.put("spbill_create_ip", "123.12.12.123");//支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
        data.put("notify_url", "http://www.example.com/wxpay/notify");//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data,8000,10000);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}