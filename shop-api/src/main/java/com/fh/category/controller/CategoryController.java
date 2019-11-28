package com.fh.category.controller;

import com.alibaba.fastjson.JSONArray;
import com.fh.commons.Ignore;
import com.fh.commons.ResponseServer;
import com.fh.category.model.Category;
import com.fh.category.biz.CategoryService;
import com.fh.utils.RedisUtil;
import com.fh.utils.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
@RequestMapping("category/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据pid查询该节点下的所有子节点
     * @param pid
     * @return
     */
    @RequestMapping("queryListByPid")
    public ResponseServer queryListByPid(Integer pid){
        List<Category> list = categoryService.queryListByPid( pid);
        return ResponseServer.success(list);
    }

    @RequestMapping("queryList")
    @Ignore
    public ResponseServer queryList(){
        List<Category> list=null;
        Jedis jedis = RedisUtil.getJedis();
        String category = jedis.get(SystemConstant.CATEGORYLIST);
        if (StringUtils.isEmpty(category)){
            list= categoryService.queryList();
            String string = JSONArray.toJSONString(list);
            jedis.set(SystemConstant.CATEGORYLIST, string);
        }else {
           list = JSONArray.parseArray(category, Category.class);
        }

        return ResponseServer.success(list);
    }
}
