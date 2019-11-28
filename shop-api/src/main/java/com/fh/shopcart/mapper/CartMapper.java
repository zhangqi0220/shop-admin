package com.fh.shopcart.mapper;

import com.fh.product.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper {
    Product queryListById(Integer id);
}
