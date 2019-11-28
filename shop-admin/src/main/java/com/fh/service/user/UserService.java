package com.fh.service.user;

import com.fh.common.ServerResponse;
import com.fh.model.Area;
import com.fh.model.ResourceInfo;
import com.fh.model.User;
import com.fh.param.UserSearchParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    //ServerResponse getUserByName(String userName);
    boolean getUserByName(String userName);

    ServerResponse queryList(UserSearchParam userSearchParam);

    ServerResponse addUser(User user,List idList);

    ServerResponse login(User user, HttpServletRequest request, HttpServletResponse response);

    List<ResourceInfo> queryUserResource(Integer id);

    List<Area> queryArea(Integer id);

    ServerResponse queryAreaList();

    ServerResponse addArea(Area area);

    ServerResponse updateArea(Area area);

    ServerResponse deleteAreas(List idList);

    List<ResourceInfo> queryUserResourceByType(Integer id);

    ServerResponse updateUser(User user, List idList);

    ServerResponse getPhoneCode(String phoneNum) throws IOException;

    ServerResponse updatePassWord(String code, User user);
}
