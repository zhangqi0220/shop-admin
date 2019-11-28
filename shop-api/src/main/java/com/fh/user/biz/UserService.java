package com.fh.user.biz;

import com.fh.commons.ResponseServer;
import com.fh.user.model.User;
import com.nimbusds.jose.JOSEException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {
    //ResponseServer getUserByName(String userName);
    boolean queryUserByName(String userName);

    ResponseServer addUser(User user, String phoneCode);

    ResponseServer login(User user, HttpServletRequest request, HttpServletResponse response, String photoCode) throws JOSEException;

    ResponseServer getMes(String phone) throws IOException;
}
