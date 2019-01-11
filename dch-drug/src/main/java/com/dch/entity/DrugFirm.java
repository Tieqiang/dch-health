package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 药品厂商
 */
@Entity
@Table(name = "drug_firm", schema = "dch", catalog = "")
public class DrugFirm extends BaseEntity{
    private String firmName;
    private String inputCode;
    private String addressProvince;
    private String firmType;
    private String addressRegist;
    private String addressProduce;
    private String produceRealm;
    private String issueDate;
    private String licenceNo;
    private String foreignFlag;


    @Basic
    @Column(name = "firm_name", nullable = true, length = 100)
    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    @Basic
    @Column(name = "input_code", nullable = true, length = 50)
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Basic
    @Column(name = "address_province", nullable = true, length = 6)
    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    @Basic
    @Column(name = "firm_type", nullable = true, length = 100)
    public String getFirmType() {
        return firmType;
    }

    public void setFirmType(String firmType) {
        this.firmType = firmType;
    }

    @Basic
    @Column(name = "address_regist", nullable = true, length = 200)
    public String getAddressRegist() {
        return addressRegist;
    }

    public void setAddressRegist(String addressRegist) {
        this.addressRegist = addressRegist;
    }

    @Basic
    @Column(name = "address_produce", nullable = true, length = 200)
    public String getAddressProduce() {
        return addressProduce;
    }

    public void setAddressProduce(String addressProduce) {
        this.addressProduce = addressProduce;
    }

    @Basic
    @Column(name = "produce_realm", nullable = true, length = -1)
    public String getProduceRealm() {
        return produceRealm;
    }

    public void setProduceRealm(String produceRealm) {
        this.produceRealm = produceRealm;
    }

    @Basic
    @Column(name = "issue_date", nullable = true)
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    @Basic
    @Column(name = "licence_no", nullable = true, length = 20)
    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    @Column(name = "foreign_flag")
    public String getForeignFlag() {
        return foreignFlag;
    }

    public void setForeignFlag(String foreignFlag) {
        this.foreignFlag = foreignFlag;
    }
}
