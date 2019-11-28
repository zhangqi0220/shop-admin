package com.fh.product.biz;

import com.fh.commons.ResponseServer;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Brand;
import com.fh.product.model.Product;
import com.fh.param.ProductSearchParam;
import com.fh.utils.SystemConstant;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gy on 2019/10/14.
 */
@Service
public class ProductServiceImpl  implements  ProductService{
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> queryList(ProductSearchParam productSearchParam) {
        return productMapper.queryList(productSearchParam);
    }


    @Override
    public List getBrandList() {
        return productMapper.getBrandList();
    }

    @Override
    public long queryCount(ProductSearchParam productSearchParam) {
        return productMapper.queryCount( productSearchParam);
    }

    @Override
    public List queryMapList(ProductSearchParam productSearchParam) {
        return productMapper.queryMapList(productSearchParam);
    }

    @Override
    public ResponseServer queryHotProductList(Integer number, String timeName) {
        List<Map> list = productMapper.queryHotProductList(number,timeName);
        return ResponseServer.success(list);
    }

    @Override
    public ResponseServer queryHotBrandList(Integer number, String timeName) {
        List<Map> list = productMapper.queryHotBrandList(number,timeName);
        return ResponseServer.success(list);
    }

    @Override
    public ResponseServer queryHotBrandListByBrandId(Integer brandId) {
        List<Product> list = productMapper.queryHotBrandListByBrandId(brandId);
        return ResponseServer.success(list);
    }


}
