package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_exam_org", schema = "dch", catalog = "")
public class DrugExamOrg extends BaseEntity{
    private String identificationNo;
    private String medicalOrgName;
    private String medicalOrgAddress;
    private String identificationProfession;
    private Date identificationDate;




    @Basic
    @Column(name = "identification_no", nullable = true, length = 20)
    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    @Basic
    @Column(name = "medical_org_name", nullable = true, length = 200)
    public String getMedicalOrgName() {
        return medicalOrgName;
    }

    public void setMedicalOrgName(String medicalOrgName) {
        this.medicalOrgName = medicalOrgName;
    }

    @Basic
    @Column(name = "medical_org_address", nullable = true, length = 50)
    public String getMedicalOrgAddress() {
        return medicalOrgAddress;
    }

    public void setMedicalOrgAddress(String medicalOrgAddress) {
        this.medicalOrgAddress = medicalOrgAddress;
    }

    @Basic
    @Column(name = "identification_profession", nullable = true, length = -1)
    public String getIdentificationProfession() {
        return identificationProfession;
    }

    public void setIdentificationProfession(String identificationProfession) {
        this.identificationProfession = identificationProfession;
    }

    @Basic
    @Column(name = "identification_date", nullable = true)
    public Date getIdentificationDate() {
        return identificationDate;
    }

    public void setIdentificationDate(Date identificationDate) {
        this.identificationDate = identificationDate;
    }

}
