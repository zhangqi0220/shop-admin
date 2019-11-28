package com.fh.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static String getSessionId(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(SystemConstant.COOKIE_KEY)){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
    public static void writeCookie(HttpServletResponse response,String sessionId){
        Cookie cookie = new Cookie(SystemConstant.COOKIE_KEY,sessionId);
        //设置cookie过期时间 单位是秒
        cookie.setMaxAge(SystemConstant.COOKIE_OUT_TIME_DEFAULT);
        //这种cookie作用域
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
