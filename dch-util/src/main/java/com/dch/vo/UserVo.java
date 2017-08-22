package com.dch.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/16.
 */
public class UserVo implements Serializable {

    private String id ;
    private String loginName ;
    private Date loginTime ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
