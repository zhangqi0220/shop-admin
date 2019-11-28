package com.fh.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class SessionUtil {

    public static String getSessionId(HttpServletRequest request, HttpServletResponse response){
        String sessionId = CookieUtil.getSessionId(request);
        if (StringUtils.isEmpty(sessionId)){
            return UUID.randomUUID().toString();
        }
        CookieUtil.writeCookie(response,sessionId );
        return sessionId;
    }


}
