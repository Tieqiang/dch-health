package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_element_standard", schema = "dch", catalog = "")
public class DataElementStandard extends BaseEntity {
    private String register;
    private String dataStandardName ;
    private String relativeEnv;
    private String classMethod;
    private String manageOrg;
    private String commitOrg;



    @Basic
    @Column(name = "register")
    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    @Basic
    @Column(name = "relative_env")
    public String getRelativeEnv() {
        return relativeEnv;
    }

    public void setRelativeEnv(String relativeEnv) {
        this.relativeEnv = relativeEnv;
    }

    @Basic
    @Column(name = "class_method")
    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    @Basic
    @Column(name = "manage_org")
    public String getManageOrg() {
        return manageOrg;
    }

    public void setManageOrg(String manageOrg) {
        this.manageOrg = manageOrg;
    }


    @Basic
    @Column(name = "commit_org")
    public String getCommitOrg() {
        return commitOrg;
    }

    public void setCommitOrg(String commitOrg) {
        this.commitOrg = commitOrg;
    }


    @Column(name="data_standard_name")
    public String getDataStandardName() {
        return dataStandardName;
    }

    public void setDataStandardName(String dataStandardName) {
        this.dataStandardName = dataStandardName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataElementStandard that = (DataElementStandard) o;

        if (register != null ? !register.equals(that.register) : that.register != null) return false;
        if (relativeEnv != null ? !relativeEnv.equals(that.relativeEnv) : that.relativeEnv != null) return false;
        if (classMethod != null ? !classMethod.equals(that.classMethod) : that.classMethod != null) return false;
        if (manageOrg != null ? !manageOrg.equals(that.manageOrg) : that.manageOrg != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (register != null ? register.hashCode() : 0);
        result = 31 * result + (relativeEnv != null ? relativeEnv.hashCode() : 0);
        result = 31 * result + (classMethod != null ? classMethod.hashCode() : 0);
        result = 31 * result + (manageOrg != null ? manageOrg.hashCode() : 0);
        return result;
    }
}
