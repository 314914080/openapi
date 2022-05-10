package org.jeecg.modules.fi.importcenter.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.common.websocket.WebSocketFI;
import org.jeecg.core.k3api.entity.VoucherFEntity;
import org.jeecg.core.k3api.entity.VoucherModel;
import org.jeecg.core.k3api.service.K3ApiService;
import org.jeecg.modules.common.entity.FiDbset;
import org.jeecg.modules.common.service.FiDbSetService;
import org.jeecg.modules.fi.importcenter.entity.ImportReq;
import org.jeecg.core.k3api.entity.K3CloudVoucher;
import org.jeecg.modules.fi.importcenter.entity.Order;
import org.jeecg.modules.fi.importcenter.service.VImportCenterService;
import org.jeecg.modules.fi.query.entity.FiImGroupkey;
import org.jeecg.modules.fi.query.service.IFiImGroupkeyService;
import org.jeecg.modules.fi.setting.entity.*;
import org.jeecg.modules.fi.setting.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhengbo
 * @date 2022-04-08
 */
@Component
@Slf4j
public class VImportCenterServiceImpl implements VImportCenterService {

    @Autowired
    WebSocketFI webSocketFI;

    @Autowired
    IFiImReulsConfigService iFiImReulsConfigService;

    @Autowired
    private FiDbSetService fiDbSetService;

    @Autowired
    private IFiContrastSetService fiContrastSetService;

    @Autowired
    K3ApiService k3ApiService;

    @Autowired
    IFiImReulsConfigDdetailService iFiImReulsConfigDdetailService;

    @Autowired
    IFiImReulsConfigJdetailService iFiImReulsConfigJdetailService;

    @Autowired
    IFiImReulsConfigItemService iFiImReulsConfigItemService;

    @Autowired
    private IFiContrastSetdetailService fiContrastSetdetailService;

    @Autowired
    private IFiImGroupkeyService iFiImGroupkeyService;


