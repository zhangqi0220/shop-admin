package com.fh.mapper;

import com.fh.model.Category;

import java.util.List;

public interface CategoryMapper {

    List<Category> queryListByPid(Integer pid);

    void addCategory(Category category);

    List<Category> queryList();

    void updateCategory(Category category);

    void deleteCategory(List list);
}
