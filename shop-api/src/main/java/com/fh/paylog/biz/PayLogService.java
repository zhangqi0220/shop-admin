package com.fh.paylog.biz;

import com.fh.commons.ResponseServer;
import com.fh.user.model.User;

public interface PayLogService {
    ResponseServer getPayNum(User user);

    ResponseServer seeOrderStatus( User user);
}
