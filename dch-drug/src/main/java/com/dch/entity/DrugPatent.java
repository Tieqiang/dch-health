package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_patent", schema = "dch", catalog = "")
public class DrugPatent extends BaseEntity{
    private String pubNo;
    private Date pubDate;
    private String patentNo;
    private Date patentDate;
    private String patentName;
    private String masterClassNo;
    private String classNo;
    private String orginApplyNo;
    private String priority;
    private String applyer;
    private String inventor;
    private String address;
    private Date approvalDate;
    private String internationalApplication;
    private Date enterDate;
    private String patentAgency;
    private String agencyPerson;
    private String countryProvince;
    private String principalClaim;
    private String internationalPub;
    private String abstractContent;

    @Basic
    @Column(name = "pub_no", nullable = true, length = 20)
    public String getPubNo() {
        return pubNo;
    }

    public void setPubNo(String pubNo) {
        this.pubNo = pubNo;
    }

    @Basic
    @Column(name = "pub_date", nullable = true)
    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Basic
    @Column(name = "patent_no", nullable = true, length = 20)
    public String getPatentNo() {
        return patentNo;
    }

    public void setPatentNo(String patentNo) {
        this.patentNo = patentNo;
    }

    @Basic
    @Column(name = "patent_date", nullable = true)
    public Date getPatentDate() {
        return patentDate;
    }

    public void setPatentDate(Date patentDate) {
        this.patentDate = patentDate;
    }

    @Basic
    @Column(name = "patent_name", nullable = true, length = 200)
    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    @Basic
    @Column(name = "master_class_no", nullable = true, length = 20)
    public String getMasterClassNo() {
        return masterClassNo;
    }

    public void setMasterClassNo(String masterClassNo) {
        this.masterClassNo = masterClassNo;
    }

    @Basic
    @Column(name = "class_no", nullable = true, length = 20)
    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    @Basic
    @Column(name = "orgin_apply_no", nullable = true, length = 20)
    public String getOrginApplyNo() {
        return orginApplyNo;
    }

    public void setOrginApplyNo(String orginApplyNo) {
        this.orginApplyNo = orginApplyNo;
    }

    @Basic
    @Column(name = "priority", nullable = true, length = -1)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "applyer", nullable = true, length = 200)
    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    @Basic
    @Column(name = "inventor", nullable = true, length = 200)
    public String getInventor() {
        return inventor;
    }

    public void setInventor(String inventor) {
        this.inventor = inventor;
    }

    @Basic
    @Column(name = "address", nullable = true, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "approval_date", nullable = true)
    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    @Basic
    @Column(name = "international_application", nullable = true, length = -1)
    public String getInternationalApplication() {
        return internationalApplication;
    }

    public void setInternationalApplication(String internationalApplication) {
        this.internationalApplication = internationalApplication;
    }

    @Basic
    @Column(name = "enter_date", nullable = true)
    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    @Basic
    @Column(name = "patent_agency", nullable = true, length = 200)
    public String getPatentAgency() {
        return patentAgency;
    }

    public void setPatentAgency(String patentAgency) {
        this.patentAgency = patentAgency;
    }

    @Basic
    @Column(name = "agency_person", nullable = true, length = 200)
    public String getAgencyPerson() {
        return agencyPerson;
    }

    public void setAgencyPerson(String agencyPerson) {
        this.agencyPerson = agencyPerson;
    }

    @Basic
    @Column(name = "country_province", nullable = true, length = 20)
    public String getCountryProvince() {
        return countryProvince;
    }

    public void setCountryProvince(String countryProvince) {
        this.countryProvince = countryProvince;
    }

    @Basic
    @Column(name = "principal_claim", nullable = true, length = -1)
    public String getPrincipalClaim() {
        return principalClaim;
    }

    public void setPrincipalClaim(String principalClaim) {
        this.principalClaim = principalClaim;
    }

    @Basic
    @Column(name = "international_pub", nullable = true, length = -1)
    public String getInternationalPub() {
        return internationalPub;
    }

    public void setInternationalPub(String internationalPub) {
        this.internationalPub = internationalPub;
    }

}
