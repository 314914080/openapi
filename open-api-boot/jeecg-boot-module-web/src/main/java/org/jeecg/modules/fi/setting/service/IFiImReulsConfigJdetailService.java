package org.jeecg.modules.fi.setting.service;

import org.jeecg.modules.fi.setting.entity.FiImReulsConfigJdetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 借方科目设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
public interface IFiImReulsConfigJdetailService extends IService<FiImReulsConfigJdetail> {

	public List<FiImReulsConfigJdetail> selectByMainId(String mainId);
}
