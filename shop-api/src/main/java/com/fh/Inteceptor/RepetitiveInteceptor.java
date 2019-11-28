package com.fh.Inteceptor;



import com.fh.commons.Repetitive;
import com.fh.commons.ResponseEnum;
import com.fh.commons.exception.AjaxException;
import com.fh.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RepetitiveInteceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod =(HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //查看方法是不是需要幂等性的方法   不是 方行
        boolean annotationPresent = method.isAnnotationPresent(Repetitive.class);
        if (!annotationPresent){
            return true;
        }
        //是的话查看
        String repetitive = request.getHeader("repetitive");
        if (StringUtils.isEmpty(repetitive)){
            throw new AjaxException(ResponseEnum.REPETITIVE_IS_NULL);
        }
        Jedis jedis = RedisUtil.getJedis();
        Long del = jedis.del(repetitive);
        if (del == 0L){
            throw new AjaxException(ResponseEnum.REPETITIVE_DISABLE);
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
