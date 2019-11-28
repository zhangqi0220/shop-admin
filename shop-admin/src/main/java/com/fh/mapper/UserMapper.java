package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Area;
import com.fh.model.ResourceInfo;
import com.fh.model.User;
import com.fh.param.UserSearchParam;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User>{

    User getUserByName(String userName);

    List<User> queryList(UserSearchParam userSearchParam);

    void addUser(User user);

    long queryCount(UserSearchParam userSearchParam);

    List<ResourceInfo> queryUserResource(Integer id);

    List<Area> queryArea(Integer id);

    List<Area> queryAreaList();

    void addArea(Area area);

    void updateArea(Area area);

    void deleteAreas(List idList);

    List<ResourceInfo> queryUserResourceByType(Integer id);

    void addUserRole(Map map);

    void updateUser(User user);

    void deleteUserRole(Integer id);
}
