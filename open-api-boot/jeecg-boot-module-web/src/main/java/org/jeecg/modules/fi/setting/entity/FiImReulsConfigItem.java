package org.jeecg.modules.fi.setting.entity;

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
 * @Description: 科目核算项目
 * @Author: jeecg-boot
 * @Date:   2022-04-07
 * @Version: V1.0
 */
@Data
@TableName("fi_im_reuls_config_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fi_im_reuls_config_item对象", description="科目核算项目")
public class FiImReulsConfigItem implements Serializable {
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
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;
	/**项目代码*/
	@Excel(name = "项目代码", width = 15)
    @ApiModelProperty(value = "项目代码")
    private String itemcode;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String itemname;
	/**SQL字段*/
	@Excel(name = "SQL字段", width = 15)
    @ApiModelProperty(value = "SQL字段")
    private String fieldname;
	/**对应规则*/
	@Excel(name = "对应规则", width = 15, dictTable = "fi_contrast_set", dicText = "name", dicCode = "id")
	@Dict(dictTable = "fi_contrast_set", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "对应规则")
    private String contrastcode;
	/**备注字段*/
	@Excel(name = "备注字段", width = 15)
    @ApiModelProperty(value = "备注字段")
    private String bz;
	/**主表ID*/
	@Excel(name = "主表ID", width = 15)
    @ApiModelProperty(value = "主表ID")
    private String mainid;
}
