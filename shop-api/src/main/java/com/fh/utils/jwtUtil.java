package com.fh.utils;

import com.fh.commons.ResponseServer;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class jwtUtil {
    /**
     * 1.创建一个32-byte的密匙
     *
     * 长度有要求
     */

    private static final byte[] secret = "xvsdfgwadhbjkvkhvjgchfcgvkhvkgcvfc".getBytes();


    /**
     * 创建token
     * @param payloadMap
     * @return
     * @throws JOSEException
     */
    public static String creatToken(Map<String,Object> payloadMap) throws JOSEException {
        //3.先建立一个头部Header
        /**
         * JWSHeader参数：1.加密算法法则,2.类型，3.。。。。。。。
         * 一般只需要传入加密算法法则就可以。
         * 这里则采用HS256
         *
         * JWSAlgorithm类里面有所有的加密算法法则，直接调用。
         */
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //建立一个载荷Payload
        Payload payload = new Payload(new JSONObject(payloadMap));
        //将头部和载荷结合在一起
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //建立一个密匙
        JWSSigner jwsSigner = new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);

        //生成token
        return jwsObject.serialize();
    }


    /**
     * 解析token
     * @param token
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static Map<String,Object> valid(String token) throws ParseException, JOSEException {
//        解析token
        JWSObject jwsObject = JWSObject.parse(token);
        //获取到载荷
        Payload payload=jwsObject.getPayload();
        //建立一个解锁密匙
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //判断token
        if (jwsObject.verify(jwsVerifier)) {
            resultMap.put("Result", 0);
            //载荷的数据解析成json对象。
            JSONObject jsonObject = payload.toJSONObject();
            resultMap.put("data", jsonObject);

            //判断token是否过期
           /* if (jsonObject.containsKey("exp")) {
                Long expTime = Long.valueOf(jsonObject.get("exp").toString());
                Long nowTime = new Date().getTime();
                //判断是否过期
                if (nowTime > expTime) {
                    //已经过期
                    resultMap.clear();
                    resultMap.put("Result", 2);
                }
            }*/
        }else {
            resultMap.put("Result", 1);//解析失败
        }
        return resultMap;
    }

    /**
     * 生成token的业务逻辑
     * @param uid
     * @return
     */
    public static String getToken(String uid) {
        //获取生成token

        Map<String, Object> map = new HashMap<>();

        //建立载荷，这些数据根据业务，自己定义。
        map.put("user", uid);
        //生成时间
        map.put("sta", new Date().getTime());
        //过期时间
        map.put("exp", new Date().getTime()+1000*60*60*24);
        try {
            String token = creatToken(map);
            System.out.println("生成时间："+ new Date().getTime());
            System.out.println( "过期时间:"+new Date().getTime()+100000);
            System.out.println("token="+token);
            return token;
        } catch (JOSEException e) {
            System.out.println("生成token失败");
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 处理解析的业务逻辑
     * @param token
     */
    public static ResponseServer ValidToken(String token) {
        Map map = new HashMap();

        //解析token
        try {
            if (token != null) {

                Map<String, Object> validMap = valid(token);
                Integer i = (Integer) validMap.get("Result");
                map.put("status",i );
                if (i == 0) {
                    System.out.println("token解析成功");
                    JSONObject jsonObject = (JSONObject) validMap.get("data");
                    String user =(String)jsonObject.get("user");
                    map.put("data", user);
                    // System.out.println("uid是" + jsonObject.get("user"));
                    System.out.println("sta是"+jsonObject.get("sta"));
                    System.out.println("exp是"+jsonObject.get("exp"));
                } else if (i == 2) {
                    System.out.println("token已经过期");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return ResponseServer.success(map);
    }

    public static void main(String[] ages) throws JOSEException {

        //获取token
        String uid = "7312314sdf";
       String token = getToken(uid);
        //解析token
        //String token="eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjM0NSIsInN0YSI6MTU3MTgxODA3MTczMiwiZXhwIjoxNTcxODE4MDgxNzMyfQ.BsKby8Rh8NzXRGm7ONuMx38KukaPYPEjeWtxU5AW2uI";
        ValidToken(token);
    }

}
