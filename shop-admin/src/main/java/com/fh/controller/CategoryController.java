package com.fh.controller;

import com.alibaba.fastjson.JSONArray;
import com.fh.common.ServerResponse;
import com.fh.model.Category;
import com.fh.service.category.CategoryService;
import com.fh.util.RedisLocker;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
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
    @ResponseBody
    public ServerResponse queryListByPid(Integer pid){

        List<Category> list = categoryService.queryListByPid( pid);
        return ServerResponse.success(list);
    }

    @RequestMapping("queryList")
    @ResponseBody
    public List<Category> queryList(){
        List<Category> list= categoryService.queryList();
        return list;

    }

    @RequestMapping("/addCategory")
    @ResponseBody
    public Map addCategory(Category category){
        Map map = new HashMap();
        try {
            categoryService.addCategory(category);//增加后返回id
            //返回true 为取得锁  false为
            boolean acquired = RedisLocker.isAcquired(SystemConstant.CATEGORY_LOCK, 5000);
            if (acquired){
                List<Category> list= categoryService.queryList();
                String string = JSONArray.toJSONString(list);
                Jedis jedis = RedisUtil.getJedis();
                jedis.set(SystemConstant.CATEGORYLIST,string);
            }
            map.put("status",200);
            map.put("id",category.getId());

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",999);
        }
        return map;
    }

    @RequestMapping("/deleteCategory")
    @ResponseBody
    public Map deleteCategory(@RequestParam("idList[]") List idList){
        Map map = new HashMap();
        try {
            categoryService.deleteCategory(idList);
            map.put("status",200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",999);
        }
        return map;
    }
    @RequestMapping("/getCategoryByPid")
    @ResponseBody
    public List getCategoryByPid(Integer pid){

        List list =   categoryService.queryListByPid(pid);

        return list;
    }
    @RequestMapping("/updateCategory")
    @ResponseBody
    public Map updateCategory(Category category){
        Map map = new HashMap();
        try {
            categoryService.updateCategory(category);
            map.put("status",200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",999);
        }
        return map;
    }

}
