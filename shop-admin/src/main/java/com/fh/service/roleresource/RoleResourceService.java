package com.fh.service.roleresource;

import com.fh.common.ServerResponse;
import com.fh.model.Page;
import com.fh.model.ResourceInfo;
import com.fh.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RoleResourceService {
    ServerResponse queryRoleList(Page page);

    List<ResourceInfo> queryAllResource();

    ServerResponse addRole(List idList, String roleName);

    List<ResourceInfo> queryMyResource(Integer id);

    ServerResponse updateRole(List idList, Role role);

    ServerResponse addResource(ResourceInfo resourceInfo);

    ServerResponse updateResource(ResourceInfo resourceInfo);

    ServerResponse deleteResource(List idList);

    List<Role> queryUserRole(HttpServletRequest request, HttpServletResponse response);

    List<Role> selectUserRole(Integer id);
}
