package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_base_info", schema = "dch", catalog = "")
public class DrugBaseInfo extends BaseEntity{
    private String approvalNo;
    private Date approvalNoEndDate;
    private String spec;
    private String toxi;
    private String classId;
    private String drugCategory;
    private String drugStandardNo;
    private String drugCode;
    private String drugName;
    private String importFlag;
    private String rxFlag;


    @Basic
    @Column(name = "approval_no", nullable = true)
    public String getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(String approvalNo) {
        this.approvalNo = approvalNo;
    }



    @Basic
    @Column(name = "approval_no_end_date", nullable = true)
    public Date getApprovalNoEndDate() {
        return approvalNoEndDate;
    }

    public void setApprovalNoEndDate(Date approvalNoEndDate) {
        this.approvalNoEndDate = approvalNoEndDate;
    }

    @Basic
    @Column(name = "spec", nullable = true, length = 20)
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Basic
    @Column(name = "toxi", nullable = true, length = 20)
    public String getToxi() {
        return toxi;
    }

    public void setToxi(String toxi) {
        this.toxi = toxi;
    }

    @Basic
    @Column(name = "class_id", nullable = true, length = 64)
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "drug_category", nullable = true, length = 20)
    public String getDrugCategory() {
        return drugCategory;
    }

    public void setDrugCategory(String drugCategory) {
        this.drugCategory = drugCategory;
    }

    @Basic
    @Column(name = "drug_standard_no", nullable = true, length = 50)
    public String getDrugStandardNo() {
        return drugStandardNo;
    }

    public void setDrugStandardNo(String drugStandardNo) {
        this.drugStandardNo = drugStandardNo;
    }

    @Basic
    @Column(name = "drug_code", nullable = true, length = 20)
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    @Basic
    @Column(name = "drug_name", nullable = true, length = 200)
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    @Basic
    @Column(name = "import_flag", nullable = true, length = 2)
    public String getImportFlag() {
        return importFlag;
    }

    public void setImportFlag(String importFlag) {
        this.importFlag = importFlag;
    }

    @Basic
    @Column(name = "rx_flag", nullable = true, length = 2)
    public String getRxFlag() {
        return rxFlag;
    }

    public void setRxFlag(String rxFlag) {
        this.rxFlag = rxFlag;
    }

}
