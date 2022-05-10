package org.jeecg.modules.fi.setting.vo;

import java.util.List;
import org.jeecg.modules.fi.setting.entity.FiContrastSet;
import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 对应设置
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="fi_contrast_setPage对象", description="对应设置")
public class FiContrastSetPage {

	/**主键*/
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

	@ExcelCollection(name="规则设置明细")
	@ApiModelProperty(value = "规则设置明细")
	private List<FiContrastSetdetail> fiContrastSetdetailList;

}
