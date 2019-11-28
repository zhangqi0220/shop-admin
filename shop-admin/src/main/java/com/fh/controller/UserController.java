package com.fh.controller;

import com.alibaba.fastjson.JSON;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.model.Area;
import com.fh.model.ResourceInfo;
import com.fh.model.User;
import com.fh.param.UserSearchParam;
import com.fh.service.user.UserService;
import com.fh.util.RedisUtil;
import com.fh.util.SessionUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user/")
public class UserController {
    @Resource
    private UserService userService;

/*    @RequestMapping("checkUserByName")
    @ResponseBody
    public ServerResponse checkUserByName( String userName){

        return userService.getUserByName(userName);
    }
    @RequestMapping("queryList")
    @ResponseBody
    public ServerResponse queryList(){
        return userService.queryList();
    }*/
    @RequestMapping("checkUserByName")
    @ResponseBody
    @Ignore
    public Map checkUserByName( String userName){
         Map map=new HashMap();
        map.put("valid",userService.getUserByName(userName));
        return map;
    }
    @RequestMapping("queryList")
    @ResponseBody
    public ServerResponse queryList(UserSearchParam userSearchParam){
        return userService.queryList(userSearchParam);
    }

    @RequestMapping("addUser")
    @ResponseBody
    @Ignore
    public ServerResponse addUser(User user,@RequestParam(value = "idList[]",required=false) List idList){
        user.setJoinTime(new Date());
        return userService.addUser(user ,idList);
    }
    @RequestMapping("updateUser")
    @ResponseBody
    public ServerResponse updateUser(User user,@RequestParam(value = "idList[]",required=false) List idList){

        return userService.updateUser(user ,idList);
    }

    @RequestMapping("login")
    @ResponseBody
    @Ignore
    public ServerResponse login(User user, HttpServletRequest request, HttpServletResponse response){
        return userService.login(user,request,response);
    }
    @RequestMapping("queryUserResource")
    @ResponseBody
    public ServerResponse queryUserResource(Integer id){
        return ServerResponse.success(userService.queryUserResource(id));
    }
    @RequestMapping("queryUserResourceByType")
    @ResponseBody
    public ServerResponse queryUserResourceByType(HttpServletResponse response,HttpServletRequest request){
        Jedis jedis = RedisUtil.getJedis();
        String sessionId = SessionUtil.getSessionId(request, response);
        List<ResourceInfo> resourceInfos =null;
        if (StringUtils.isNotBlank(sessionId)){
            String stringUser = jedis.get(SystemConstant.LOGGIN_CURRENT_USER + sessionId);
            User user =  JSON.parseObject(stringUser,User.class);
            resourceInfos = userService.queryUserResourceByType(user.getId());
        }
        return ServerResponse.success(resourceInfos);
    }

    /**
     * 根据Id地区查询
     * @param id
     * @return
     */
    @RequestMapping("queryArea")
    @ResponseBody
    @Ignore
    public ServerResponse queryArea(Integer id){
        return ServerResponse.success(userService.queryArea(id));
    }

    /**
     * 查询全部的地区
     * @return
     */
    @RequestMapping("queryAreaList")
    @ResponseBody
    public ServerResponse queryAreaList(){
        return userService.queryAreaList();
    }

    /**
     * 地区的增加
     * @return
     */
    @RequestMapping("addArea")
    @ResponseBody
    public ServerResponse addArea(Area area){
        return userService.addArea(area);
    }
    /**
     * 地区的修改
     * @return
     */
    @RequestMapping("updateArea")
    @ResponseBody
    public ServerResponse updateArea(Area area){
        return userService.updateArea(area);
    }

    @RequestMapping("deleteAreas")
    @ResponseBody
    public ServerResponse deleteAreas(@RequestParam("idList[]") List idList){
        return userService.deleteAreas(idList);
    }

    /**
     * 刷新缓存
     * @return
     */
    @RequestMapping("refresh")
    @ResponseBody
    public ServerResponse refresh(){
        Jedis jedis = RedisUtil.getJedis();
        jedis.del(SystemConstant.RESOURCES_ALL);
        jedis.del(SystemConstant.RESOURCES_USER);
        return ServerResponse.success();
    }
    /**
     * 注销
     * @return
     */
    @RequestMapping("logout")
    @ResponseBody
    public ServerResponse logout(HttpServletRequest request,HttpServletResponse response){
        Jedis jedis = RedisUtil.getJedis();
        String sessionId = SessionUtil.getSessionId(request, response);
        jedis.expire(SystemConstant.LOGGIN_CURRENT_USER+sessionId,0 );
        return ServerResponse.success();
    }

    /**
     * 获取短信验证
     * @return
     */
    @RequestMapping("getPhoneCode")
    @ResponseBody
    @Ignore
    public ServerResponse getPhoneCode(String phoneNum) throws IOException {

        return userService.getPhoneCode(phoneNum);
    }
    @RequestMapping("updatePassWord")
    @ResponseBody
    @Ignore
    public ServerResponse updatePassWord(String code,User user){
        return userService.updatePassWord(code,user);
    }


}
