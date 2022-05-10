package org.jeecg.core.k3api.entity;

import lombok.Data;

/**
 * @author zhengbo
 * @date 2022-03-22
 */
@Data
public class ValidateUser {

    private String acctID;
    private String username;
    private String password;
    private String lcid;

    public  ValidateUser(String acctID,String username,String password,String lcid){
        this.acctID=acctID;
        this.username=username;
        this.password=password;
        this.lcid=lcid;
    }
}
