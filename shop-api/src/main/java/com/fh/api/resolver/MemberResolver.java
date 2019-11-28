package com.fh.api.resolver;

import com.fh.user.model.User;
import com.fh.utils.SystemConstant;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class MemberResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument。
        boolean b = methodParameter.hasParameterAnnotation(MemberGain.class);
        if (b){
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。
        HttpServletRequest request =  nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        User user = (User)request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        return user;
    }
}
