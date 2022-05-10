package org.jeecg.modules.fi.setting.service;

import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import org.jeecg.modules.fi.setting.entity.FiContrastSet;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 对应设置
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
public interface IFiContrastSetService extends IService<FiContrastSet> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(FiContrastSet fiContrastSet, List<FiContrastSetdetail> fiContrastSetdetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(FiContrastSet fiContrastSet, List<FiContrastSetdetail> fiContrastSetdetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
