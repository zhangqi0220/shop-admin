package com.fh.user.mapper;

import com.fh.user.model.User;
import com.fh.param.UserSearchParam;

import java.util.List;

public interface UserMapper{

    User queryUserByName(String userName);

    List<User> queryList(UserSearchParam userSearchParam);

    void addUser(User user);

    User getUserByPhoneNum(String phoneNum);
}
