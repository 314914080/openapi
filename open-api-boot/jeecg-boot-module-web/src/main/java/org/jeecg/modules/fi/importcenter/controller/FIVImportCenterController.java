package org.jeecg.modules.fi.importcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.fi.importcenter.entity.ImportReq;
import org.jeecg.modules.fi.importcenter.service.VImportCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengbo
 * @date 2022-04-08
 */
@Api(tags="凭证导入中心")
@RestController
@RequestMapping("/importCenter/VImport")
@Slf4j
public class FIVImportCenterController {


    @Autowired
    VImportCenterService vImportCenterService;


    /**
     *   添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "凭证导入-执行")
    @ApiOperation(value="凭证导入-执行", notes="凭证导入-执行")
    @PostMapping(value = "/execute")
    public Result<?> add(@RequestBody ImportReq importReq) {

        try {
            vImportCenterService.executeImport(importReq);

        }catch (Exception e){
            return Result.OK("执行错误:"+e.getMessage());
        }finally {

        }

        return Result.OK("添加成功！");
    }


}
