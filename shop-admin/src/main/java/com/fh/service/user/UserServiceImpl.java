package com.fh.service.user;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fh.common.ServerResponse;
import com.fh.mapper.UserMapper;
import com.fh.model.Area;
import com.fh.model.ResourceInfo;
import com.fh.model.User;
import com.fh.param.UserSearchParam;
import com.fh.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    private Jedis jedis = RedisUtil.getJedis();

    @Override
    public boolean getUserByName(String userName) {

        User user =  userMapper.getUserByName(userName);
        if(user ==null){
            //用户不存在   可以注册
           return true;
        }else
            //用户已存在   不可以注册
            return false;
    }


    @Override
    public ServerResponse queryList(UserSearchParam userSearchParam) {
        //查询总条数
        long totalCount = userMapper.queryCount(userSearchParam);
        List<User> list =  userMapper.queryList(userSearchParam);
        Map map = new HashMap();
        map.put("draw",userSearchParam.getDraw());
        map.put("recordsTotal",totalCount);
        map.put("recordsFiltered",totalCount);
        map.put("data",list);

        return ServerResponse.success(map);
    }

    @Override
    public ServerResponse addUser(User user,List idList) {
       String salt =  RandomStringUtils.randomAlphanumeric(20);
       String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt))  ;
       user.setPassWord(encodePassWord);
        user.setSalt(salt);
        userMapper.addUser(user);
        if (idList != null && idList.size()>0){
            Map map = new HashMap();
            map.put("userId", user.getId());
            map.put("list", idList);
            userMapper.addUserRole(map);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateUser(User user, List idList) {
        //修改用户
        userMapper.updateUser(user);
        //删除用户当前角色
        userMapper.deleteUserRole(user.getId());
        // 再添加角色
        Map map = new HashMap();
        map.put("userId", user.getId());
        map.put("list", idList);
        userMapper.addUserRole(map);
        return ServerResponse.success();
    }

    /**
     * 获取短信验证
     * @param phoneNum
     * @return
     */
    @Override
    public ServerResponse getPhoneCode(String phoneNum) throws IOException {
        String code = PhoneUtil.phoneVoice(phoneNum);
        //将短信验证码放入到redis中并设置过期时间
        jedis.set(SystemConstant.PHONE_CODE, code);
        jedis.expire(SystemConstant.PHONE_CODE, 1000*60);//设置过期时间
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updatePassWord(String code, User user) {
        //从redis中取出验证码
        String string = jedis.get(SystemConstant.PHONE_CODE);
        //如果为空则说明验证码过期
        if (StringUtils.isEmpty(string)){
            return ServerResponse.codeError();
        }
        JSONObject jsonObject = JSONObject.fromObject(string);
        Map map =(Map)JSONObject.toBean(jsonObject, Map.class);

        //不过期判断验证码是否一致
        if (!code.equals(map.get("obj"))){
            return ServerResponse.error();
        }
        //一致后修改密码
        //随机数
        String salt =  RandomStringUtils.randomAlphanumeric(20);
        String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt))  ;
        user.setPassWord(encodePassWord);
        user.setSalt(salt);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("phoneNum", user.getPhoneNum());
        userMapper.update(user, wrapper);

        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(User user, HttpServletRequest request, HttpServletResponse response) {
        int flag=0;
        User us =  userMapper.getUserByName(user.getUserName());
        if(us==null){
            //用户不存在
            flag= SystemConstant.LOGGIN_USERNAME_ERROR;
        }else{
           String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+us.getSalt()));
            if(!us.getPassWord().equals(encodePassWord)){
                //用户密码错误
                flag=SystemConstant.LOGGIN_PASSWORD_ERROR;
            }else{
                //登陆成功
                flag=SystemConstant.LOGGIN_SUCCESS;
                //把用户信息放到session中
                String sessionId = SessionUtil.getSessionId(request, response);
                CookieUtil.writeCookie(response,sessionId);
                String userString = JSON.toJSONString(us);
                //将对象转为字符串放入redis中
                jedis.set(SystemConstant.LOGGIN_CURRENT_USER+sessionId,userString);

                if(user.getRememberMe()==1){//记住 密码
                   // writeCookie(user, response);
                }
            }
        }
        return ServerResponse.success(flag);
    }

    @Override
    public List<ResourceInfo> queryUserResource(Integer id) {

        return userMapper.queryUserResource(id);
    }

    @Override
    public List<Area> queryArea(Integer id) {
        return userMapper.queryArea(id);
    }

    @Override
    public ServerResponse queryAreaList() {
        List<Area> list= userMapper.queryAreaList();
        return ServerResponse.success(list);
    }

    /**
     *增加地区
     * @param area
     * @return
     */
    @Override
    public ServerResponse addArea(Area area) {
        userMapper.addArea(area);
        return ServerResponse.success();
    }

    /**
     * 修改地区
     * @param area
     * @return
     */
    @Override
    public ServerResponse updateArea(Area area) {
        userMapper.updateArea(area);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteAreas(List idList) {
                    userMapper.deleteAreas(idList);
        return ServerResponse.success();
    }

    @Override
    public List<ResourceInfo> queryUserResourceByType(Integer id) {
        return userMapper.queryUserResourceByType(id);
    }



    private void writeCookie(User user, HttpServletResponse response) {
        //把用户信息存储到cookie中
        try {
        String encodeUserName = URLEncoder.encode(user.getUserName(),"UTF-8");
        Cookie cookie = new Cookie(SystemConstant.COOKIE_KEY,encodeUserName);
        //设置cookie过期时间 单位是秒
        cookie.setMaxAge(SystemConstant.COOKIE_OUT_TIME);
        //cookie.setMaxAge(1*60);
        //这种cookie作用域
        cookie.setPath("/");
        response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
