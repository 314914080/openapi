package org.jeecg.modules.fi.query.entity;

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
 * @Description: 导入查询
 * @Author: jeecg-boot
 * @Date:   2022-05-10
 * @Version: V1.0
 */
@Data
@TableName("fi_im_groupkey")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fi_im_groupkey对象", description="导入查询")
public class FiImGroupkey implements Serializable {
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
	/**唯一键值*/
	@Excel(name = "唯一键值", width = 15)
    @ApiModelProperty(value = "唯一键值")
    private String groupkey;
	/**消息*/
	@Excel(name = "消息", width = 15)
    @ApiModelProperty(value = "消息")
    private String msg;
	/**规则编号*/
	@Excel(name = "规则编号", width = 15)
    @ApiModelProperty(value = "规则编号")
    private String code;
	/**规则描述*/
	@Excel(name = "规则描述", width = 15)
    @ApiModelProperty(value = "规则描述")
    private String name;
}
