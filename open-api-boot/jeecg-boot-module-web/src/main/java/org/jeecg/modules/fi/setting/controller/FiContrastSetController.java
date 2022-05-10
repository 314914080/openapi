package org.jeecg.modules.fi.setting.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.service.FiDbSetService;
import org.jeecg.modules.fi.setting.entity.ContrastData;
import org.jeecg.modules.fi.setting.entity.FiContrastSet;
import org.jeecg.modules.fi.setting.entity.FiContrastSetdetail;
import org.jeecg.modules.fi.setting.service.IFiContrastSetService;
import org.jeecg.modules.fi.setting.service.IFiContrastSetdetailService;
import org.jeecg.modules.fi.setting.vo.FiContrastSetPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

 /**
 * @Description: 对应设置
 * @Author: jeecg-boot
 * @Date:   2022-03-11
 * @Version: V1.0
 */
@Api(tags="对应设置")
@RestController
@RequestMapping("/setting/fiContrastSet")
@Slf4j
public class FiContrastSetController {
	@Autowired
	private IFiContrastSetService fiContrastSetService;
	@Autowired
	private IFiContrastSetdetailService fiContrastSetdetailService;

	@Autowired
	private FiDbSetService fiDbSetService;
	/**
	 * 分页列表查询
	 *
	 * @param fiContrastSet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "对应设置-分页列表查询")
	@ApiOperation(value="对应设置-分页列表查询", notes="对应设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FiContrastSet fiContrastSet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,@RequestParam(name="mainId", defaultValue="") String mainId) {

		System.out.println("MAINID"+mainId);
		QueryWrapper<FiContrastSet> queryWrapper = QueryGenerator.initQueryWrapper(fiContrastSet, req.getParameterMap());
		Page<FiContrastSet> page = new Page<FiContrastSet>(pageNo, pageSize);
		IPage<FiContrastSet> pageList = fiContrastSetService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fiContrastSetPage
	 * @return
	 */
	@AutoLog(value = "对应设置-添加")
	@ApiOperation(value="对应设置-添加", notes="对应设置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FiContrastSetPage fiContrastSetPage) {
		FiContrastSet fiContrastSet = new FiContrastSet();
		BeanUtils.copyProperties(fiContrastSetPage, fiContrastSet);
		fiContrastSetService.saveMain(fiContrastSet, fiContrastSetPage.getFiContrastSetdetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fiContrastSetPage
	 * @return
	 */
	@AutoLog(value = "对应设置-编辑")
	@ApiOperation(value="对应设置-编辑", notes="对应设置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FiContrastSetPage fiContrastSetPage) {
		FiContrastSet fiContrastSet = new FiContrastSet();
		BeanUtils.copyProperties(fiContrastSetPage, fiContrastSet);
		FiContrastSet fiContrastSetEntity = fiContrastSetService.getById(fiContrastSet.getId());
		if(fiContrastSetEntity==null) {
			return Result.error("未找到对应数据");
		}
		fiContrastSetService.updateMain(fiContrastSet, fiContrastSetPage.getFiContrastSetdetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "对应设置-通过id删除")
	@ApiOperation(value="对应设置-通过id删除", notes="对应设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		fiContrastSetService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "对应设置-批量删除")
	@ApiOperation(value="对应设置-批量删除", notes="对应设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fiContrastSetService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "对应设置-通过id查询")
	@ApiOperation(value="对应设置-通过id查询", notes="对应设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FiContrastSet fiContrastSet = fiContrastSetService.getById(id);
		if(fiContrastSet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fiContrastSet);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "规则设置明细通过主表ID查询")
	@ApiOperation(value="规则设置明细主表ID查询", notes="规则设置明细-通主表ID查询")
	@GetMapping(value = "/queryFiContrastSetdetailByMainId")
	public Result<?> queryFiContrastSetdetailListByMainId(@RequestParam(name="id",required=true) String id,@RequestParam(name="sqlText",required=false) String sqlText,@RequestParam(name="searchText",required=false) String searchText) {
		List<FiContrastSetdetail> fiContrastSetdetailListAll = new ArrayList<>();
		try {
		if(StringUtils.isEmpty(searchText)) {
			FiContrastSet fiContrastSet = fiContrastSetService.getById(id);
				List<ContrastData> contrastDataList = null;
				if (!StringUtils.isEmpty(fiContrastSet.getDbType()) && !StringUtils.isEmpty(sqlText)) {

					List<String> listSql = Arrays.asList(fiContrastSet.getSqltext().toUpperCase().split(" "));
					if (listSql.contains("UPDATE") || listSql.contains("DELETE") || listSql.contains("DROP")) {
						return Result.error("语句不能包含update、delete、drop等关键字");
					}

					//查询ERP数据库资料
					String dbKey = fiDbSetService.selectDbKeyByType(fiContrastSet.getDbType());
					List<Map<String, Object>> mapList = DynamicDBUtil.findList(dbKey, fiContrastSet.getSqltext());
					//转换对象
					contrastDataList = JSON.parseObject(JSON.toJSONString(mapList), new TypeReference<List<ContrastData>>() {
					});
				}
				List<FiContrastSetdetail> fiContrastSetdetailList = fiContrastSetdetailService.selectByMainId(id);
				if (contrastDataList != null && contrastDataList.size() != 0) {
					Map<String, ContrastData> map = contrastDataList.stream().collect(Collectors.toMap(ContrastData::getLxdm, Function.identity()));

					Map<String, FiContrastSetdetail> map2 = fiContrastSetdetailList.stream().collect(Collectors.toMap(FiContrastSetdetail::getCode, Function.identity()));

					//检查已经设置是否存在更新名称
					fiContrastSetdetailList.stream().forEach(fiContrastSetdetail -> {
						if (map.containsKey(fiContrastSetdetail.getCode())) {

							fiContrastSetdetail.setName(map.get(fiContrastSetdetail.getCode()).getLxmc());
						}
						fiContrastSetdetailListAll.add(fiContrastSetdetail);
					});
					//检查已经设置是否存在存在更新名称
					contrastDataList.stream().forEach(contrastData -> {
						if (!map2.containsKey(contrastData.getLxdm())) {
							FiContrastSetdetail fiContrastSetdetail = new FiContrastSetdetail();
							fiContrastSetdetail.setCode(contrastData.getLxdm());
							fiContrastSetdetail.setName(contrastData.getLxmc());
							fiContrastSetdetail.setMainId(id);
							fiContrastSetdetailListAll.add(fiContrastSetdetail);
						}
					});
				} else {

					fiContrastSetdetailListAll.addAll(fiContrastSetdetailList);

				}
		}else {
			LambdaQueryWrapper<FiContrastSetdetail> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper.like(FiContrastSetdetail::getCode,searchText).or().like(FiContrastSetdetail::getName,searchText);
			fiContrastSetdetailListAll.addAll(fiContrastSetdetailService.list(queryWrapper));
		}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error(500, "执行错误:" + e.getMessage());

		}

		return Result.OK(fiContrastSetdetailListAll);
	}


    /**
    * 导出excel
    *
    * @param request
    * @param fiContrastSet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FiContrastSet fiContrastSet) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<FiContrastSet> queryWrapper = QueryGenerator.initQueryWrapper(fiContrastSet, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<FiContrastSet> queryList = fiContrastSetService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<FiContrastSet> fiContrastSetList = new ArrayList<FiContrastSet>();
      if(oConvertUtils.isEmpty(selections)) {
          fiContrastSetList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          fiContrastSetList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<FiContrastSetPage> pageList = new ArrayList<FiContrastSetPage>();
      for (FiContrastSet main : fiContrastSetList) {
          FiContrastSetPage vo = new FiContrastSetPage();
          BeanUtils.copyProperties(main, vo);
          List<FiContrastSetdetail> fiContrastSetdetailList = fiContrastSetdetailService.selectByMainId(main.getId());
          vo.setFiContrastSetdetailList(fiContrastSetdetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "对应设置列表");
      mv.addObject(NormalExcelConstants.CLASS, FiContrastSetPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("对应设置数据", "导出人:"+sysUser.getRealname(), "对应设置"));
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
              List<FiContrastSetPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FiContrastSetPage.class, params);
              for (FiContrastSetPage page : list) {
                  FiContrastSet po = new FiContrastSet();
                  BeanUtils.copyProperties(page, po);
                  fiContrastSetService.saveMain(po, page.getFiContrastSetdetailList());
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

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "对应设置-执行SQL")
	 @ApiOperation(value="对应设置-执行SQL语句", notes="对应设置-执行SQL")
	 @GetMapping(value = "/querySqlById")
	 public Result<?> querySqlById(@RequestParam(name="id",required=true) String id,@RequestParam(name="dbType",required=true) String dbType,@RequestParam(name="sqlText",required=true) String sqlText,@RequestParam(name="searchText",required=false) String searchText) {
		 List<FiContrastSetdetail> fiContrastSetdetailListAll = new ArrayList<>();
		 try {
			 if(StringUtils.isEmpty(searchText)) {
				 FiContrastSet fiContrastSet = fiContrastSetService.getById(id);
				 List<ContrastData> contrastDataList = null;
				 if (!StringUtils.isEmpty(fiContrastSet.getDbType()) && !StringUtils.isEmpty(sqlText)) {

					 List<String> listSql = Arrays.asList(fiContrastSet.getSqltext().toUpperCase().split(" "));
					 if (listSql.contains("UPDATE") || listSql.contains("DELETE") || listSql.contains("DROP")) {
						 return Result.error("语句不能包含update、delete、drop等关键字");
					 }

					 //查询ERP数据库资料
					 String dbKey = fiDbSetService.selectDbKeyByType(fiContrastSet.getDbType());
					 List<Map<String, Object>> mapList = DynamicDBUtil.findList(dbKey, fiContrastSet.getSqltext());
					 //转换对象
					 contrastDataList = JSON.parseObject(JSON.toJSONString(mapList), new TypeReference<List<ContrastData>>() {
					 });
				 }
				 List<FiContrastSetdetail> fiContrastSetdetailList = fiContrastSetdetailService.selectByMainId(id);
				 if (contrastDataList != null && contrastDataList.size() != 0) {
					 Map<String, ContrastData> map = contrastDataList.stream().collect(Collectors.toMap(ContrastData::getLxdm, Function.identity()));

					 Map<String, FiContrastSetdetail> map2 = fiContrastSetdetailList.stream().collect(Collectors.toMap(FiContrastSetdetail::getCode, Function.identity()));

					 //检查已经设置是否存在更新名称
					 fiContrastSetdetailList.stream().forEach(fiContrastSetdetail -> {
						 if (map.containsKey(fiContrastSetdetail.getCode())) {

							 fiContrastSetdetail.setName(map.get(fiContrastSetdetail.getCode()).getLxmc());
						 }
						 fiContrastSetdetailListAll.add(fiContrastSetdetail);
					 });
					 //检查已经设置是否存在存在更新名称
					 contrastDataList.stream().forEach(contrastData -> {
						 if (!map2.containsKey(contrastData.getLxdm())) {
							 FiContrastSetdetail fiContrastSetdetail = new FiContrastSetdetail();
							 fiContrastSetdetail.setCode(contrastData.getLxdm());
							 fiContrastSetdetail.setName(contrastData.getLxmc());
							 fiContrastSetdetail.setMainId(id);
							 fiContrastSetdetailListAll.add(fiContrastSetdetail);
						 }
					 });
				 } else {
					 fiContrastSetdetailListAll.addAll(fiContrastSetdetailList);
				 }
			 }else {
				 LambdaQueryWrapper<FiContrastSetdetail> queryWrapper=new LambdaQueryWrapper<>();
				 queryWrapper.like(FiContrastSetdetail::getCode,searchText).or().like(FiContrastSetdetail::getName,searchText);
				 fiContrastSetdetailListAll.addAll(fiContrastSetdetailService.list(queryWrapper));
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
			 return Result.error(500, "执行错误:" + e.getMessage());

		 }

		 return Result.OK(fiContrastSetdetailListAll);
	 }


 }
