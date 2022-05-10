package org.jeecg.modules.fi.setting.service.impl;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import org.jeecg.modules.fi.setting.mapper.FiImReulsConfigDdetailMapper;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigDdetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 贷方科目设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Service
public class FiImReulsConfigDdetailServiceImpl extends ServiceImpl<FiImReulsConfigDdetailMapper, FiImReulsConfigDdetail> implements IFiImReulsConfigDdetailService {
	
	@Autowired
	private FiImReulsConfigDdetailMapper fiImReulsConfigDdetailMapper;
	
	@Override
	public List<FiImReulsConfigDdetail> selectByMainId(String mainId) {
		return fiImReulsConfigDdetailMapper.selectByMainId(mainId);
	}
}
