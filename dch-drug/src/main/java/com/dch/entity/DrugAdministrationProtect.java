package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 药品行政政策
 */
@Entity
@Table(name = "drug_administration_protect", schema = "dch", catalog = "")
public class DrugAdministrationProtect extends BaseEntity {
    private String drugId;
    private String applyer;
    private String applyerCountryCn;
    private String applyerCountryEn;
    private String authorizeNo;
    private Date authorizeDate;
    private String pubNo;
    private String drugName ;




    @Basic
    @Column(name = "drug_id", nullable = true, length = 64)
    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "applyer", nullable = true, length = 100)
    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    @Basic
    @Column(name = "applyer_country_cn", nullable = true, length = 200)
    public String getApplyerCountryCn() {
        return applyerCountryCn;
    }

    public void setApplyerCountryCn(String applyerCountryCn) {
        this.applyerCountryCn = applyerCountryCn;
    }

    @Basic
    @Column(name = "applyer_country_en", nullable = true, length = 200)
    public String getApplyerCountryEn() {
        return applyerCountryEn;
    }

    public void setApplyerCountryEn(String applyerCountryEn) {
        this.applyerCountryEn = applyerCountryEn;
    }

    @Basic
    @Column(name = "authorize_no", nullable = true, length = 50)
    public String getAuthorizeNo() {
        return authorizeNo;
    }

    public void setAuthorizeNo(String authorizeNo) {
        this.authorizeNo = authorizeNo;
    }

    @Basic
    @Column(name = "authorize_date", nullable = true)
    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    @Basic
    @Column(name = "pub_no", nullable = true, length = 50)
    public String getPubNo() {
        return pubNo;
    }

    public void setPubNo(String pubNo) {
        this.pubNo = pubNo;
    }

    @Column(name="drug_name")
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
