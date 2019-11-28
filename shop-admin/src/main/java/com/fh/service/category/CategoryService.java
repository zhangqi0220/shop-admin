package com.fh.service.category;

import com.fh.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryListByPid(Integer pid);

    List<Category> queryList();

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(List idList);
}
