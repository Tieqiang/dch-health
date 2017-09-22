package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/21.
 */
public class DrugCountryVo implements Serializable{
    private String id;
    private String drugName;
    private String drugCode;
    private String className;
    private String spec;
    private String toxi;
    private String drugCategory;
    private String rxFlag;
    private String country;

    public DrugCountryVo(String id, String drugName, String drugCode, String className, String spec, String toxi, String drugCategory, String rxFlag, String country) {
        this.id = id;
        this.drugName = drugName;
        this.drugCode = drugCode;
        this.className = className;
        this.spec = spec;
        this.toxi = toxi;
        this.drugCategory = drugCategory;
        this.rxFlag = rxFlag;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
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

    public String getRxFlag() {
        return rxFlag;
    }

    public void setRxFlag(String rxFlag) {
        this.rxFlag = rxFlag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
