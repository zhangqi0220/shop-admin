package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Page;
import com.fh.model.ResourceInfo;
import com.fh.model.Role;

import java.util.List;
import java.util.Map;

public interface RoleResourcesMapper{
    Integer queryRoleCount();

    List<Role> queryRoleList(Page page);

    List<ResourceInfo> queryAllResource();

    int addRole(Role role);

    void addRoleResource(Map map);

    List<Integer> queryMyResource(Integer id);

    void deleteById(Integer id);

    void updateRole(Role role);

    void addResource(ResourceInfo resourceInfo);

    void updateResource(ResourceInfo resourceInfo);

    void deleteResource(List idList);

    List<Role> queryUserRole(Integer id);
}
