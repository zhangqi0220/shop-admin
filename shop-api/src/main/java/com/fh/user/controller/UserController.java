package com.fh.user.controller;

import com.fh.commons.Ignore;
import com.fh.commons.ResponseServer;
import com.fh.user.model.User;

import com.fh.user.biz.UserService;
import com.fh.utils.Code;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("user/")
public class UserController {
    @Resource
    private UserService userServices;

    @RequestMapping("addUser")
    @Ignore
    public ResponseServer addUser(User user, String phoneCode) {
        return userServices.addUser(user, phoneCode);
    }

    @RequestMapping("login")
    @Ignore
    public ResponseServer login(User user, String photoCode, HttpServletRequest request, HttpServletResponse response) throws JOSEException {

        return userServices.login(user, request, response, photoCode);
    }

    @RequestMapping("getPhoneCode")
    @Ignore
    public ResponseServer getPhoneCode(String phoneNum) throws IOException {
        return userServices.getMes(phoneNum);
    }

    @RequestMapping("checkUserByName")
    @Ignore
    public Map checkUserByName(String userName) {
        Map map = new HashMap();
        map.put("valid", userServices.queryUserByName(userName));
        return map;
    }

    @RequestMapping("getPhotoCode")
    @Ignore
    public void getPhotoCode(HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        Code code = new Code();
        String photoCode = code.getCode();
        Jedis jedis = RedisUtil.getJedis();
        jedis.set(SystemConstant.PHONE_CODE, photoCode);
        jedis.expire(SystemConstant.PHONE_CODE, 1000 * 60*3);
        jedis.close();
        code.write(response.getOutputStream());
    }

    @RequestMapping("queryUser")
    public ResponseServer queryUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        if (user == null) {
            return ResponseServer.error();
        }
        return ResponseServer.success(user);
    }

    /**
     * 获取解决重复提交的“车票”
     * @return
     */
    @RequestMapping("getRedoInfo")
    public ResponseServer getRedoInfo() {
        String ticket = UUID.randomUUID().toString();
        Jedis jedis = RedisUtil.getJedis();
        jedis.set(ticket,ticket);
        return ResponseServer.success(ticket);
    }

}
