package org.jeecg.modules.common.service;

import org.jeecg.modules.common.entity.FiDbset;

/**
 * @author zhengbo
 * @date 2022-03-18
 */
public interface FiDbSetService  {

    String selectDbKeyByType(String dbType);
    FiDbset selectDbKeyById( String dbType);
}
