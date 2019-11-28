package com.fh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("jump")
public class jumpJsp {

    @RequestMapping("toadd")
    public String toadd(){
        return "/demo/add.jsp";
    }

    @RequestMapping("toArea")
    public String toArea(){
        return "bootstrap/area";
    }
    @RequestMapping("demo8")
    public String demo8(){
        return "bootstrap/demo8";
    }
    @RequestMapping("toUser")
    public String update(){
        return "user/list";
    }
    @RequestMapping("toRole")
    public String toRole(){
        return "bootstrap/role";
    }
    @RequestMapping("toResource")
    public String toResource(){
        return "bootstrap/resource";
    }

    @RequestMapping("ztree")
    public String ztree(){
        return "bootstrap/ztree";
    }

    @RequestMapping("log")
    public String log(){
        return "log/log";
    }




}
