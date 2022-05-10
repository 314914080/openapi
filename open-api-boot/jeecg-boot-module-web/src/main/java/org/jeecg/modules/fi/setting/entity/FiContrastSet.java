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
 * @Description: 对应设置
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@ApiModel(value="fi_contrast_set对象", description="对应设置")
@Data
@TableName("fi_contrast_set")
public class FiContrastSet implements Serializable {
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
	/**规则名称*/
	@Excel(name = "规则名称", width = 15)
    @ApiModelProperty(value = "规则名称")
    private String name;
	/**账套*/
	@Excel(name = "账套", width = 15, dictTable = "fi_dbset", dicText = "dbname", dicCode = "dbname")
    @Dict(dictTable = "fi_dbset", dicText = "dbname", dicCode = "dbname")
    @ApiModelProperty(value = "账套")
    private String dbType;
	/**sql语句*/
	@Excel(name = "sql语句", width = 15)
    @ApiModelProperty(value = "sql语句")
    private String sqltext;
}
