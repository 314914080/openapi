package org.jeecg.modules.fi.setting.service.impl;

import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import org.jeecg.modules.fi.setting.mapper.FiContrastSetdetailMapper;
import org.jeecg.modules.fi.setting.service.IFiContrastSetdetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 规则设置明细
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@Service
public class FiContrastSetdetailServiceImpl extends ServiceImpl<FiContrastSetdetailMapper, FiContrastSetdetail> implements IFiContrastSetdetailService {
	
	@Autowired
	private FiContrastSetdetailMapper fiContrastSetdetailMapper;
	
	@Override
	public List<FiContrastSetdetail> selectByMainId(String mainId) {
		return fiContrastSetdetailMapper.selectByMainId(mainId);
	}
}
