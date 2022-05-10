package org.jeecg.modules.fi.setting.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 规则设置明细
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@ApiModel(value="fi_contrast_setdetail对象", description="规则设置明细")
@Data
@TableName("fi_contrast_setdetail")
public class FiContrastSetdetail implements Serializable {
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
	/**主数据代码*/
	@Excel(name = "主数据代码", width = 15)
    @ApiModelProperty(value = "主数据代码")
    private String code;
	/**主数据名称*/
	@Excel(name = "主数据名称", width = 15)
    @ApiModelProperty(value = "主数据名称")
    private String name;
	/**外部系统编码*/
	@Excel(name = "外部系统编码", width = 15)
    @ApiModelProperty(value = "外部系统编码")
    private String externalcode;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String mainId;
}
