package org.jeecg.modules.common.service.impl;

import org.jeecg.modules.common.entity.FiDbset;
import org.jeecg.modules.common.mapper.FiDbSetMapper;
import org.jeecg.modules.common.service.FiDbSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhengbo
 * @date 2022-03-18
 */
@Component
public class FiDbSetServiceImpl implements FiDbSetService {
@Autowired
    FiDbSetMapper mapper;

    @Override
    public String selectDbKeyByType(String dbType) {
        return mapper.selectDbKeyByType(dbType);
    }


    @Override
    public FiDbset selectDbKeyById(String dbType) {
        return mapper.selectDbKeyById(dbType);
    }
}
