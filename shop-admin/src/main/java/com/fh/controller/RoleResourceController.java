package com.fh.controller;

import com.fh.common.ServerResponse;
import com.fh.model.Page;
import com.fh.model.ResourceInfo;
import com.fh.model.Role;
import com.fh.service.roleresource.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("role")
public class RoleResourceController {
    @Autowired
    private RoleResourceService roleResourceService;

    @RequestMapping("queryRoleList")
    @ResponseBody
    public ServerResponse queryRoleList(Page page) {
        return roleResourceService.queryRoleList(page);
    }

    @ResponseBody
    @RequestMapping("queryAllResource")
    public ServerResponse queryAllResource() {
        return ServerResponse.success(roleResourceService.queryAllResource());
    }

    @ResponseBody
    @RequestMapping("addRole")
    public ServerResponse addRole(@RequestParam("idList[]") List idList, String roleName) {

        return ServerResponse.success(roleResourceService.addRole(idList, roleName));
    }
    @ResponseBody
    @RequestMapping("updateRole")
    public ServerResponse updateRole(@RequestParam("idList[]") List idList, Role role) {

        return ServerResponse.success(roleResourceService.updateRole(idList, role));
    }

    @ResponseBody
    @RequestMapping("queryMyResource")
    public ServerResponse queryMyResource(Integer id) {

        return ServerResponse.success(roleResourceService.queryMyResource(id));
    }

    /**
     * 增加权限
     * @param resourceInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("addResource")
    public ServerResponse addResource( ResourceInfo resourceInfo) {

        return ServerResponse.success(roleResourceService.addResource(resourceInfo));
    }

    /**
     * 修改权限
     * @param resourceInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("updateResource")
    public ServerResponse updateResource(ResourceInfo resourceInfo) {

        return ServerResponse.success(roleResourceService.updateResource(resourceInfo));
    }
    @ResponseBody
    @RequestMapping("deleteResource")
    public ServerResponse deleteResource(@RequestParam("idList[]") List idList) {

        return ServerResponse.success(roleResourceService.deleteResource(idList));
    }

    /**
     * 查看自己的角色
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("queryUserRole")
    public ServerResponse queryUserRole(HttpServletRequest request, HttpServletResponse response) {
        List<Role> roles = roleResourceService.queryUserRole(request,response);
        return ServerResponse.success(roles);
    }

    /**
     * 查看别人的角色 方法写重了
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("selectUserRole")
    public ServerResponse selectUserRole(Integer id) {

        return ServerResponse.success(roleResourceService.selectUserRole(id));
    }




}
