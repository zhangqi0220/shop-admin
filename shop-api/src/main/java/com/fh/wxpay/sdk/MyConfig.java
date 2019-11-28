package com.fh.wxpay.sdk;

import com.fh.wxpay.sdk.WXPayConfig;
import java.io.*;
public class MyConfig extends WXPayConfig{
    private byte[] certData;


    @Override
    public String getAppID() {
        return "wxa1e44e130a9a8eee";
    } //微信支付分配的公众账号ID（企业号corpid即为此appId）

    @Override
    public String getMchID() {
        return "1507758211";
    } //微信支付分配的商户号

    @Override
    public String getKey() {
        return "feihujiaoyu12345678yuxiaoyang123";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            //调用不需要证书的方法
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
