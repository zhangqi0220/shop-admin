package com.fh.Inteceptor;


import com.alibaba.fastjson.JSONObject;
import com.fh.commons.exception.AjaxException;
import com.fh.commons.Ignore;
import com.fh.commons.ResponseEnum;
import com.fh.commons.ResponseServer;
import com.fh.user.mapper.UserMapper;
import com.fh.user.model.User;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import com.fh.utils.jwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class MyInteceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Content-Type, X-E4M-With,x-auth,repetitive");
        //预加载不让继续执行
        if (request.getMethod().equalsIgnoreCase("options")){
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法是否有自定义的注解  有注解的方法是要放开的方法
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }

        String header = request.getHeader("x-auth");
        if (StringUtils.isEmpty(header)){
            throw new AjaxException(ResponseEnum.TOKEN_IS_NULL);//token为空
        }
        //从redis中取出token 判断token是否为空
        Jedis jedis = RedisUtil.getJedis();
        String token = jedis.get(SystemConstant.TOKEN_KEY + handler);
        if (StringUtils.isEmpty(token)){
            throw new AjaxException(ResponseEnum.TOKEN_IS_NULL);//token为空
        }
        //response.setHeader("REDIRECT","REDIRECT");
        ResponseServer serverResponse = jwtUtil.ValidToken(token);//从自定义工具类解析token
        Map map = (Map) serverResponse.getData();
        Integer status = (Integer)map.get("status");

        if (status!=0){ //状态为零  不管是过期 还是解析失败  都设置为解析失败
            throw new AjaxException(ResponseEnum.TOKEN_CHECK_ERROR);//过期或者解析失败
        }
        //给token续命加时间
        jedis.expire(SystemConstant.TOKEN_KEY+token, 60*60*24);

        //将用户放到session中 以便后面使用
        String data =(String) map.get("data");
        User user = JSONObject.parseObject(data, User.class);
        request.getSession().setAttribute(SystemConstant.LOGGIN_CURRENT_USER,user);

        User attribute =(User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
