package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_element", schema = "dch", catalog = "")
public class DataElement extends BaseEntity {

    private String categoryId;
    private String dataElementName;
    private String dataElementIdentify;
    private String inputCode;
    private String defineDesc;
    private String dataElementCode;
    private String dataElementType;
    private String dataElementState;
    private String dataVersionId ;
    private String dataValueRealmId;
    private String refDataStandardId;
    private String refDataElementId;


    @Basic
    @Column(name = "category_id")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String classId) {
        this.categoryId = classId;
    }

    @Basic
    @Column(name = "data_element_name")
    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    @Basic
    @Column(name = "data_element_identify")
    public String getDataElementIdentify() {
        return dataElementIdentify;
    }

    public void setDataElementIdentify(String dataElementIdentify) {
        this.dataElementIdentify = dataElementIdentify;
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
    @Column(name = "define_desc")
    public String getDefineDesc() {
        return defineDesc;
    }

    public void setDefineDesc(String defineDesc) {
        this.defineDesc = defineDesc;
    }

    @Basic
    @Column(name = "data_element_code")
    public String getDataElementCode() {
        return dataElementCode;
    }

    public void setDataElementCode(String dataElementCode) {
        this.dataElementCode = dataElementCode;
    }

    @Basic
    @Column(name = "data_element_type")
    public String getDataElementType() {
        return dataElementType;
    }

    public void setDataElementType(String dataElementType) {
        this.dataElementType = dataElementType;
    }

    @Basic
    @Column(name = "data_element_state")
    public String getDataElementState() {
        return dataElementState;
    }

    public void setDataElementState(String dataElementState) {
        this.dataElementState = dataElementState;
    }


    @Basic
    @Column(name = "data_value_realm_id")
    public String getDataValueRealmId() {
        return dataValueRealmId;
    }

    public void setDataValueRealmId(String dataValueRealmId) {
        this.dataValueRealmId = dataValueRealmId;
    }

    @Basic
    @Column(name = "ref_data_standard_id")
    public String getRefDataStandardId() {
        return refDataStandardId;
    }

    public void setRefDataStandardId(String refDataStandardId) {
        this.refDataStandardId = refDataStandardId;
    }

    @Basic
    @Column(name = "ref_data_element_id")
    public String getRefDataElementId() {
        return refDataElementId;
    }

    @Basic
    @Column(name="data_version_id")
    public String getDataVersionId() {
        return dataVersionId;
    }

    public void setDataVersionId(String dataVersionId) {
        this.dataVersionId = dataVersionId;
    }

    public void setRefDataElementId(String refDataElementId) {
        this.refDataElementId = refDataElementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataElement that = (DataElement) o;

        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (dataElementName != null ? !dataElementName.equals(that.dataElementName) : that.dataElementName != null)
            return false;
        if (dataElementIdentify != null ? !dataElementIdentify.equals(that.dataElementIdentify) : that.dataElementIdentify != null)
            return false;
        if (inputCode != null ? !inputCode.equals(that.inputCode) : that.inputCode != null) return false;
        if (defineDesc != null ? !defineDesc.equals(that.defineDesc) : that.defineDesc != null) return false;
        if (dataElementCode != null ? !dataElementCode.equals(that.dataElementCode) : that.dataElementCode != null)
            return false;
        if (dataElementType != null ? !dataElementType.equals(that.dataElementType) : that.dataElementType != null)
            return false;
        if (dataElementState != null ? !dataElementState.equals(that.dataElementState) : that.dataElementState != null)
            return false;
       if (dataValueRealmId != null ? !dataValueRealmId.equals(that.dataValueRealmId) : that.dataValueRealmId != null)
            return false;
        if (refDataStandardId != null ? !refDataStandardId.equals(that.refDataStandardId) : that.refDataStandardId != null)
            return false;
        if (refDataElementId != null ? !refDataElementId.equals(that.refDataElementId) : that.refDataElementId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =0;
        result= 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (dataElementName != null ? dataElementName.hashCode() : 0);
        result = 31 * result + (dataElementIdentify != null ? dataElementIdentify.hashCode() : 0);
        result = 31 * result + (inputCode != null ? inputCode.hashCode() : 0);
        result = 31 * result + (defineDesc != null ? defineDesc.hashCode() : 0);
        result = 31 * result + (dataElementCode != null ? dataElementCode.hashCode() : 0);
        result = 31 * result + (dataElementType != null ? dataElementType.hashCode() : 0);
        result = 31 * result + (dataElementState != null ? dataElementState.hashCode() : 0);
        result = 31 * result + (dataValueRealmId != null ? dataValueRealmId.hashCode() : 0);
        result = 31 * result + (refDataStandardId != null ? refDataStandardId.hashCode() : 0);
        result = 31 * result + (refDataElementId != null ? refDataElementId.hashCode() : 0);
        return result;
    }
}
