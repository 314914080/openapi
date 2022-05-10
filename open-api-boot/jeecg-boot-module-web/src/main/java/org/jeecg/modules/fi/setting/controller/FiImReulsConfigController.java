package org.jeecg.modules.fi.setting.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigJdetail;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfigDdetail;
import org.jeecg.modules.fi.setting.entity.FiImReulsConfig;
import org.jeecg.modules.fi.setting.vo.FiImReulsConfigPage;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigService;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigJdetailService;
import org.jeecg.modules.fi.setting.service.IFiImReulsConfigDdetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 凭证导入规则设置
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Api(tags="凭证导入规则设置")
@RestController
@RequestMapping("/setting/fiImReulsConfig")
@Slf4j
public class FiImReulsConfigController {
	@Autowired
	private IFiImReulsConfigService fiImReulsConfigService;
	@Autowired
	private IFiImReulsConfigJdetailService fiImReulsConfigJdetailService;
	@Autowired
	private IFiImReulsConfigDdetailService fiImReulsConfigDdetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fiImReulsConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-分页列表查询")
	@ApiOperation(value="凭证导入规则设置-分页列表查询", notes="凭证导入规则设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FiImReulsConfig fiImReulsConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FiImReulsConfig> queryWrapper = QueryGenerator.initQueryWrapper(fiImReulsConfig, req.getParameterMap());
		Page<FiImReulsConfig> page = new Page<FiImReulsConfig>(pageNo, pageSize);
		IPage<FiImReulsConfig> pageList = fiImReulsConfigService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fiImReulsConfigPage
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-添加")
	@ApiOperation(value="凭证导入规则设置-添加", notes="凭证导入规则设置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FiImReulsConfigPage fiImReulsConfigPage) {
		FiImReulsConfig fiImReulsConfig = new FiImReulsConfig();
		BeanUtils.copyProperties(fiImReulsConfigPage, fiImReulsConfig);
		fiImReulsConfigService.saveMain(fiImReulsConfig, fiImReulsConfigPage.getFiImReulsConfigJdetailList(),fiImReulsConfigPage.getFiImReulsConfigDdetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fiImReulsConfigPage
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-编辑")
	@ApiOperation(value="凭证导入规则设置-编辑", notes="凭证导入规则设置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FiImReulsConfigPage fiImReulsConfigPage) {
		FiImReulsConfig fiImReulsConfig = new FiImReulsConfig();
		BeanUtils.copyProperties(fiImReulsConfigPage, fiImReulsConfig);
		FiImReulsConfig fiImReulsConfigEntity = fiImReulsConfigService.getById(fiImReulsConfig.getId());
		if(fiImReulsConfigEntity==null) {
			return Result.error("未找到对应数据");
		}
		fiImReulsConfigService.updateMain(fiImReulsConfig, fiImReulsConfigPage.getFiImReulsConfigJdetailList(),fiImReulsConfigPage.getFiImReulsConfigDdetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-通过id删除")
	@ApiOperation(value="凭证导入规则设置-通过id删除", notes="凭证导入规则设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		fiImReulsConfigService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-批量删除")
	@ApiOperation(value="凭证导入规则设置-批量删除", notes="凭证导入规则设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fiImReulsConfigService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "凭证导入规则设置-通过id查询")
	@ApiOperation(value="凭证导入规则设置-通过id查询", notes="凭证导入规则设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FiImReulsConfig fiImReulsConfig = fiImReulsConfigService.getById(id);
		if(fiImReulsConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fiImReulsConfig);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "借方科目设置通过主表ID查询")
	@ApiOperation(value="借方科目设置主表ID查询", notes="借方科目设置-通主表ID查询")
	@GetMapping(value = "/queryFiImReulsConfigJdetailByMainId")
	public Result<?> queryFiImReulsConfigJdetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList = fiImReulsConfigJdetailService.selectByMainId(id);
		return Result.OK(fiImReulsConfigJdetailList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "贷方科目设置通过主表ID查询")
	@ApiOperation(value="贷方科目设置主表ID查询", notes="贷方科目设置-通主表ID查询")
	@GetMapping(value = "/queryFiImReulsConfigDdetailByMainId")
	public Result<?> queryFiImReulsConfigDdetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList = fiImReulsConfigDdetailService.selectByMainId(id);
		return Result.OK(fiImReulsConfigDdetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fiImReulsConfig
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FiImReulsConfig fiImReulsConfig) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<FiImReulsConfig> queryWrapper = QueryGenerator.initQueryWrapper(fiImReulsConfig, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<FiImReulsConfig> queryList = fiImReulsConfigService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<FiImReulsConfig> fiImReulsConfigList = new ArrayList<FiImReulsConfig>();
      if(oConvertUtils.isEmpty(selections)) {
          fiImReulsConfigList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          fiImReulsConfigList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<FiImReulsConfigPage> pageList = new ArrayList<FiImReulsConfigPage>();
      for (FiImReulsConfig main : fiImReulsConfigList) {
          FiImReulsConfigPage vo = new FiImReulsConfigPage();
          BeanUtils.copyProperties(main, vo);
          List<FiImReulsConfigJdetail> fiImReulsConfigJdetailList = fiImReulsConfigJdetailService.selectByMainId(main.getId());
          vo.setFiImReulsConfigJdetailList(fiImReulsConfigJdetailList);
          List<FiImReulsConfigDdetail> fiImReulsConfigDdetailList = fiImReulsConfigDdetailService.selectByMainId(main.getId());
          vo.setFiImReulsConfigDdetailList(fiImReulsConfigDdetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "凭证导入规则设置列表");
      mv.addObject(NormalExcelConstants.CLASS, FiImReulsConfigPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("凭证导入规则设置数据", "导出人:"+sysUser.getRealname(), "凭证导入规则设置"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<FiImReulsConfigPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FiImReulsConfigPage.class, params);
              for (FiImReulsConfigPage page : list) {
                  FiImReulsConfig po = new FiImReulsConfig();
                  BeanUtils.copyProperties(page, po);
                  fiImReulsConfigService.saveMain(po, page.getFiImReulsConfigJdetailList(),page.getFiImReulsConfigDdetailList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
