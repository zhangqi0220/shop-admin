package com.fh.user.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.commons.ResponseServer;
import com.fh.user.model.User;
import com.fh.user.mapper.UserMapper;
import com.fh.utils.*;
import com.fh.utils.jwtUtil;
import com.nimbusds.jose.JOSEException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMappers;

    private Jedis jedis = RedisUtil.getJedis();

    @Override
    public boolean queryUserByName(String userName) {

        User user =  userMappers.queryUserByName(userName);
        if(user ==null){
            //用户不存在   可以注册
           return true;
        }else
            //用户已存在   不可以注册
            return false;
    }


    @Override
    public ResponseServer addUser(User user, String phoneCode) {
        //先判断验证码是否相同
        String phoneCodeStr = jedis.get(SystemConstant.PHONE_CODE);
        Map map = JSONObject.parseObject(phoneCodeStr, Map.class);
        String code = (String) map.get("obj");
        if (!code.equals(code)){return ResponseServer.success(SystemConstant.PHONE_CODE_ERROR);}

        //判断用户名是否存在
        User us =  userMappers.queryUserByName(user.getUserName());
      if (us!=null){return ResponseServer.success(SystemConstant.PHONE_IS_NONULL);}

        //判断用户手机是否存在
      User use =  userMappers.getUserByPhoneNum(user.getPhoneNum());
      if (use!=null){return ResponseServer.success(SystemConstant.USER_IS_NONULL);}

       String salt =  RandomStringUtils.randomAlphanumeric(20);
       String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt)) ;

       user.setPassWord(encodePassWord);
        user.setSalt(salt);
        userMappers.addUser(user);
        return ResponseServer.success();
    }


    @Override
    public ResponseServer login(User user, HttpServletRequest request, HttpServletResponse response, String photoCode) throws JOSEException {
        String photoCodeStr = jedis.get(SystemConstant.PHONE_CODE);

        if (StringUtils.isEmpty(photoCodeStr)&&!photoCode.equals(photoCodeStr)){
            return ResponseServer.loginPhotoError();
        }
        User us =  userMappers.queryUserByName(user.getUserName());
        if(us==null){
            //用户不存在
            return ResponseServer.loginUserError();
        }else{
           String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+us.getSalt()));
            if(!us.getPassWord().equals(encodePassWord)){
                //用户密码错误
                return ResponseServer.loginPswError();
            }else{
                String userStr = JSONObject.toJSONString(us);
                //登陆成功
                String token = jwtUtil.getToken(userStr);

                if(user.getRememberMe()==1){//记住 密码

                }
                jedis.set(SystemConstant.TOKEN_KEY+token, token);
                jedis.expire(SystemConstant.TOKEN_KEY+token,60*60*24);
                return ResponseServer.success(token);
            }
        }

    }
    @Override
    public ResponseServer getMes(String phone) throws IOException {
        if (StringUtils.isEmpty(phone)){
            return ResponseServer.error();
        }
        String voice = PhoneUtil.phoneVoice(phone);
        Jedis jedis = RedisUtil.getJedis();
        jedis.set(SystemConstant.PHONE_CODE, voice);
        jedis.expire(SystemConstant.PHONE_CODE, 1000*60*3);
        return ResponseServer.success();
    }

}
