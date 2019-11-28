package com.fh.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by gy on 2019/10/14.
 */
public class Product {
    private Integer id;         //商品id
    private String name;        //商品名称
    private Double price;       //价格
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date   createDate;
    private Integer brandId;   //品牌Id
    private String filePath;   //商品图片
    private Integer category1;//地区
    private Integer category2;
    private Integer category3;
    private String brandName;
    private Integer status; //上下架
    private Integer reserve;//库存

    public Integer getReserve() {
        return reserve;
    }

    public void setReserve(Integer reserve) {
        this.reserve = reserve;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCategory1() {
        return category1;
    }

    public void setCategory1(Integer category1) {
        this.category1 = category1;
    }

    public Integer getCategory2() {
        return category2;
    }

    public void setCategory2(Integer category2) {
        this.category2 = category2;
    }

    public Integer getCategory3() {
        return category3;
    }

    public void setCategory3(Integer category3) {
        this.category3 = category3;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Product() {
    }

    public Product(Integer id, String name, Double price, Date createDate, Integer brandId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createDate = createDate;
        this.brandId = brandId;
    }
}
