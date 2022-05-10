package org.jeecg.modules.fi.importcenter.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 查询ERP 单据数据
 * @author zhengbo
 * @date 2022-04-10
 */
@Data
public class Order {

    private String orderDate;
    private String orderkey;
    private BigDecimal amount ;
    private BigDecimal qty;
    private String remark;
    private String groupkey;
    private HashMap<String, String> mapValue;


}
