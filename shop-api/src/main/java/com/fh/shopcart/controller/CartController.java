package com.fh.shopcart.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.commons.Repetitive;
import com.fh.commons.ResponseServer;
import com.fh.shopcart.biz.CartService;
import com.fh.shopcart.model.ShopCart;
import com.fh.user.model.User;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    @Repetitive    //防止表单重复提交注解
    public ResponseServer buy(HttpServletRequest request, Integer count, Integer productId) {
        return cartService.sendCart(request, count, productId);
    }
    @RequestMapping("queryCart")
    public ResponseServer queryCart(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        Jedis jedis = RedisUtil.getJedis();
        String hget = jedis.hget(SystemConstant.USER_SHOP_CART, user.getId().toString());
        if (StringUtils.isEmpty(hget)){
            return ResponseServer.error(SystemConstant.SHOPCART_IS_NULL);
        }
        ShopCart shopCart = JSONObject.parseObject(hget, ShopCart.class);
        if (shopCart.getList().size()==0){
            return ResponseServer.error(SystemConstant.SHOPCART_IS_NULL);
        }
        return ResponseServer.success(shopCart);
    }
    @RequestMapping("del")
    public ResponseServer del(HttpServletRequest request,Integer productId){
        return cartService.del(request,productId);
    }


}

