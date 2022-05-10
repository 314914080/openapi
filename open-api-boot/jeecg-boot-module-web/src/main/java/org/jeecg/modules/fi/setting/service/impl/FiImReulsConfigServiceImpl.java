package org.jeecg.modules.fi.setting.service.impl;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfig;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigJdetail;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import org.jeecg.modules.fi.setting.mapper.FiImReulsConfigJdetailMapper;
import org.jeecg.modules.fi.setting.mapper.FiImReulsConfigDdetailMapper;
import org.jeecg.modules.fi.setting.mapper.FiImReulsConfigMapper;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 凭证导入规则设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Service
public class FiImReulsConfigServiceImpl extends ServiceImpl<FiImReulsConfigMapper, FiImReulsConfig> implements IFiImReulsConfigService {

	@Autowired
	private FiImReulsConfigMapper fiImReulsConfigMapper;
	@Autowired
	private FiImReulsConfigJdetailMapper fiImReulsConfigJdetailMapper;
	@Autowired
	private FiImReulsConfigDdetailMapper fiImReulsConfigDdetailMapper;
	
	@Override
	@Transactional
	public void saveMain(FiImReulsConfig fiImReulsConfig, List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList,List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList) {
		fiImReulsConfigMapper.insert(fiImReulsConfig);
		if(fiImReulsConfigJdetailList!=null && fiImReulsConfigJdetailList.size()>0) {
			for(FiImReulsConfigJdetail entity:fiImReulsConfigJdetailList) {
				//外键设置
				entity.setMainid(fiImReulsConfig.getId());
				fiImReulsConfigJdetailMapper.insert(entity);
			}
		}
		if(fiImReulsConfigDdetailList!=null && fiImReulsConfigDdetailList.size()>0) {
			for(FiImReulsConfigDdetail entity:fiImReulsConfigDdetailList) {
				//外键设置
				entity.setMainid(fiImReulsConfig.getId());
				fiImReulsConfigDdetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(FiImReulsConfig fiImReulsConfig,List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList,List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList) {
		fiImReulsConfigMapper.updateById(fiImReulsConfig);
		
		//1.先删除子表数据
		fiImReulsConfigJdetailMapper.deleteByMainId(fiImReulsConfig.getId());
		fiImReulsConfigDdetailMapper.deleteByMainId(fiImReulsConfig.getId());
		
		//2.子表数据重新插入
		if(fiImReulsConfigJdetailList!=null && fiImReulsConfigJdetailList.size()>0) {
			for(FiImReulsConfigJdetail entity:fiImReulsConfigJdetailList) {
				//外键设置
				entity.setMainid(fiImReulsConfig.getId());
				fiImReulsConfigJdetailMapper.insert(entity);
			}
		}
		if(fiImReulsConfigDdetailList!=null && fiImReulsConfigDdetailList.size()>0) {
			for(FiImReulsConfigDdetail entity:fiImReulsConfigDdetailList) {
				//外键设置
				entity.setMainid(fiImReulsConfig.getId());
				fiImReulsConfigDdetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		fiImReulsConfigJdetailMapper.deleteByMainId(id);
		fiImReulsConfigDdetailMapper.deleteByMainId(id);
		fiImReulsConfigMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			fiImReulsConfigJdetailMapper.deleteByMainId(id.toString());
			fiImReulsConfigDdetailMapper.deleteByMainId(id.toString());
			fiImReulsConfigMapper.deleteById(id);
		}
	}
	
}
