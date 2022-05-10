package org.jeecg.modules.fi.importcenter.service;

import org.jeecg.modules.fi.importcenter.entity.ImportReq;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhengbo
 * @date 2022-04-08
 */
public interface VImportCenterService {




    public void executeImport(ImportReq importReq);

}
