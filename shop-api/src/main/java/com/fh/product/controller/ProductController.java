package com.fh.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.fh.commons.Ignore;
import com.fh.commons.ResponseServer;
import com.fh.product.model.Product;
import com.fh.param.ProductSearchParam;
import com.fh.product.biz.ProductService;
import com.fh.utils.FileUtil;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by gy on 2019/10/14.
 */
@Controller
@Api(value = "ProductController")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/queryList")
    @ResponseBody
    @ApiOperation(value = "查商品")
    @Ignore
    public ResponseServer queryList(ProductSearchParam productSearchParam) {
        // Long totalCount = productService.queryTotalCount(productSearchParam);
        List<Product> list = null;
        Jedis jedis = RedisUtil.getJedis();
        String productStr = jedis.get(SystemConstant.PRODUCT_LIST);
        if (StringUtils.isEmpty(productStr)) {
            list = productService.queryList(productSearchParam);
            String string = JSONArray.toJSONString(list);
            jedis.set(SystemConstant.PRODUCT_LIST, string);
            jedis.expire(SystemConstant.PRODUCT_LIST, 50);
        } else {
            list = JSONArray.parseArray(productStr, Product.class);
        }

        return ResponseServer.success(list);
    }

    @RequestMapping("/getBrandList")
    @ResponseBody
    public ResponseServer getBrandList() {
        List list = productService.getBrandList();
        return ResponseServer.success(list);
    }

    @RequestMapping("/queryMapList")
    @ResponseBody
    public ResponseServer queryMapList(ProductSearchParam productSearchParam) {
        long totalCount = productService.queryCount(productSearchParam);
        List list = productService.queryMapList(productSearchParam);
        return ResponseServer.success(list);
    }

    /**
     *
     * 查询热销品牌的集合
     * @return
     */
    @RequestMapping("/queryHotBrandList")
    @ResponseBody
    @Ignore
    public ResponseServer queryHotBrandList(Integer number , String timeName) {
        //1  DAY 一天 WEEK 一周 3 MONTH 三月  1 YEAR 一年

        return productService.queryHotBrandList(number,timeName);
    }
    /**
     *
     * 查询热销商品的集合
     * @return
     */
    @RequestMapping("/queryHotProductList")
    @ResponseBody
    @Ignore
    public ResponseServer queryHotProductList(Integer number , String timeName) {
        //1  DAY 一天 WEEK 一周 3 MONTH 三月  1 YEAR 一年

        return productService.queryHotProductList(number,timeName);
    }
    /**
     *
     * 查询热销商品的集合
     * @return
     */
    @RequestMapping("/queryHotBrandListByBrandId")
    @ResponseBody
    @Ignore
    public ResponseServer queryHotBrandListByBrandId(Integer brandId) {
        //1  DAY 一天 WEEK 一周 3 MONTH 三月  1 YEAR 一年

        return productService.queryHotBrandListByBrandId(brandId);
    }


}


