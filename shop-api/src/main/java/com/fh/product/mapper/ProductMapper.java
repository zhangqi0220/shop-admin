package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.product.model.Brand;
import com.fh.product.model.Product;
import com.fh.param.ProductSearchParam;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by gy on 2019/10/14.
 */
public interface ProductMapper extends BaseMapper<Product> {
    List<Product> queryList(ProductSearchParam productSearchParam);

    List getBrandList();

    long queryCount(ProductSearchParam productSearchParam);

    List queryMapList(ProductSearchParam productSearchParam);

    List queryListNoPage(ProductSearchParam productSearchParam);

    Integer getBrandIdByBrandName(String brandName);

    void addBrand(Brand brand);

    void addProductList(List<Product> list);

    List<Map> queryHotProductList(@Param("num") Integer num,@Param("timeName")String timeName);

    List<Map> queryHotBrandList(@Param("num") Integer num,@Param("timeName")String timeName);

    List<Product> queryHotBrandListByBrandId(Integer brandId);
}
