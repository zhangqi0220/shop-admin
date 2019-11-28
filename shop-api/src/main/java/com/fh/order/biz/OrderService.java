package com.fh.order.biz;

import com.fh.commons.ResponseServer;
import com.fh.order.model.ConsigneeInfo;
import com.fh.user.model.User;

public interface OrderService {
    ResponseServer queryDirection(User user);

    ResponseServer addOrder(Integer consigneeId, Integer payType, User user);

    ResponseServer addConsigneeinfo(ConsigneeInfo consigneeInfo, User user);
}
