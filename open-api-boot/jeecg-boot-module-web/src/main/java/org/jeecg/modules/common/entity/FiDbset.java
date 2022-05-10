package org.jeecg.modules.common.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 数据账套管理
 * @Author: jeecg-boot
 * @Date:   2022-04-10
 * @Version: V1.0
 */
@Data
@TableName("fi_dbset")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fi_dbset对象", description="数据账套管理")
public class FiDbset implements Serializable {
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
	/**账套名称*/
	@Excel(name = "账套名称", width = 15)
    @ApiModelProperty(value = "账套名称")
    private String dbname;
	/**账套类型*/
	@Excel(name = "账套类型", width = 15, dicCode = "fi_db_type")
	@Dict(dicCode = "fi_db_type")
    @ApiModelProperty(value = "账套类型")
    private String dbtype;
	/**财务软件版本*/
	@Excel(name = "财务软件版本", width = 15, dicCode = "fi_v_type")
	@Dict(dicCode = "fi_v_type")
    @ApiModelProperty(value = "财务软件版本")
    private String fitype;
	/**数据源类型*/
	@Excel(name = "数据源类型", width = 15, dicCode = "fi_api_type")
	@Dict(dicCode = "fi_api_type")
    @ApiModelProperty(value = "数据源类型")
    private String apitype;
	/**数据源*/
	@Excel(name = "数据源", width = 15, dictTable = "sys_data_source", dicText = "name", dicCode = "code")
	@Dict(dictTable = "sys_data_source", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "数据源")
    private String dbcode;
	/**api接口*/
	@Excel(name = "api接口", width = 15)
    @ApiModelProperty(value = "api接口")
    private String apicode;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
}
