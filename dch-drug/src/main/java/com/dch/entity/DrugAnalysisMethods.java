package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_analysis_methods", schema = "dch", catalog = "")
public class DrugAnalysisMethods extends BaseEntity {
    private String fileId;
    private String methodName;
    private String userRealm;
    private String methodPrinciple;
    private String equipment;
    private String demo;
    private String operateStep;
    private String reference;
    private String memo;



    @Basic
    @Column(name = "file_id", nullable = true, length = 64)
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "method_name", nullable = true, length = 100)
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Basic
    @Column(name = "user_realm", nullable = true, length = -1)
    public String getUserRealm() {
        return userRealm;
    }

    public void setUserRealm(String userRealm) {
        this.userRealm = userRealm;
    }

    @Basic
    @Column(name = "method_principle", nullable = true, length = -1)
    public String getMethodPrinciple() {
        return methodPrinciple;
    }

    public void setMethodPrinciple(String methodPrinciple) {
        this.methodPrinciple = methodPrinciple;
    }

    @Basic
    @Column(name = "equipment", nullable = true, length = -1)
    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    @Basic
    @Column(name = "demo", nullable = true, length = -1)
    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    @Basic
    @Column(name = "operate_step", nullable = true, length = -1)
    public String getOperateStep() {
        return operateStep;
    }

    public void setOperateStep(String operateStep) {
        this.operateStep = operateStep;
    }

    @Basic
    @Column(name = "reference", nullable = true, length = -1)
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Basic
    @Column(name = "memo", nullable = true, length = -1)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
