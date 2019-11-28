package com.fh.shopcart.biz;

import com.fh.commons.ResponseServer;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    ResponseServer sendCart(HttpServletRequest request, Integer count, Integer productId);

    ResponseServer del(HttpServletRequest request, Integer productId);
}
