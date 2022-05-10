package org.jeecg.modules.fi.setting.mapper;

import java.util.List;
import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 规则设置明细
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
public interface FiContrastSetdetailMapper extends BaseMapper<FiContrastSetdetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<FiContrastSetdetail> selectByMainId(@Param("mainId") String mainId);
}
