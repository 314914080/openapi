package org.jeecg.modules.fi.setting.service;

import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 规则设置明细
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
public interface IFiContrastSetdetailService extends IService<FiContrastSetdetail> {

	public List<FiContrastSetdetail> selectByMainId(String mainId);
}
