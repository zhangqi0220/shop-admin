package com.fh.category.biz;

import com.fh.category.model.Category;


import java.util.List;

public interface CategoryService {
    List<Category> queryListByPid(Integer pid);

    List<Category> queryList();

}
