package com.ch.log;

import com.ch.dao.OperateLogDao;
import com.ch.model.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component("defaultOperateLogReporter")
public class OperateLogDBReporter implements  OperateLogReporter{

    @Autowired
    private OperateLogDao operateLogDao;
    @Override
    public void report(OperateLog log) {
        operateLogDao.insert(log);
    }
}
