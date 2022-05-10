package org.jeecg.modules.fi.setting.service;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 贷方科目设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
public interface IFiImReulsConfigDdetailService extends IService<FiImReulsConfigDdetail> {

	public List<FiImReulsConfigDdetail> selectByMainId(String mainId);
}
