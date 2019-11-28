package com.fh.paylog.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.commons.ResponseEnum;
import com.fh.commons.ResponseServer;
import com.fh.order.mapper.OrderMapper;
import com.fh.paylog.mapper.PayLogMapper;
import com.fh.paylog.model.PayLog;
import com.fh.user.model.User;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import com.fh.wxpay.sdk.MyConfig;
import com.fh.wxpay.sdk.WXPay;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayLogServiceImpl implements PayLogService {
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ResponseServer getPayNum(User user) {
        try {
            //通过用户id查出支付日志
            Jedis jedis = RedisUtil.getJedis();
            String payLogStr = jedis.get(SystemConstant.PAY_LOG_KEY + user.getId());
            //判断支付日志是否为空
            if (StringUtils.isEmpty(payLogStr)) {
                return ResponseServer.tokenError(ResponseEnum.PAY_INFO_ISNULL);
            }
            PayLog payLog = JSONObject.parseObject(payLogStr, PayLog.class);
            //统一下单：
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "一袋一袋"); //商品描述
            data.put("out_trade_no", payLog.getOut_trade_no()); //商户订单号
            data.put("total_fee", "1");//  订单总金额，单位为分
            data.put("spbill_create_ip", "123.12.12.123");//支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
            data.put("notify_url", "http://www.example.com/wxpay/notify");//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = DateUtils.addMinutes(new Date(), 3);//加时三分钟
            data.put("time_expire", simpleDateFormat.format(date));  // 此处指定为扫码支付

            Map<String, String> resp = wxpay.unifiedOrder(data, 8000, 10000);
            System.out.println(resp);
            //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            //判断通信标识是否成功
            if (!resp.get("return_code").equalsIgnoreCase("SUCCESS")) {
                //不成功返回状态码
                return ResponseServer.error(resp.get("return_msg"));
            }
            //业务结果
            //判断业务结果是否成功
            if (!resp.get("result_code").equalsIgnoreCase("SUCCESS")) {
                //不成功返回状态码
                return ResponseServer.error(resp.get("err_code_des"));
            }
            //成功后返回二维码路径  和 一些必要参数
            Map<String, String> map = new HashMap<>();
            //需要支付的价格
            map.put("totalPrice", payLog.getTotalPrice().toString());
            //支付日志的流水号
            map.put("out_trade_no", payLog.getOut_trade_no());
            //成功返回的二维码路径
            map.put("code_url", resp.get("code_url"));
            return ResponseServer.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseServer.error(SystemConstant.PAY_INFO_EXCEPTION);
        }

    }

    @Override
    public ResponseServer seeOrderStatus(User user) {

        try {

            Jedis jedis = RedisUtil.getJedis();
            String payLogString = jedis.get(SystemConstant.PAY_LOG_KEY + user.getId());
            if (StringUtils.isEmpty(payLogString)) {
                return ResponseServer.error(SystemConstant.PAY_INFO_ISNULL);
            }
            PayLog payLog = JSONObject.parseObject(payLogString, PayLog.class);

            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", payLog.getOut_trade_no());
            int count=0;
            for (; ; ) {
                Map<String, String> resp = wxpay.orderQuery(data);
                System.out.println(resp);
                //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
                //判断通信标识是否成功
                if (!resp.get("return_code").equalsIgnoreCase("SUCCESS")) {
                    //不成功返回状态码
                    return ResponseServer.error(resp.get("return_msg"));
                }
                //业务结果
                //判断业务结果是否成功
                if (!resp.get("result_code").equalsIgnoreCase("SUCCESS")) {
                    //不成功返回状态码
                    return ResponseServer.error(resp.get("err_code_des"));
                }
                //支付成功  SUCCESS—支付成功  REFUND—转入退款  NOTPAY—未支付  CLOSED—已关闭
                // REVOKED—已撤销（付款码支付）
                //USERPAYING--用户支付中（付款码支付）
                //PAYERROR--支付失败(其他原因，如银行返回失败)
                if (resp.get("trade_state").equalsIgnoreCase("SUCCESS")) {
                    //成功返回状态码 并修改订单支付状态
                    orderMapper.updateOrderStatus(payLog.getOrderId(),SystemConstant.PAY_SUCCESS);
                    //修改支付日志状态
                    payLog.setPayStatus(SystemConstant.PAY_SUCCESS);
                    payLogMapper.updateById(payLog);
                    //删除redis中的日志
                    jedis.del(SystemConstant.PAY_LOG_KEY+user.getId());
                    jedis.close();
                    return ResponseServer.success(payLog.getTotalPrice());
                }
                //线程休眠3秒
                Thread.sleep(3000);
                //判断 二维码过期的三分钟内 方法最多执行次数 三分钟 180秒 除去3 秒
                //也就是说最低 也要60次 加上线程执行时间 大致59次
                if (count>59){
                      return  ResponseServer.error(SystemConstant.PAY_INFO_EXCEPTION);
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseServer.error();
        }

    }
}
