package org.jeecg.modules.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.FiDbset;

/**
 * @author zhengbo
 * @date 2022-03-18
 */
@Mapper
public interface FiDbSetMapper {



    String selectDbKeyByType(@Param("dbType") String dbType);

    FiDbset selectDbKeyById(@Param("id") String id);
}
