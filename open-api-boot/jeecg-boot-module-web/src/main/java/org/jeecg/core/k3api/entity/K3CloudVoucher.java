package org.jeecg.core.k3api.entity;

import lombok.Data;

/**
 * @author zhengbo
 * @date 2022-04-10
 */
@Data
public class K3CloudVoucher {

    private String IsDeleteEntry="true";
    private String[] NeedUpDateFields={};
    private String[] NeedReturnFields={};
    private String subSystemId="";
    private String isVerifyBaseDataField="false";
    private String isEntryBatchFill="true";
    private String validateFlag="true";
    private String numberSearch="true";
    private String isAutoAdjustField="false";
    private String interationFlags="false";
    private String ignoreInterationFlag="false";
    private String isControlPrecision="false";
    private VoucherModel model;


}
