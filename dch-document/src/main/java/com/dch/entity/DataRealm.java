package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_realm", schema = "dch", catalog = "")
public class DataRealm extends BaseEntity {
    private String categoryId;
    private String dataRealmName;
    private String dataRealmIdentify;
    private String dataVersionId ;
    private String defineDesc;
    private String inputCode;
    private String description;




    @Basic
    @Column(name = "category_id")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "data_realm_name")
    public String getDataRealmName() {
        return dataRealmName;
    }

    public void setDataRealmName(String dataRealmName) {
        this.dataRealmName = dataRealmName;
    }

    @Basic
    @Column(name = "data_realm_identify")
    public String getDataRealmIdentify() {
        return dataRealmIdentify;
    }

    public void setDataRealmIdentify(String dataRealmIdentify) {
        this.dataRealmIdentify = dataRealmIdentify;
    }

    @Basic
    @Column(name = "define_desc")
    public String getDefineDesc() {
        return defineDesc;
    }

    public void setDefineDesc(String defineDesc) {
        this.defineDesc = defineDesc;
    }

    @Basic
    @Column(name = "input_code")
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Basic
    @Column(name="data_version_id")
    public String getDataVersionId() {
        return dataVersionId;
    }

    public void setDataVersionId(String dataVersionId) {
        this.dataVersionId = dataVersionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRealm dataRealm = (DataRealm) o;

        if (categoryId != null ? !categoryId.equals(dataRealm.categoryId) : dataRealm.categoryId != null) return false;
        if (dataRealmName != null ? !dataRealmName.equals(dataRealm.dataRealmName) : dataRealm.dataRealmName != null)
            return false;
        if (dataRealmIdentify != null ? !dataRealmIdentify.equals(dataRealm.dataRealmIdentify) : dataRealm.dataRealmIdentify != null)
            return false;
        if (defineDesc != null ? !defineDesc.equals(dataRealm.defineDesc) : dataRealm.defineDesc != null) return false;
        if (inputCode != null ? !inputCode.equals(dataRealm.inputCode) : dataRealm.inputCode != null) return false;
        if (description != null ? !description.equals(dataRealm.description) : dataRealm.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (dataRealmName != null ? dataRealmName.hashCode() : 0);
        result = 31 * result + (dataRealmIdentify != null ? dataRealmIdentify.hashCode() : 0);
        result = 31 * result + (defineDesc != null ? defineDesc.hashCode() : 0);
        result = 31 * result + (inputCode != null ? inputCode.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
