package com.fh.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.commons.ResponseServer;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.ConsigneeInfo;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.paylog.mapper.PayLogMapper;
import com.fh.paylog.model.PayLog;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PayLogMapper payLogMapper;

    @Override
    public ResponseServer queryDirection(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", user.getId());
        List<ConsigneeInfo> list = orderMapper.selectList(queryWrapper);

        return ResponseServer.success(list);
    }

    @Override                          //地区id             支付类型        用户
    public ResponseServer addOrder(Integer consigneeId, Integer payType, User user) {
        //雪花算法 生成唯一Id
        long id = IdWorker.getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String orderId = simpleDateFormat.format(new Date());
        orderId += id;

        //从redis中取出用户的购物车
        Jedis redis = RedisUtil.getJedis();
        String shopCartStr = redis.hget(SystemConstant.USER_SHOP_CART, user.getId().toString());
        //判断是否有购物车
        if (StringUtils.isEmpty(shopCartStr)) {
            //没有直接返回
            return ResponseServer.error(SystemConstant.SHOPCART_IS_NULL);
        }

        //通过字符串获取购物车对象
        ShopCart shopCart = JSONObject.parseObject(shopCartStr, ShopCart.class);
        //获取购物车商品集合
        List<CartInfo> list = shopCart.getList();
        //判断购物车中是否有商品
        if (list.size() == 0) {
            //没有直接返回
            return ResponseServer.error(SystemConstant.SHOPCART_IS_NULL);
        }
        //用来存储库存不够的集合
        List<CartInfo> cartList = new ArrayList<>();
        //订单的 总价格 全局的
        BigDecimal totalPrice = new BigDecimal(0);
        //订单的 总数量
        Long totalCount = 0L;
        for (CartInfo cartInfo : list) {
            //查出当前商品的库存
            Product product = orderMapper.queryListById(cartInfo.getProductId());
            //当库存大于当前订单的数量时才创建订单详情
            if (product.getReserve() >= cartInfo.getCount()) {
                //创建订单详情
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setCount(Long.valueOf(cartInfo.getCount()));//数量
                orderInfo.setOrderId(orderId);                        //订单id
                orderInfo.setPhotoPath(cartInfo.getPhotoPath());       //商品图片
                orderInfo.setProductName(cartInfo.getProductName());   //商品名称
                orderInfo.setCountPrice(new BigDecimal(cartInfo.getCountPrice()));//商品小计
                orderInfo.setPrice(new BigDecimal(cartInfo.getPrice()));    //商品价格
                orderInfo.setProductId(cartInfo.getProductId());            //商品id
                //将订单添加数据库时先去减掉库存  同时判断订购数量要小于库存
                Long aLong = orderMapper.updateProductByReserve(orderInfo.getProductId(), orderInfo.getCount());
                if (aLong == 0) { //==0时 更新失败  ==1时  更新成功
                    //库存不足
                    cartList.add(cartInfo);
                } else {
                    //库存足够  添加订单 并在循环中 计算 总价格 和数量
                    totalPrice = BigDecimalUtils.add(totalPrice.toString(), cartInfo.getCountPrice());
                    //总数量
                    totalCount += orderInfo.getCount();
                    orderMapper.addOederInfo(orderInfo);  //将订单详情添加至数据库
                }
            } else {
                //否则就将此商品暂存库存不够的集合
                cartList.add(cartInfo);
            }
        }
        //生成订单  如果订单详情为空  就没有必要 生成订单
        if (cartList.size() == list.size()) {
            return ResponseServer.error(SystemConstant.ALLPRODUCT_IS_NULL); //返回状态
        }
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setOrderId(orderId);
        order.setConsignee(consigneeId);
        order.setPayType(payType);
        order.setStatus(SystemConstant.ORDER_PAY_STATUS);
        order.setTotalCount(totalCount);
        order.setTotalPrice(totalPrice);
        order.setUserId(user.getId().toString());
        orderMapper.addOrder(order);
        if (list.size() == 0) {
            redis.hdel(SystemConstant.USER_SHOP_CART, user.getId().toString());
        } else {
            //计算购物车剩余 的 商品总价 和 商品数量
            String totalCount1 = shopCart.getTotalCount();
            String totalPrice1 = shopCart.getTotalPrice();
            //购物车  原先总数量减去 提交的订单总数量
            BigDecimal subCount = BigDecimalUtils.sub(totalCount1, totalCount.toString());
            shopCart.setTotalCount(subCount.toString());
            //购物车  原先价格减去 提交的订单总价格
            BigDecimal subPrice = BigDecimalUtils.sub(totalPrice1, totalPrice.toString());
            shopCart.setTotalPrice(subPrice.toString());
            shopCart.setList(cartList);
            String string = JSONObject.toJSONString(shopCart);
            //创建支付日志记录
            PayLog payLog  = new PayLog();
            payLog.setCreateDate(new Date());
            payLog.setOrderId(orderId);
            payLog.setOut_trade_no(IdWorker.getId()+"");
            payLog.setPayStatus(SystemConstant.PAY_WAITING);
            payLog.setPayType(payType);  //支付类型
            payLog.setTotalPrice(totalPrice);
            payLog.setUserId(user.getId());
            payLogMapper.insert(payLog);
            //将信息存入redis中方便取
            String payLogString = JSONObject.toJSONString(payLog);
            redis.set(SystemConstant.PAY_LOG_KEY + user.getId(), payLogString);
            //更新购物车
            redis.hset(SystemConstant.USER_SHOP_CART,user.getId().toString() , string);
        }
        return ResponseServer.success(cartList);
    }

    @Override
    public ResponseServer addConsigneeinfo(ConsigneeInfo consigneeInfo, User user) {
        consigneeInfo.setUserId(user.getId());
        Integer defaultArea = consigneeInfo.getDefaultArea();
        if (defaultArea==1){ //1 为设为默认地址
            //设为2 清除所有的默认地址
            orderMapper.updateIsDefaultArea(defaultArea+1);
        }
        orderMapper.insert(consigneeInfo);
        return ResponseServer.success();
    }
}
