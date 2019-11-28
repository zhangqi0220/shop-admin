package com.fh.shopcart.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fh.commons.ResponseServer;
import com.fh.product.model.Product;
import com.fh.shopcart.mapper.CartMapper;
import com.fh.shopcart.model.CartInfo;
import com.fh.shopcart.model.ShopCart;
import com.fh.user.model.User;
import com.fh.utils.BigDecimalUtils;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public ResponseServer sendCart(HttpServletRequest request, Integer count, Integer productId) {
        Jedis jedis = RedisUtil.getJedis();
        //1、查询商品是否存在
        Product product = cartMapper.queryListById(productId);
        if (product==null){
            return ResponseServer.error(SystemConstant.PRODUCT_IS_NULL);
        }
        //2、查询商品是否下架
        Integer status = product.getStatus();
        if (status!=1){
            return ResponseServer.error(SystemConstant.PRODUCT_SOLD_OUT_);
        }
        //3.获取用户id
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        //4.判断用户是否有购物车
        String shopCartStr = jedis.hget(SystemConstant.USER_SHOP_CART, user.getId().toString());
        ShopCart shopCart =null;
        if (StringUtils.isEmpty(shopCartStr)){
            //5.没有购物车  创建购物车 并添加商品
             shopCart = carateCart(product,user,count,productId);

        }else {
            //6.有购物车  判断商品是否已存在
            shopCart = JSONObject.parseObject(shopCartStr, ShopCart.class);
           boolean exist = false;
            List<CartInfo> list = shopCart.getList();
            for (CartInfo cartInfo : list) {
                if (cartInfo.getProductId()==productId){//判断商品是否存在
                    exist=true;
                    break;
                }
            }
            if (exist){
                //  a.商品在购物车存在   添加数量    判断是否数量是否为零  如果为零   把商品从购物车中移除
                for (CartInfo cartInfo : list) {
                    if (cartInfo.getProductId()==productId){
                       // 添加数量    判断是否数量是否为零
                        BigDecimal newCount = BigDecimalUtils.add(cartInfo.getCount().toString(), count.toString());
                        Integer countNew = Integer.valueOf(newCount.toString());
                        if (countNew==0){
                            //如果为零   把商品从购物车中移除
                            list.remove(cartInfo);
                            updateCart(count, shopCart, cartInfo);

                        }else {
                            //不为零继续添加
                            cartInfo.setCount(countNew);//商品数量
                            updateCart(count, shopCart,cartInfo);
                        }
                        break;
                    }
                }

            }else {
                //   b.如果不存在  往购物车中添加商品
                CartInfo cartInfo=new CartInfo();
                cartInfo.setCount(count);
                cartInfo.setProductName(product.getName());
                cartInfo.setPrice(product.getPrice().toString());
                cartInfo.setUserid(user.getId());
                cartInfo.setPhotoPath(product.getFilePath());
                cartInfo.setProductId(productId);
                cartInfo.setCountPrice("0");
                list.add(cartInfo);
                updateCart(count, shopCart, cartInfo);

            }
            //7.更新购物车  计算总件数  总价格

        }
        setShopCaet(jedis, user, shopCart);

        return ResponseServer.success();
    }


    private void updateCart(Integer count, ShopCart shopCart, CartInfo cartInfo) {
        //计算过的价格
        BigDecimal mul = BigDecimalUtils.mul(count.toString(), cartInfo.getPrice());

        BigDecimal newCountPrice = BigDecimalUtils.add(mul.toString(), cartInfo.getCountPrice());
        cartInfo.setCountPrice(newCountPrice.toString());//小计
        //总条数
        BigDecimal totalCount = BigDecimalUtils.add(shopCart.getTotalCount(), count.toString());
        shopCart.setTotalCount(totalCount.toString());
        //总价格
        BigDecimal totalPrice = BigDecimalUtils.add(shopCart.getTotalPrice(),mul.toString());
        shopCart.setTotalPrice(totalPrice.toString());
    }

    /**
     * 创建购物车
     * @return
     * @param product
     * @param user
     * @param count
     * @param productId
     */
    private ShopCart carateCart(Product product, User user, Integer count, Integer productId) {
        List<CartInfo> list= new ArrayList<>();
        ShopCart shopCart=new ShopCart();
        CartInfo cartInfo = new CartInfo();
        cartInfo.setCount(count);
        cartInfo.setProductName(product.getName());
        cartInfo.setPrice(product.getPrice().toString());
        cartInfo.setUserid(user.getId());
        cartInfo.setPhotoPath(product.getFilePath());
        cartInfo.setProductId(productId);
        cartInfo.setCountPrice(product.getPrice().toString());
        //更新商品
        list.add(cartInfo);
        //计算购物车商品总数
        shopCart.setTotalCount(cartInfo.getCount().toString());
        //计算购物车商品总价格
        shopCart.setTotalPrice(cartInfo.getPrice());
        //将购物车放入redis中
        shopCart.setList(list);
        return shopCart;
    }

    //8.把购物车放在redis中
    private void setShopCaet(Jedis jedis, User user, ShopCart shopCart) {
        String strShopCart = JSONObject.toJSONString(shopCart);
        jedis.hset(SystemConstant.USER_SHOP_CART, user.getId().toString(), strShopCart);
    }


    @Override
    public ResponseServer del(HttpServletRequest request, Integer productId) {
        User user = (User)request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        Jedis jedis = RedisUtil.getJedis();
        String hget = jedis.hget(SystemConstant.USER_SHOP_CART, user.getId().toString());
        ShopCart shopCart = JSONObject.parseObject(hget, ShopCart.class);
        List<CartInfo> list = shopCart.getList();
        for (CartInfo cartInfo : list) {
            if (cartInfo.getProductId()==productId){
                BigDecimal totalprice = BigDecimalUtils.sub(shopCart.getTotalPrice(), cartInfo.getCountPrice());
                shopCart.setTotalPrice(totalprice.toString());//总计减去小计
                BigDecimal totalCount = BigDecimalUtils.sub(shopCart.getTotalCount(), cartInfo.getCount().toString());
                shopCart.setTotalCount(totalCount.toString());
                list.remove(cartInfo);
                break;
            }
        }
        setShopCaet(jedis,user,shopCart);
        return ResponseServer.success();
    }
}
