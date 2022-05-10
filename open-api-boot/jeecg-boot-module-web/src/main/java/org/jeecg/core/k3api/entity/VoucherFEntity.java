package org.jeecg.core.k3api.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhengbo
 * @date 2022-04-13
 */
@Data
@JSONType(orders = {"fEXPLANATION","fACCOUNTID","fDetailID","fCURRENCYID","fEXCHANGERATETYPE","fDEBIT","fCREDIT","fAMOUNTFOR"})
public class VoucherFEntity {

    @JSONField(ordinal = 1)
    private String fEXPLANATION;//摘要

    @JSONField(ordinal = 2)
    private JSONObject fACCOUNTID;//科目

    @JSONField(ordinal = 3)
    private JSONObject fDetailID;//核算维度
    @JSONField(ordinal = 4)
    private  JSONObject fCURRENCYID;//币种

    @JSONField(ordinal = 5)
    private JSONObject fEXCHANGERATETYPE;//汇率类型

    @JSONField(ordinal = 6)
    private BigDecimal fDEBIT;//借方金额

    @JSONField(ordinal = 7)
    private BigDecimal fCREDIT;//贷方金额

    @JSONField(ordinal = 8)
    private BigDecimal fAMOUNTFOR;


    @JSONField(serialize=false)
    private String fDC;//凭证方向 1借方 -1是贷方
//    private String fQty;

}
