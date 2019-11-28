package com.fh.paylog.controller;

import com.fh.api.resolver.MemberGain;
import com.fh.commons.Repetitive;
import com.fh.commons.ResponseServer;
import com.fh.paylog.biz.PayLogService;
import com.fh.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payLog")
@Api(value = "PayLogController")
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    /**
     * 下订单
     * @param user
     * @return
     */
    @ApiOperation(value = "下订单",httpMethod = "POST")
    @RequestMapping("getPayNum")
    public ResponseServer getPayNum (@MemberGain User user){
        return payLogService.getPayNum (user);
    }

    @ApiOperation(value = "查询订单支付状态",httpMethod = "GET")
    @RequestMapping("queryOrderStatus")
    public ResponseServer queryOrderStatus(@MemberGain User user){
        return payLogService.seeOrderStatus(user);
    }
}
