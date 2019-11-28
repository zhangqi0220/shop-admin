package com.fh.shopcart.model;

import java.util.ArrayList;
import java.util.List;

public class ShopCart {
    private String totalCount;
    private String totalPrice;
    private List<CartInfo> list =new ArrayList<>();

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartInfo> getList() {
        return list;
    }

    public void setList(List<CartInfo> list) {
        this.list = list;
    }
}
