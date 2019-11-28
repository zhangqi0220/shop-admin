package com.fh.log.biz;

import com.fh.commons.ResponseServer;
import com.fh.log.model.Log;
import com.fh.param.LogSearchParam;

public interface LogService {
    void addLog(Log log);

    ResponseServer queryList(LogSearchParam logSearchParam);
}
