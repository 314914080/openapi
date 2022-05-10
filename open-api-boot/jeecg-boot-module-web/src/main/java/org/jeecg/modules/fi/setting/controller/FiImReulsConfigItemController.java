package org.jeecg.modules.fi.setting.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.core.k3api.entity.Account;
import org.jeecg.core.k3api.service.K3ApiService;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigItem;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigItemService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 科目核算项目
 * @Author: jeecg-boot
 * @Date:   2022-04-07
 * @Version: V1.0
 */
@Api(tags="科目核算项目")
@RestController
@RequestMapping("/setting/fiImReulsConfigItem")
@Slf4j
public class FiImReulsConfigItemController extends JeecgController<FiImReulsConfigItem, IFiImReulsConfigItemService> {
	@Autowired
	private IFiImReulsConfigItemService fiImReulsConfigItemService;

	@Autowired

	private K3ApiService k3ApiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fiImReulsConfigItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "科目核算项目-分页列表查询")
	@ApiOperation(value="科目核算项目-分页列表查询", notes="科目核算项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FiImReulsConfigItem fiImReulsConfigItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,  @RequestParam(name="kmdm", defaultValue="") String kmdm) {
		QueryWrapper<FiImReulsConfigItem> queryWrapper = QueryGenerator.initQueryWrapper(fiImReulsConfigItem, req.getParameterMap());
		Page<FiImReulsConfigItem> page = new Page<FiImReulsConfigItem>(pageNo, pageSize);
		IPage<FiImReulsConfigItem> pageList = fiImReulsConfigItemService.page(page, queryWrapper);

		System.out.println(kmdm);
		if(StringUtils.isEmpty(kmdm)){
			return Result.OK(pageList);
		}

		List<Account> accounts= null;
		try {
			accounts = k3ApiService.getK3CloudAccount(kmdm);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Result.error(e.getMessage());
		}
		if(accounts.size()==0){

			return Result.OK(pageList);
		}
		Map<String,Account>  accountMap=accounts.stream().collect((Collectors.toMap(Account::getFlexNumber, Function.identity())));
		List<FiImReulsConfigItem> itemsAll=new ArrayList<>();
		List<FiImReulsConfigItem> itemList=pageList.getRecords();
		Map<String,FiImReulsConfigItem> itemMap= itemList.stream().collect(Collectors.toMap(FiImReulsConfigItem::getItemcode, Function.identity()));
		accounts.stream().forEach(account -> {

			if(itemMap.containsKey(account.getFlexNumber())){

				FiImReulsConfigItem fiImReulsConfigItem1=itemMap.get(account.getFlexNumber());
				fiImReulsConfigItem1.setMainid(fiImReulsConfigItem.getMainid());
				fiImReulsConfigItem1.setType("1");
				itemsAll.add(fiImReulsConfigItem1);
			}else {
				FiImReulsConfigItem fiImReulsConfigItem1=new FiImReulsConfigItem();
				fiImReulsConfigItem1.setItemcode(account.getFlexNumber());
				fiImReulsConfigItem1.setItemname(account.getFlexName());
				fiImReulsConfigItem1.setMainid(fiImReulsConfigItem.getMainid());
				fiImReulsConfigItem1.setType("1");
				itemsAll.add(fiImReulsConfigItem1);
			}

		});

		itemList.stream().forEach(fiImReulsConfigItem1 -> {

			if(!accountMap.containsKey(fiImReulsConfigItem1.getItemcode())){

				fiImReulsConfigItem1.setBz("此核算项目已经不存在，请删除");
				itemsAll.add(fiImReulsConfigItem1);
			}
		});

		pageList.setRecords(itemsAll);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fiImReulsConfigItem
	 * @return
	 */
	@AutoLog(value = "科目核算项目-添加")
	@ApiOperation(value="科目核算项目-添加", notes="科目核算项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FiImReulsConfigItem fiImReulsConfigItem) {
		fiImReulsConfigItemService.save(fiImReulsConfigItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fiImReulsConfigItem
	 * @return
	 */
	@AutoLog(value = "科目核算项目-编辑")
	@ApiOperation(value="科目核算项目-编辑", notes="科目核算项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FiImReulsConfigItem fiImReulsConfigItem) {
		fiImReulsConfigItemService.updateById(fiImReulsConfigItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "科目核算项目-通过id删除")
	@ApiOperation(value="科目核算项目-通过id删除", notes="科目核算项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		fiImReulsConfigItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "科目核算项目-批量删除")
	@ApiOperation(value="科目核算项目-批量删除", notes="科目核算项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fiImReulsConfigItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "科目核算项目-通过id查询")
	@ApiOperation(value="科目核算项目-通过id查询", notes="科目核算项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FiImReulsConfigItem fiImReulsConfigItem = fiImReulsConfigItemService.getById(id);
		if(fiImReulsConfigItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fiImReulsConfigItem);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fiImReulsConfigItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FiImReulsConfigItem fiImReulsConfigItem) {
        return super.exportXls(request, fiImReulsConfigItem, FiImReulsConfigItem.class, "科目核算项目");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FiImReulsConfigItem.class);
    }

}
