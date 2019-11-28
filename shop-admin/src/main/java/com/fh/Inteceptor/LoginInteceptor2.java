package com.fh.Inteceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fh.common.Ignore;
import com.fh.mapper.RoleResourcesMapper;
import com.fh.model.ResourceInfo;
import com.fh.model.User;
import com.fh.service.user.UserService;
import com.fh.util.RedisUtil;
import com.fh.util.SessionUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class LoginInteceptor2 extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleResourcesMapper roleResourcesMapper;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //将一些经常重复调用的数据放入到缓存中
        Jedis jedis = RedisUtil.getJedis();
        //查出当前用户
        String sessionId = SessionUtil.getSessionId(request, response);
        String strUser = jedis.get(SystemConstant.LOGGIN_CURRENT_USER + sessionId);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法是否有自定义的注解  有注解的方法是要放开的方法
        if (method.isAnnotationPresent(Ignore.class)) {
            return true;
        }
        //请求的路径
        String servletPath = request.getServletPath();
        boolean common = false;
        //从Redis缓存中取
        String allResources = jedis.get(SystemConstant.RESOURCES_ALL);
        List<ResourceInfo> allList;
        if (StringUtils.isNotBlank(allResources)) {//不为空取出即用
            allList = JSONArray.parseObject(allResources, new TypeReference<ArrayList<ResourceInfo>>() {
            });//为用户所拥有的的权限
        } else {//为空 去数据库查
            allList = roleResourcesMapper.queryAllResource();//查出全部的权限
            jedis.set(SystemConstant.RESOURCES_ALL, JSONArray.toJSONString(allList));
            jedis.expire(SystemConstant.RESOURCES_ALL, 1000 * 60 * 60);//设置一小时过期
        }
        for (ResourceInfo resourceInfo : allList) {
            if (StringUtils.isNotBlank(resourceInfo.getUrl()) && servletPath.contains(resourceInfo.getUrl())) {
                common = true;
                break;
            }
        }
        if (!common) { //如果不在全部的权限中 说明这个请求是公共的就放行  否则就去判断当前用户是否有权限
            return true;
        }


        //从Redis缓存中取
        String resources = jedis.get(SystemConstant.RESOURCES_USER);
        List<ResourceInfo> list = null;
        if (StringUtils.isNotBlank(resources)) {//不为空取出即用
            list = JSONArray.parseObject(resources, new TypeReference<ArrayList<ResourceInfo>>() {
            });//为用户所拥有的的权限
        } else {//为空 去数据库查
            if (StringUtils.isNotBlank(strUser)) {
                User user = JSONObject.parseObject(strUser, User.class);
                list = userService.queryUserResource(user.getId());
                jedis.set(SystemConstant.RESOURCES_USER, JSONArray.toJSONString(list));
                jedis.expire(SystemConstant.RESOURCES_USER, 1000 * 60 * 60);//设置一小时过期
            }
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ResourceInfo resourceInfo = list.get(i);
                if (StringUtils.isNotBlank(resourceInfo.getUrl()) && request.getServletPath().contains(resourceInfo.getUrl())) {
                    return true;
                }
            }
        }

        response.sendRedirect("/common/noMenu.jsp");
        return false;
    }
}
