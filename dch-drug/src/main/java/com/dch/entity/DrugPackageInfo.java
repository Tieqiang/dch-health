package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 药品规格信息
 */
@Entity
@Table(name = "drug_package_info", schema = "dch", catalog = "")
public class DrugPackageInfo extends BaseEntity {
    private String drugId;
    private String packageSpec;
    private String drugBarCode;
    private String drugCode;
    private String firmId ;
    private String firmName ;
    private String drugSpec;
    private String approvalNo;
    private String approvalNoEndDate;
    @Basic
    @Column(name = "drug_spec", nullable = true, length = 400)
    public String getDrugSpec() { return drugSpec; }

    public void setDrugSpec(String drugSpec) { this.drugSpec = drugSpec; }


    @Basic
    @Column(name = "approval_no", nullable = true, length = 500)
    public String getApprovalNo() { return approvalNo; }

    public void setApprovalNo(String approvalNo) { this.approvalNo = approvalNo; }

    @Basic
    @Column(name = "approval_no_end_date", nullable = true, length = 50)
    public String getApprovalNoEndDate() { return approvalNoEndDate; }

    public void setApprovalNoEndDate(String approvalNoEndDate) {  this.approvalNoEndDate = approvalNoEndDate; }

    @Basic
    @Column(name = "firm_id", nullable = true, length = 64)
    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }
    @Basic
    @Column(name = "drug_id", nullable = true, length = 64)
    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "package_spec", nullable = true, length = 20)
    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    @Basic
    @Column(name = "drug_bar_code", nullable = true, length = 20)
    public String getDrugBarCode() {
        return drugBarCode;
    }

    public void setDrugBarCode(String drugBarCode) {
        this.drugBarCode = drugBarCode;
    }

    @Basic
    @Column(name = "drug_code", nullable = true, length = 200)
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }
    @Basic
    @Column(name="firm_name")
    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
}
