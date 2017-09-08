package com.dch.vo;

import java.io.Serializable;

public class DrugBaseInfoVo implements Serializable {
    private String id;
    private String name;
    private String code;
    private String className;
    private String toxi;
    private String drugCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getToxi() {
        return toxi;
    }

    public void setToxi(String toxi) {
        this.toxi = toxi;
    }

    public String getDrugCategory() {
        return drugCategory;
    }

    public void setDrugCategory(String drugCategory) {
        this.drugCategory = drugCategory;
    }
}
