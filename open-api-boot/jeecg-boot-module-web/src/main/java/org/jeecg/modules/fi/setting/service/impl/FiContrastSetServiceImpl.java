package org.jeecg.modules.fi.setting.service.impl;

import org.jeecg.modules.fi.setting.entity.FiContrastSet;
import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import org.jeecg.modules.fi.setting.mapper.FiContrastSetdetailMapper;
import org.jeecg.modules.fi.setting.mapper.FiContrastSetMapper;
import org.jeecg.modules.fi.setting.service.IFiContrastSetService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 对应设置
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@Service
public class FiContrastSetServiceImpl extends ServiceImpl<FiContrastSetMapper, FiContrastSet> implements IFiContrastSetService {

	@Autowired
	private FiContrastSetMapper fiContrastSetMapper;
	@Autowired
	private FiContrastSetdetailMapper fiContrastSetdetailMapper;
	
	@Override
	@Transactional
	public void saveMain(FiContrastSet fiContrastSet, List<FiContrastSetdetail> fiContrastSetdetailList) {
		fiContrastSetMapper.insert(fiContrastSet);
		if(fiContrastSetdetailList!=null && fiContrastSetdetailList.size()>0) {
			for(FiContrastSetdetail entity:fiContrastSetdetailList) {
				//外键设置
				entity.setMainId(fiContrastSet.getId());
				fiContrastSetdetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(FiContrastSet fiContrastSet,List<FiContrastSetdetail> fiContrastSetdetailList) {
		fiContrastSetMapper.updateById(fiContrastSet);

		
		//2.子表数据重新插入
		if(fiContrastSetdetailList!=null && fiContrastSetdetailList.size()>0) {
			//1.先删除子表数据
			//fiContrastSetdetailMapper.deleteByMainId(fiContrastSet.getId());
			for(FiContrastSetdetail entity:fiContrastSetdetailList) {
				fiContrastSetdetailMapper.deleteById(entity.getId());
				//外键设置
				entity.setMainId(fiContrastSet.getId());
				fiContrastSetdetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		fiContrastSetdetailMapper.deleteByMainId(id);
		fiContrastSetMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			fiContrastSetdetailMapper.deleteByMainId(id.toString());
			fiContrastSetMapper.deleteById(id);
		}
	}
	
}
