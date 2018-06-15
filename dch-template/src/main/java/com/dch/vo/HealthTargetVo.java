package com.dch.vo;

import com.dch.entity.FutureTarget;
import com.dch.entity.TargetLevel;

import java.util.List;

/**
 * Created by sunkqa on 2018/6/14.
 */
public class HealthTargetVo {
    private String id;
    private String name;
    private String parentId;
    private Integer grade;
    private String dependFile;
    private String step;
    private String computeMethod;
    private String similarCountry;
    private String otherCountryValue;
    private String otherCountryStep;
    private String status;
    private List<TargetLevel> targetLevelList;
    private List<FutureTarget> futureTargetList;


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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getDependFile() {
        return dependFile;
    }

    public void setDependFile(String dependFile) {
        this.dependFile = dependFile;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getComputeMethod() {
        return computeMethod;
    }

    public void setComputeMethod(String computeMethod) {
        this.computeMethod = computeMethod;
    }

    public String getSimilarCountry() {
        return similarCountry;
    }

    public void setSimilarCountry(String similarCountry) {
        this.similarCountry = similarCountry;
    }

    public String getOtherCountryValue() {
        return otherCountryValue;
    }

    public void setOtherCountryValue(String otherCountryValue) {
        this.otherCountryValue = otherCountryValue;
    }

    public String getOtherCountryStep() {
        return otherCountryStep;
    }

    public void setOtherCountryStep(String otherCountryStep) {
        this.otherCountryStep = otherCountryStep;
    }

    public List<TargetLevel> getTargetLevelList() {
        return targetLevelList;
    }

    public void setTargetLevelList(List<TargetLevel> targetLevelList) {
        this.targetLevelList = targetLevelList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FutureTarget> getFutureTargetList() {
        return futureTargetList;
    }

    public void setFutureTargetList(List<FutureTarget> futureTargetList) {
        this.futureTargetList = futureTargetList;
    }
}
