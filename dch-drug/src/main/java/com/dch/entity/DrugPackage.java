package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 药品包材
 */
@Entity
@Table(name = "drug_package", schema = "dch", catalog = "")
public class DrugPackage extends BaseEntity {
    private String packageName;
    private String registerNo;
    private String companyName;
    private String productOrigin;
    private Date approvalDate;
    private String spec;

    @Basic
    @Column(name = "package_name", nullable = true, length = 200)
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Basic
    @Column(name = "register_no", nullable = true, length = 200)
    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    @Basic
    @Column(name = "company_name", nullable = true, length = 200)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "product_origin", nullable = true, length = -1)
    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
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
    @Column(name = "spec", nullable = true, length = 200)
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

}
