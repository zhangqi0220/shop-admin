package com.fh.service.roleresource;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.mapper.RoleResourcesMapper;
import com.fh.model.Page;
import com.fh.model.ResourceInfo;
import com.fh.model.Role;
import com.fh.model.User;
import com.fh.util.RedisUtil;
import com.fh.util.SessionUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleResourceServiceImpl implements RoleResourceService {
    @Autowired
    private RoleResourcesMapper roleResourcesMapper;
    @Override
    public ServerResponse queryRoleList(Page page) {
        Integer totalCount = roleResourcesMapper.queryRoleCount();
        List<Role> list =roleResourcesMapper.queryRoleList(page);
        Map map = new HashMap();
        map.put("draw",page.getDraw());
        map.put("recordsTotal",totalCount);
        map.put("recordsFiltered",totalCount);
        map.put("data",list);
        return ServerResponse.success(map);
    }

    @Override
    public List<ResourceInfo> queryAllResource() {

        return roleResourcesMapper.queryAllResource();
    }

    /**
     * 增加角色 及其角色对应权限
     * @param idList
     * @param roleName
     * @return
     */
    @Override
    public ServerResponse addRole(List idList, String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        //增加角色
        roleResourcesMapper.addRole(role);
        //增加角色对应权限
        Map map = new HashMap();
        map.put("list", idList);
        map.put("roleId", role.getId());
        roleResourcesMapper.addRoleResource(map);
        return ServerResponse.success();
    }

    @Override
    public List<ResourceInfo> queryMyResource(Integer id) {
        //查询所有的权限
        List<ResourceInfo> list = roleResourcesMapper.queryAllResource();
        //查询我拥有的权限
        List<Integer> myList = roleResourcesMapper.queryMyResource(id);
        //lambda表达式
        list.stream().filter(x->{
            //查看你当前的权限中是否包含全部集合中的当前Id
            if (myList.contains(x.getId())){
                //如果有设置为true
                x.setChecked(true);
            }
            return  true;
            //将stream流转换为list
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public ServerResponse updateRole(List idList, Role role) {
        roleResourcesMapper.updateRole(role);
        //先删除 后增加
        roleResourcesMapper.deleteById(role.getId());
        //增加角色对应权限
        Map map = new HashMap();
        map.put("list", idList);
        map.put("roleId", role.getId());
        roleResourcesMapper.addRoleResource(map);

        return ServerResponse.success();
    }

    @Override
    public ServerResponse addResource(ResourceInfo resourceInfo) {
        roleResourcesMapper.addResource(resourceInfo);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateResource(ResourceInfo resourceInfo) {
        roleResourcesMapper.updateResource(resourceInfo);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteResource(List idList) {
        roleResourcesMapper.deleteResource(idList);
        return ServerResponse.success();
    }

    @Override
    public  List<Role>  queryUserRole(HttpServletRequest request , HttpServletResponse response) {
        Jedis jedis = RedisUtil.getJedis();
        List<Role> roles=null;
        String sessionId = SessionUtil.getSessionId(request, response);
        if (StringUtils.isNotBlank(sessionId)){
            String strUser = jedis.get(SystemConstant.LOGGIN_CURRENT_USER + sessionId);
            User user = JSONObject.parseObject(strUser, User.class);
            roles = roleResourcesMapper.queryUserRole(user.getId());
        }
        return roles;
    }
    @Override
    public  List<Role>  selectUserRole(Integer id) {
        return roleResourcesMapper.queryUserRole(id);
    }


}
