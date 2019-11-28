package com.fh.product.biz;

import com.fh.commons.ResponseServer;
import com.fh.product.model.Product;
import com.fh.param.ProductSearchParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gy on 2019/10/14.
 */
public interface ProductService {
    List<Product> queryList(ProductSearchParam page);


    List getBrandList();

    long queryCount(ProductSearchParam productSearchParam);

    List queryMapList(ProductSearchParam productSearchParam);


    ResponseServer queryHotProductList(Integer number, String timeName);

    ResponseServer queryHotBrandList(Integer number, String timeName);

    ResponseServer queryHotBrandListByBrandId(Integer brandId);
}
