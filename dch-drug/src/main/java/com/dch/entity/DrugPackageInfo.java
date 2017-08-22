package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_package_info", schema = "dch", catalog = "")
public class DrugPackageInfo extends BaseEntity {
    private String drugId;
    private String packageSpec;
    private String durgBarCode;
    private String drugCode;



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
    @Column(name = "durg_bar_code", nullable = true, length = 20)
    public String getDurgBarCode() {
        return durgBarCode;
    }

    public void setDurgBarCode(String durgBarCode) {
        this.durgBarCode = durgBarCode;
    }

    @Basic
    @Column(name = "drug_code", nullable = true, length = 200)
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

}
