package com.fh.order.model;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_order_area")
public class ConsigneeInfo { //收件人

    private Integer id;
    private Integer userId;  //登录的用户
    private String name;    //收件人姓名
    private String area;     //详细地址
    private String phone;     //手机号码
    private String email;     //电子邮箱
    private String areaName; //地址别名
    private Integer defaultArea;//默认地址   1  其他为2

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(Integer defaultArea) {
        this.defaultArea = defaultArea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
