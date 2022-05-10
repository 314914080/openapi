package org.jeecg.modules.fi.setting.mapper;

import java.util.List;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 贷方科目设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
public interface FiImReulsConfigDdetailMapper extends BaseMapper<FiImReulsConfigDdetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<FiImReulsConfigDdetail> selectByMainId(@Param("mainId") String mainId);
}
