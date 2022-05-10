package org.jeecg.core.k3api.service;

import org.jeecg.core.k3api.entity.Account;
import org.jeecg.modules.online.config.exception.BusinessException;

import java.util.List;

/**
 * @author zhengbo
 * @date 2022-03-22
 */
public interface K3ApiService {


    public  String saveVoucher(String sessionid,String formid ,String jsonData);


    public  Boolean getK3CloudSession(String dbid,String userid,String password) throws Exception;

    List<Account> getK3CloudAccount(String kmdm) throws BusinessException;

}
