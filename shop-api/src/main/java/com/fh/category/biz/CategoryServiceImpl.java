package com.fh.category.biz;

import com.fh.category.mapper.CategoryMapper;
import com.fh.category.model.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryListByPid(Integer pid) {
        return categoryMapper.queryListByPid(pid);
    }

    @Override
    public List<Category> queryList() {
        return categoryMapper.queryList();
    }


}
