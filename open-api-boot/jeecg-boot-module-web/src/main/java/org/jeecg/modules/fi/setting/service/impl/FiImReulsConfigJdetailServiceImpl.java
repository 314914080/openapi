package org.jeecg.modules.fi.setting.service.impl;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfigJdetail;
import org.jeecg.modules.fi.setting.mapper.FiImReulsConfigJdetailMapper;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigJdetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 借方科目设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Service
public class FiImReulsConfigJdetailServiceImpl extends ServiceImpl<FiImReulsConfigJdetailMapper, FiImReulsConfigJdetail> implements IFiImReulsConfigJdetailService {
	
	@Autowired
	private FiImReulsConfigJdetailMapper fiImReulsConfigJdetailMapper;
	
	@Override
	public List<FiImReulsConfigJdetail> selectByMainId(String mainId) {
		return fiImReulsConfigJdetailMapper.selectByMainId(mainId);
	}
}
