package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_name_dict", schema = "dch", catalog = "")
public class DrugNameDict extends BaseEntity {
    private String drugCode;
    private String drugNameType;
    private String drugName;
    private String inputCode;
    private String drugId ;


    @Basic
    @Column(name = "drug_code", nullable = true, length = 20)
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    @Basic
    @Column(name = "drug_name_type", nullable = true, length = 100)
    public String getDrugNameType() {
        return drugNameType;
    }

    public void setDrugNameType(String drugNameType) {
        this.drugNameType = drugNameType;
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
    @Column(name = "input_code", nullable = true, length = 100)
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Column(name="drug_id")
    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }
}
