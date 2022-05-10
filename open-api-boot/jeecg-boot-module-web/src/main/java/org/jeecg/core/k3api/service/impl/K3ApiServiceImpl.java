package org.jeecg.core.k3api.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kingdee.bos.webapi.client.K3CloudApiClient;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.core.k3api.entity.Account;
import org.jeecg.core.k3api.service.K3ApiService;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengbo
 * @date 2022-03-22
 */
@Component
@Slf4j
public class K3ApiServiceImpl implements K3ApiService {


    public K3CloudApiClient k3CloudApiClient = null;


    @Override
    public String saveVoucher(String sessionid, String formid, String jsonData) {

        String msg = "";
        try {
            msg=k3CloudApiClient.save(formid, jsonData);
            log.info(msg);
           JSONObject obj= JSONObject.parseObject(msg);
           if (obj.containsKey("Result")){

             String status=obj.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess");
             if("true".equals(status)){

                return "成功";
             }
           }

        } catch (Exception e) {
            e.printStackTrace();
            msg="导入异常:"+e.getMessage();
        }
        return msg;
    }

    @Override
    public Boolean getK3CloudSession(String dbid, String userid, String password) throws Exception {

        if (k3CloudApiClient != null) {
            return true;
        }
        k3CloudApiClient = new K3CloudApiClient("https://sxyldfs.test.ik3cloud.com/K3Cloud/");

        if (k3CloudApiClient.login("20200306132749149", "郑波", "yld2022..", 2052)) {

            return true;
        }
        return false;
    }


    public List<Account> getK3CloudAccount(String kmdm) throws BusinessException {

        List<Account> list = new ArrayList<>();
        try {


            System.out.println(k3CloudApiClient);
            if (k3CloudApiClient == null) {

                getK3CloudSession("", "", "");

            }


            JSONObject obj = new JSONObject();
            obj.put("Number", kmdm);
            obj.put("Id", "");

            String returnJson = k3CloudApiClient.view("BD_Account", obj.toJSONString());


            if(returnJson.contains("请重新登录")){
                k3CloudApiClient=null;
                getK3CloudSession("", "", "");
                returnJson = k3CloudApiClient.view("BD_Account", obj.toJSONString());
            }

            System.out.println(returnJson);
            JSONObject returnObj = JSONObject.parseObject(returnJson);

            String name = returnObj.getJSONObject("Result").getJSONObject("Result").getJSONArray("Name").getJSONObject(0).getString("Value");
            JSONArray items = returnObj.getJSONObject("Result").getJSONObject("Result").getJSONArray("BD_ACCOUNTFLEXENTRY");
            Account account = null;
            for (int i = 0; i < items.size(); i++) {
                account = new Account();
                JSONObject jsonObjectitem = items.getJSONObject(i);
                String flexNumber = jsonObjectitem.getJSONObject("FlEXITEMPROPERTYID").getString("FlexNumber");
                String flexName = jsonObjectitem.getJSONObject("FlEXITEMPROPERTYID").getJSONArray("MultiLanguageText").getJSONObject(0).getString("Name");
                account.setKmdm(kmdm);
                account.setKmmc(name);
                account.setFlexNumber(flexNumber);
                account.setFlexName(flexName);
                list.add(account);
            }


        } catch (Exception e) {
            log.error("查询科目异常" + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("查询科目异常" + e.getMessage());
        }

        return list;
    }


}
