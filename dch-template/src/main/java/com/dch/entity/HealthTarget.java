package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunkqa on 2018/6/14.
 */
@Entity
@Table(name = "health_target", schema = "dch", catalog = "")
public class HealthTarget extends BaseEntity{
    private String name;
    private String parentId;
    private Integer grade;
    private String dependFile;
    private String step;
    private String computeMethod;
    private String similarCountry;
    private String otherCountryValue;
    private String otherCountryStep;

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "grade")
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "depend_file")
    public String getDependFile() {
        return dependFile;
    }

    public void setDependFile(String dependFile) {
        this.dependFile = dependFile;
    }

    @Basic
    @Column(name = "step")
    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Basic
    @Column(name = "compute_method")
    public String getComputeMethod() {
        return computeMethod;
    }

    public void setComputeMethod(String computeMethod) {
        this.computeMethod = computeMethod;
    }

    @Basic
    @Column(name = "similar_country")
    public String getSimilarCountry() {
        return similarCountry;
    }

    public void setSimilarCountry(String similarCountry) {
        this.similarCountry = similarCountry;
    }

    @Basic
    @Column(name = "other_country_value")
    public String getOtherCountryValue() {
        return otherCountryValue;
    }

    public void setOtherCountryValue(String otherCountryValue) {
        this.otherCountryValue = otherCountryValue;
    }

    @Basic
    @Column(name = "other_country_step")
    public String getOtherCountryStep() {
        return otherCountryStep;
    }

    public void setOtherCountryStep(String otherCountryStep) {
        this.otherCountryStep = otherCountryStep;
    }

}
