package com.fh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.order.model.ConsigneeInfo;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<ConsigneeInfo> {

    Long updateProductByReserve(@Param("productId") Integer productId, @Param("count") Long count);

    Product queryListById(Integer productId);

    void addOederInfo(OrderInfo orderInfo);

    void addOrder(Order order);

    void updateIsDefaultArea(Integer defaultArea);

    void updateOrderStatus( @Param("orderId") String orderId , @Param("paySuccess")int paySuccess);
}
