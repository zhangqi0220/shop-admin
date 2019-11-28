package com.fh.log.controller;

import com.fh.commons.ResponseServer;
import com.fh.param.LogSearchParam;
import com.fh.log.biz.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("log/")
public class LogController {
    @Resource
    private LogService logService;
    @RequestMapping("queryList")
    @ResponseBody
    public ResponseServer queryList(LogSearchParam logSearchParam){
        return logService.queryList(logSearchParam);
    }
}
