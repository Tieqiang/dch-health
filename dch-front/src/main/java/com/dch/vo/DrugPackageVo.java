package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20.
 */
public class DrugPackageVo implements Serializable{
    private String id;
    private String drugName;
    private String firmName;
    private String drugSpec;
    private String approvalNo;
    private String approvalNoEndDate;

    public DrugPackageVo(String id, String drugName, String firmName, String drugSpec, String approvalNo, String approvalNoEndDate) {
        this.id = id;
        this.drugName = drugName;
        this.firmName = firmName;
        this.drugSpec = drugSpec;
        this.approvalNo = approvalNo;
        this.approvalNoEndDate = approvalNoEndDate;
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

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getDrugSpec() {
        return drugSpec;
    }

    public void setDrugSpec(String drugSpec) {
        this.drugSpec = drugSpec;
    }

    public String getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(String approvalNo) {
        this.approvalNo = approvalNo;
    }

    public String getApprovalNoEndDate() {
        return approvalNoEndDate;
    }

    public void setApprovalNoEndDate(String approvalNoEndDate) {
        this.approvalNoEndDate = approvalNoEndDate;
    }
}
