package com.fh.category.mapper;

import com.fh.category.model.Category;

import java.util.List;

public interface CategoryMapper {

    List<Category> queryListByPid(Integer pid);

    List<Category> queryList();

}
