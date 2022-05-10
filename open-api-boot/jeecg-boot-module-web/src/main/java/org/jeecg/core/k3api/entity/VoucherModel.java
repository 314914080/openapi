package org.jeecg.core.k3api.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author zhengbo
 * @date 2022-04-13
 */
@Data
public class VoucherModel {

    private JSONObject fAccountBookID;//账簿
    private String  fDate;//日期
    private String fBUSDATE;

    private JSONObject fVOUCHERGROUPID;//凭证字

    private  String fVOUCHERGROUPNO;//凭证号 -不用赋值自动会填写

    private boolean fISADJUSTVOUCHER;

    private String fDocumentStatus;//审核状态-Z

    private List<VoucherFEntity>  fEntity; //借贷方科目



}
