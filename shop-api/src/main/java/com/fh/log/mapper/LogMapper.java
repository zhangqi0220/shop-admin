package com.fh.log.mapper;

import com.fh.log.model.Log;
import com.fh.param.LogSearchParam;

import java.util.List;

public interface LogMapper {
    void addLog(Log log);

    long queryCount(LogSearchParam logSearchParam);

    List<Log> queryList(LogSearchParam logSearchParam);
}
