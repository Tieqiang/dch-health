package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_data_element", schema = "dch", catalog = "")
public class TemplateDataElement extends BaseEntity{
    private String dataElementName;
    private String dataElementCode;
    private String parentDataId;
    private String dataStandRefId;
    private String dataElementType;
    private String dataUnion;
    private String childrenFlag;
    private String dataGroupId;
    private String hasGroupId;
    private String templateId;

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() { return templateId; }

    public void setTemplateId(String templateId) { this.templateId = templateId; }

    @Basic
    @Column(name = "data_element_name", nullable = true, length = 200)
    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    @Basic
    @Column(name = "data_element_code", nullable = true, length = 200)
    public String getDataElementCode() {
        return dataElementCode;
    }

    public void setDataElementCode(String dataElementCode) {
        this.dataElementCode = dataElementCode;
    }

    @Basic
    @Column(name = "parent_data_id", nullable = true, length = 64)
    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }

    @Basic
    @Column(name = "data_stand_ref_id", nullable = true, length = 64)
    public String getDataStandRefId() {
        return dataStandRefId;
    }

    public void setDataStandRefId(String dataStandRefId) {
        this.dataStandRefId = dataStandRefId;
    }

    @Basic
    @Column(name = "data_element_type", nullable = true, length = 200)
    public String getDataElementType() {
        return dataElementType;
    }

    public void setDataElementType(String dataElementType) {
        this.dataElementType = dataElementType;
    }

    @Basic
    @Column(name = "data_union", nullable = true, length = 500)
    public String getDataUnion() {
        return dataUnion;
    }

    public void setDataUnion(String dataUnion) {
        this.dataUnion = dataUnion;
    }

    @Basic
    @Column(name = "children_flag", nullable = true, length = 10)
    public String getChildrenFlag() {
        return childrenFlag;
    }

    public void setChildrenFlag(String childrenFlag) {
        this.childrenFlag = childrenFlag;
    }

    @Basic
    @Column(name = "data_group_id", nullable = true, length = 64)
    public String getDataGroupId() {
        return dataGroupId;
    }

    public void setDataGroupId(String dataGroupId) {
        this.dataGroupId = dataGroupId;
    }

    @Basic
    @Column(name = "has_group_id", nullable = true, length = 64)
    public String getHasGroupId() {
        return hasGroupId;
    }

    public void setHasGroupId(String hasGroupId) {
        this.hasGroupId = hasGroupId;
    }

}
