package com.fh.log.biz;

import com.fh.commons.ResponseServer;
import com.fh.log.mapper.LogMapper;
import com.fh.log.model.Log;
import com.fh.param.LogSearchParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper logMapper;

    @Override
    public void addLog(Log log) {
        logMapper.addLog(log);
    }

    @Override
    public ResponseServer queryList(LogSearchParam logSearchParam) {
        long totalCount = logMapper.queryCount(logSearchParam);
        List<Log> list = logMapper.queryList(logSearchParam);
        Map map = new HashMap();
        map.put("draw",logSearchParam.getDraw());
        map.put("recordsTotal",totalCount);
        map.put("recordsFiltered",totalCount);
        map.put("data",list);
        return ResponseServer.success(map);
    }
}
