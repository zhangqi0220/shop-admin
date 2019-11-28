package com.fh.order.controller;

import com.fh.api.resolver.MemberGain;
import com.fh.commons.Repetitive;
import com.fh.commons.ResponseServer;
import com.fh.order.biz.OrderService;
import com.fh.order.model.ConsigneeInfo;
import com.fh.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order/")
@Api(value = "OrderController")

public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 查看收件人地址
     *
     * @return
     */
    @ApiOperation(value = "查看收件人地址",httpMethod = "GET")
    @GetMapping("queryDirection")
    public ResponseServer queryDirection(@MemberGain User user) {
        return orderService.queryDirection(user);
    }

    /**
     * 发起订单
     * @param consigneeId
     * @param payType
     * @param user
     * @return
     */
    @PostMapping("sendOrder")
    @ApiOperation(value = "发起订单",httpMethod = "POST")
    @Repetitive    //防止表单重复提交注解
    public ResponseServer sendOrder(Integer consigneeId, Integer payType, @MemberGain User user) {
        return orderService.addOrder(consigneeId, payType, user);
    }

    /**
     *
     * @return
     */
    @ApiOperation(value = "增叫地址",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "name",required = true,paramType = "add"),
            @ApiImplicitParam(value = "area",required = true,paramType = "add"),
            @ApiImplicitParam(value = "phone",required = true,paramType = "add"),
    })
    @PostMapping("addConsigneeinfo")
    public ResponseServer addConsigneeinfo(ConsigneeInfo consigneeInfo ,@MemberGain User user){
    return orderService.addConsigneeinfo(consigneeInfo,user);
    }


    @RequestMapping("test")
    @Repetitive
    public ResponseServer test(){
        return ResponseServer.success();
    }
}
