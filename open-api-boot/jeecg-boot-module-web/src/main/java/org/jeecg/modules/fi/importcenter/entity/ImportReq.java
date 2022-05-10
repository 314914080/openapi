package org.jeecg.modules.fi.importcenter.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengbo
 * @date 2022-04-08
 */
@Data
public class ImportReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String  startDate;
    private String  endDate;
    private String  code;
    private String userId;
}
