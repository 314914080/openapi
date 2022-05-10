package org.jeecg.modules.fi.setting.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 凭证导入规则设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@ApiModel(value="fi_im_reuls_config对象", description="凭证导入规则设置")
@Data
@TableName("fi_im_reuls_config")
public class FiImReulsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**规则代码*/
	@Excel(name = "规则代码", width = 15)
    @ApiModelProperty(value = "规则代码")
    private String code;
	/**规则描述*/
	@Excel(name = "规则描述", width = 15)
    @ApiModelProperty(value = "规则描述")
    private String name;
	/**ERP账套*/
	@Excel(name = "ERP账套", width = 15, dictTable = "fi_dbset", dicText = "dbname", dicCode = "id")
    @Dict(dictTable = "fi_dbset", dicText = "dbname", dicCode = "id")
    @ApiModelProperty(value = "ERP账套")
    private String erpDbType;
	/**财务账套*/
	@Excel(name = "财务账套", width = 15, dictTable = "fi_dbset", dicText = "dbname", dicCode = "id")
    @Dict(dictTable = "fi_dbset", dicText = "dbname", dicCode = "id")
    @ApiModelProperty(value = "财务账套")
    private String fiDbType;
	/**合并规则*/
	@Excel(name = "合并规则", width = 15, dicCode = "fi_vouchermerge_type")
    @Dict(dicCode = "fi_vouchermerge_type")
    @ApiModelProperty(value = "合并规则")
    private String fiMergeType;
	/**凭证字*/
	@Excel(name = "凭证字", width = 15)
    @ApiModelProperty(value = "凭证字")
    private String fiPz;
	/**凭证用户名*/
	@Excel(name = "凭证用户名", width = 15)
    @ApiModelProperty(value = "凭证用户名")
    private String fiUser;
	/**借方类型字段*/
	@Excel(name = "借方类型字段", width = 15)
    @ApiModelProperty(value = "借方类型字段")
    private String jfiled;
	/**贷方类型字段*/
	@Excel(name = "贷方类型字段", width = 15)
    @ApiModelProperty(value = "贷方类型字段")
    private String dfiled;
	/**查询类型(用于借贷方是否分开SQL查询)*/
	@Excel(name = "查询类型(用于借贷方是否分开SQL查询)", width = 15, dicCode = "fi_querydata_type")
    @Dict(dicCode = "fi_querydata_type")
    @ApiModelProperty(value = "查询类型(用于借贷方是否分开SQL查询)")
    private String sqltype;
	/**查询语句1*/
	@Excel(name = "查询语句1", width = 15)
    @ApiModelProperty(value = "查询语句1")
    private String sqltext1;
	/**查询语句2*/
	@Excel(name = "查询语句2", width = 15)
    @ApiModelProperty(value = "查询语句2")
    private String sqltext2;
	/**启用*/
	@Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private String status;
}
