package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Administrator on 2017/8/22.
 * 药品品广告
 */
@Entity
@Table(name = "drug_ad", schema = "dch", catalog = "")
public class DrugAd extends BaseEntity {
    private String drugCode;
    private String drugName;
    private String drugId;
    private String adContent;
    private String adType;
    private String adPeriod;
    private String adTime;
    private String adNo;
    private String zipCode;



    @Basic
    @Column(name = "drug_code", nullable = true, length = 20)
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
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
    @Column(name = "ad_content", nullable = true, length = -1)
    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    @Basic
    @Column(name = "ad_type", nullable = true, length = 20)
    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    @Basic
    @Column(name = "ad_period", nullable = true)
    public String getAdPeriod() {
        return adPeriod;
    }

    public void setAdPeriod(String adPeriod) {
        this.adPeriod = adPeriod;
    }

    @Basic
    @Column(name = "ad_time", nullable = true)
    public String getAdTime() {
        return adTime;
    }

    public void setAdTime(String adTime) {
        this.adTime = adTime;
    }

    @Basic
    @Column(name = "ad_no", nullable = true, length = 20)
    public String getAdNo() {
        return adNo;
    }

    public void setAdNo(String adNo) {
        this.adNo = adNo;
    }

    @Basic
    @Column(name = "zip_code", nullable = true, length = 6)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "drug_name", nullable = true, length = -1)
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
