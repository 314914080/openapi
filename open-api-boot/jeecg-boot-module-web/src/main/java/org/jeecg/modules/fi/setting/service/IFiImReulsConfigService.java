package org.jeecg.modules.fi.setting.service;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfigJdetail;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 凭证导入规则设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
public interface IFiImReulsConfigService extends IService<FiImReulsConfig> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(FiImReulsConfig fiImReulsConfig, List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList, List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(FiImReulsConfig fiImReulsConfig, List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList, List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