    @Override
    @Async
    public void executeImport(ImportReq importReq) {
        try {


            if (StringUtils.isEmpty(importReq.getCode()) || StringUtils.isEmpty(importReq.getStartDate()) || StringUtils.isEmpty(importReq.getEndDate())) {

                webSocketFI.pushMessage(importReq.getUserId(), "请选择导入日期或者导入规则");
                return;
            }

            //按组ID分组查询
            LambdaQueryWrapper<FiImReulsConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FiImReulsConfig::getCode, importReq.getCode());
            FiImReulsConfig fiImReulsConfig = iFiImReulsConfigService.getOne(queryWrapper);

            List<String> listSql = Arrays.asList(fiImReulsConfig.getSqltext1().toUpperCase().split(" "));
            if (listSql.contains("UPDATE") || listSql.contains("DELETE") || listSql.contains("DROP")) {
                webSocketFI.pushMessage(importReq.getUserId(), "语句不能包含update、delete、drop等关键字");
            }


            //查询ERP数据库资料
            FiDbset erpDbset = fiDbSetService.selectDbKeyById(fiImReulsConfig.getErpDbType());

            System.out.println("数据源" + JSON.toJSONString(erpDbset));

            if (!fiImReulsConfig.getSqltext1().contains("@startdate")) {
                webSocketFI.pushMessage(importReq.getUserId(), "语句没有@startdate 关键参数");
            }

            if (!fiImReulsConfig.getSqltext1().contains("@enddate")) {
                webSocketFI.pushMessage(importReq.getUserId(), "语句没有@enddate 关键参数");
            }
            String sql = "";
            sql = fiImReulsConfig.getSqltext1().replace("@startdate", "'" + importReq.getStartDate() + "'");
            sql = sql.replace("@enddate", "'" + importReq.getEndDate() + "'");
            List<Map<String, Object>> mapList = DynamicDBUtil.findList(erpDbset.getDbcode(), sql);
            //转换对象
//      JSON.parseObject(JSON.toJSONString(mapList),new TypeReference<List<Object>>(){});

            webSocketFI.pushMessage(importReq.getUserId(), "获取数据行数:" + mapList.size());
            HashMap<String, List<Order>> map = assembleOrder(mapList);


            //查询财务财务软件
            FiDbset fiDbset = fiDbSetService.selectDbKeyById(fiImReulsConfig.getFiDbType());
            if ("k3cloud".equals(fiDbset.getFitype())) {
                excuteImportK3Cloud(importReq, fiImReulsConfig, map);
            } else {

                webSocketFI.pushMessage(importReq.getUserId(), "无法识别的财务软件类型");
            }


        } catch (Exception e) {
            webSocketFI.pushMessage(importReq.getUserId(), "执行异常:" + e.getMessage());
        } finally {
            webSocketFI.pushMessage(importReq.getUserId(), "OK");
        }
    }


    public void excuteImportK3Cloud(ImportReq importReq, FiImReulsConfig fiImReulsConfig, HashMap<String, List<Order>> map) {

        webSocketFI.pushMessage(importReq.getUserId(), "开始登录金蝶云星空获取SESSION");

        try {


            if (k3ApiService.getK3CloudSession("", "", "")) {
                webSocketFI.pushMessage(importReq.getUserId(), "金蝶云星空登录成功!!!");
            }

            //获取借方科目设置
            List<FiImReulsConfigJdetail> jdetails = iFiImReulsConfigJdetailService.selectByMainId(fiImReulsConfig.getId());

            Map<String, FiImReulsConfigJdetail> jdetailMap = jdetails.stream().collect(Collectors.toMap(FiImReulsConfigJdetail::getCode, Function.identity()));

            //获取贷方科目设置
            List<FiImReulsConfigDdetail> ddetails = iFiImReulsConfigDdetailService.selectByMainId(fiImReulsConfig.getId());

            Map<String, FiImReulsConfigDdetail> ddetailMap = ddetails.stream().collect(Collectors.toMap(FiImReulsConfigDdetail::getCode, Function.identity()));

            //获取借贷方项目核算
            QueryWrapper<FiImReulsConfigItem> queryWrapperItem = new QueryWrapper<>();
            List<FiImReulsConfigItem> itemList = iFiImReulsConfigItemService.list(queryWrapperItem);

            Map<String, List<FiImReulsConfigItem>> itemMap = itemList.stream().collect(Collectors.groupingBy(FiImReulsConfigItem::getMainid));


            //获取核算项目对照设置
            QueryWrapper<FiContrastSetdetail> queryWrapperContrastSet = new QueryWrapper<>();
            List<FiContrastSetdetail> fiContrastSetdetailList = fiContrastSetdetailService.list(queryWrapperContrastSet);
            Map<String, List<FiContrastSetdetail>> contrastMap = fiContrastSetdetailList.stream().collect(Collectors.groupingBy(FiContrastSetdetail::getMainId));


            //遍历map
            Iterator<String> iter1 = map.keySet().iterator();

            K3CloudVoucher v1 = null;//凭证
            VoucherModel vm = null;//凭证主表
            VoucherFEntity vej = null;//凭证借方分录
            VoucherFEntity ved = null;//凭证贷方方分录
            l:
            while (iter1.hasNext()) {

                String key = iter1.next();
                webSocketFI.pushMessage(importReq.getUserId(), "分析凭证:" + key);
                List<Order> orders = map.get(key);
                v1 = new K3CloudVoucher();
                vm = new VoucherModel();

                vm.setFAccountBookID(new JSONObject());
                vm.setFDate(key);
                vm.setFBUSDATE(key);
                vm.setFDocumentStatus("Z");
                vm.setFISADJUSTVOUCHER(false);
                vm.setFVOUCHERGROUPNO("");

                Map<String, List<VoucherFEntity>> voucherModelMap = new HashMap<>();//凭证主表


                //遍历当前日期所有数据行
                for (Order order : orders) {

                    //盘点金额是否为0  0的不允许生成凭证
                    if (order.getAmount().compareTo(new BigDecimal("0")) == 0 && order.getAmount().compareTo(new BigDecimal("0")) == 0) {
                        continue;
                    }

                    //获取设置的借方科目

                    //借贷方类型代码
                    String jlxdm = fiImReulsConfig.getJfiled();
                    String dlxdm = fiImReulsConfig.getDfiled();
                    if (!order.getMapValue().containsKey(jlxdm) || !order.getMapValue().containsKey(dlxdm)) {
                        webSocketFI.pushMessage(importReq.getUserId(), order.getOrderkey() + ":没有设置科目的借贷类型字段");
                        continue l;
                    }
                    //借方科目


                    FiImReulsConfigJdetail jkm = jdetailMap.get(order.getMapValue().get(jlxdm));
                    //贷方科目
                    FiImReulsConfigDdetail dkm = ddetailMap.get(order.getMapValue().get(dlxdm));


                    if (jkm == null || StringUtils.isEmpty(jkm.getKmcode())) {

                        webSocketFI.pushMessage(importReq.getUserId(), order.getOrderkey() + ":没有设置借方科目:"+order.getMapValue().get(jlxdm));
                        continue l;
                    }


                    if (dkm == null || StringUtils.isEmpty(dkm.getKmcode())) {
                        webSocketFI.pushMessage(importReq.getUserId(), order.getOrderkey() + ":没有设置贷方科目:"+order.getMapValue().get(dlxdm));
                        continue l;
                    }

                    vej = new VoucherFEntity();
                    ved = new VoucherFEntity();

                    //设置借方分录
                    vej.setFACCOUNTID(getJSONObject("FNumber", jkm.getKmcode()));
                    vej.setFCURRENCYID(getJSONObject("FNumber", "PRE001"));
                    vej.setFEXCHANGERATETYPE(getJSONObject("FNumber", "HLTX01_SYS"));
                    vej.setFEXPLANATION(order.getRemark());
                    vej.setFAMOUNTFOR(order.getAmount());
                    vej.setFDEBIT(order.getAmount());
                    vej.setFDC("1");
                    if (itemMap.containsKey(jkm.getId())) {//设置核算项目
                        List<FiImReulsConfigItem> jConfigItem = itemMap.get(jkm.getId());

                        JSONObject objJItem = new JSONObject();
                        for (FiImReulsConfigItem fiImReulsConfigItem : jConfigItem) {
                            String fieldName = fiImReulsConfigItem.getFieldname();//字段名称
                            String itemcode = fiImReulsConfigItem.getItemcode();//项目代码
                            String itemname = fiImReulsConfigItem.getItemname();//项目代码
                            String contrastcode = fiImReulsConfigItem.getContrastcode();//项目对照表

                            if (StringUtils.isEmpty(fieldName) || !order.getMapValue().containsKey(fieldName)) {
                                webSocketFI.pushMessage(importReq.getUserId(), order.getOrderkey() + ":核算项目没有设置完整:" + itemname);
                                continue l;
                            }
                            String value = order.getMapValue().get(fieldName);
                            if (!StringUtils.isEmpty(contrastcode)) {
                                value = getContrastSet(value, contrastMap.get(contrastcode));
                            }
                            objJItem.put("FDETAILID__" + itemcode.toUpperCase(), getJSONObject("FNumber", value));
                        }
                        vej.setFDetailID(objJItem);
                    }


                    //设置贷方分录
                    ved.setFACCOUNTID(getJSONObject("FNumber", dkm.getKmcode()));
                    ved.setFCURRENCYID(getJSONObject("FNumber", "PRE001"));
                    ved.setFEXCHANGERATETYPE(getJSONObject("FNumber", "HLTX01_SYS"));
                    ved.setFAMOUNTFOR(order.getAmount());
                    ved.setFCREDIT(order.getAmount());
                    ved.setFEXPLANATION(order.getRemark());
                    ved.setFDC("-1");
                    if (itemMap.containsKey(dkm.getId())) {//设置核算项目
                        List<FiImReulsConfigItem> dConfigItem = itemMap.get(dkm.getId());

                        JSONObject objDItem = new JSONObject();
                        for (FiImReulsConfigItem fiImReulsConfigItem : dConfigItem) {
                            String fieldName = fiImReulsConfigItem.getFieldname();//字段名称
                            String itemcode = fiImReulsConfigItem.getItemcode();//项目代码
                            String itemname = fiImReulsConfigItem.getItemname();//项目代码
                            String contrastcode = fiImReulsConfigItem.getContrastcode();//项目对照表

                            if (StringUtils.isEmpty(fieldName) || !order.getMapValue().containsKey(fieldName)) {
                                webSocketFI.pushMessage(importReq.getUserId(), order.getOrderkey() + ":核算项目没有设置完整:" + itemname);
                                continue l;
                            }
                            String value = order.getMapValue().get(fieldName);
                            if (!StringUtils.isEmpty(contrastcode)) {
                                value = getContrastSet(value, contrastMap.get(contrastcode));
                            }
                            objDItem.put("FDETAILID__" + itemcode.toUpperCase(), getJSONObject("FNumber", value));
                        }
                        ved.setFDetailID(objDItem);
                    }


                    if (voucherModelMap.containsKey(order.getGroupkey())) {

                        List<VoucherFEntity> entityList = voucherModelMap.get(order.getGroupkey());
                        entityList.add(vej);
                        entityList.add(ved);
                        voucherModelMap.put(order.getGroupkey(), entityList);

                    } else {

                        List<VoucherFEntity> entityList = new ArrayList<>();
                        entityList.add(vej);
                        entityList.add(ved);
                        voucherModelMap.put(order.getGroupkey(), entityList);
                    }

                }
                webSocketFI.pushMessage(importReq.getUserId(), "分析完成");
                webSocketFI.pushMessage(importReq.getUserId(), "开始分组合并借贷方");
                Iterator<String> ite = voucherModelMap.keySet().iterator();
                while (ite.hasNext()) {
                    String keys=ite.next();
                    List<VoucherFEntity> entityList = voucherModelMap.get(keys);
                    //合并借贷方
                    switch (fiImReulsConfig.getFiMergeType()) {
                        case "0":
                            vm.setFEntity(entityList);
                            break;
                        case "1":
                            vm.setFEntity(mergeItem(entityList, "1"));
                            break;
                        case "2":
                            vm.setFEntity(mergeItem(entityList, "-1"));
                            break;
                        case "3":
                            vm.setFEntity(mergeItem(mergeItem(entityList, "1"),"-1"));
                            break;
                        default:
                            vm.setFEntity(entityList);
                            break;
                    }
                    v1.setModel(vm);
                    String json = JSON.toJSONString(v1);
                    log.info("JSON格式：" + json);
                    webSocketFI.pushMessage(importReq.getUserId(), keys+"凭证导入中..." );
                    String msg = k3ApiService.saveVoucher("", "GL_VOUCHER", json);

                    webSocketFI.pushMessage(importReq.getUserId(), keys+"凭证导入结果:" + msg);
                    FiImGroupkey fiImGroupkey=new FiImGroupkey();
                    fiImGroupkey.setCode(fiImReulsConfig.getCode());
                    fiImGroupkey.setName(fiImReulsConfig.getName());
                    fiImGroupkey.setMsg(msg);
                    fiImGroupkey.setGroupkey(keys);
                    iFiImGroupkeyService.save(fiImGroupkey);

                }

                webSocketFI.pushMessage(importReq.getUserId(), "凭证导入结束:" + key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("凭证导入出错" + e.getMessage());

            webSocketFI.pushMessage(importReq.getUserId(), "系统错误:" + e.getMessage());
        }
    }


    /**
     * 合并借贷方
     *
     * @param list
     * @return
     */
    public List<VoucherFEntity> mergeItem(List<VoucherFEntity> list, String fdc) {
        List<VoucherFEntity> mergeList = new ArrayList<>();

        HashMap<String, VoucherFEntity> vmap = new HashMap<>();

        StringBuilder str = null;
        for (VoucherFEntity v : list) {
            if (!v.getFDC().equals(fdc) && !fdc.equals("3")) {
                mergeList.add(v);
                continue;
            }
            str = new StringBuilder();
            str.append(v.getFDC() + v.getFACCOUNTID().getString("FNumber"));

            if (v.getFDetailID() != null && v.getFDetailID().size() != 0) {

                Iterator<String> ite = v.getFDetailID().keySet().stream().sorted().iterator();

                while (ite.hasNext()) {
                    String key = ite.next();
                    str.append(key);
                    str.append(v.getFDetailID().getString(key));
                }
            }
            String key = str.toString();

            if (vmap.containsKey(key)) {
                VoucherFEntity vx1 = vmap.get(key);
                vx1.setFAMOUNTFOR(vx1.getFAMOUNTFOR().add(v.getFAMOUNTFOR()));
                if("1".equals(fdc)){
                    vx1.setFDEBIT(vx1.getFDEBIT().add(v.getFDEBIT()));
                }else  if("-1".equals(fdc)){
                    vx1.setFCREDIT(vx1.getFCREDIT().add(v.getFCREDIT()));
                }
                vmap.put(key, vx1);
            } else {
                vmap.put(key, v);
            }

        }
        vmap.forEach((key, v) -> {
            mergeList.add(v);

        });


        return mergeList;
    }


    private String getContrastSet(String value, List<FiContrastSetdetail> contrastSetdetails) {

        Map<String, FiContrastSetdetail> ddetailMap = contrastSetdetails.stream().collect(Collectors.toMap(FiContrastSetdetail::getCode, Function.identity()));

        if (ddetailMap.containsKey(value)) {

            return ddetailMap.get(value).getExternalcode();

        } else {

            return value;

        }
    }


    private JSONObject getJSONObject(String name, String value) {
        JSONObject obj = new JSONObject();
        obj.put(name, value);
        return obj;
    }


    /**
     * 组装查询单据
     *
     * @param mapList
     * @return
     */
    public HashMap<String, List<Order>> assembleOrder(List<Map<String, Object>> mapList) {
        HashMap<String, List<Order>> listOrderMap = new HashMap<>();
        mapList.forEach(map -> {

            Order order = new Order();
            order.setMapValue(new HashMap<>());
            map.forEach((key, value) -> {
                switch (key.toLowerCase()) {
                    case "orderkey":
                        order.setOrderkey(String.valueOf(value));
                        break;
                    case "orderdate":
                        order.setOrderDate(String.valueOf(value));
                        break;
                    case "amount":
                        order.setAmount(new BigDecimal(String.valueOf(value)));
                        break;
                    case "qty":
                        order.setQty(new BigDecimal(String.valueOf(value)));
                        break;
                    case "remark":
                        order.setRemark(String.valueOf(value));
                        break;
                    case "groupkey":
                        order.setGroupkey(String.valueOf(value));
                        break;
                    default:
                        order.getMapValue().put(key, String.valueOf(value));
                        break;
                }
            });
            if (listOrderMap.containsKey(order.getOrderDate())) {
                List<Order> orders = listOrderMap.get(order.getOrderDate());
                orders.add(order);
                listOrderMap.put(order.getOrderDate(), orders);

            } else {
                List<Order> orders = new ArrayList<>();
                orders.add(order);
                listOrderMap.put(order.getOrderDate(), orders);
            }
        });
        return listOrderMap;
    }


}
