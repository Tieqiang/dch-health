package com.dch.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 授权用户信息
 */
public class Principal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String loginName;
    private String name;
    public String getId() {
        return id;
    }

    public Principal(String id, String loginName, String name) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setName(String name) {
        this.name = name;
    }
}