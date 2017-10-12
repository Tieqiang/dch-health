package com.dch.vo;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/11.
 */
public class TemplateDataElementVo {
    private String id ;
    private String dataElementName;
    private String dataElementCode;
    private String parentDataId;
    private String dataStandRefId;
    private String dataStandRefName;
    private String dataElementType;
    private String dataUnion;
    private String childrenFlag;
    private String dataGroupId;
    private String hasGroupId;
    private String templateId;
    private Date createDate;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;

    public TemplateDataElementVo(String id, String dataElementName, String dataElementCode, String parentDataId, String dataStandRefId, String dataStandRefName, String dataElementType, String dataUnion, String childrenFlag, String dataGroupId, String hasGroupId, String templateId, Date createDate, Date modifyDate, String createBy, String modifyBy, String status) {
        this.id = id;
        this.dataElementName = dataElementName;
        this.dataElementCode = dataElementCode;
        this.parentDataId = parentDataId;
        this.dataStandRefId = dataStandRefId;
        this.dataStandRefName = dataStandRefName;
        this.dataElementType = dataElementType;
        this.dataUnion = dataUnion;
        this.childrenFlag = childrenFlag;
        this.dataGroupId = dataGroupId;
        this.hasGroupId = hasGroupId;
        this.templateId = templateId;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    public String getDataElementCode() {
        return dataElementCode;
    }

    public void setDataElementCode(String dataElementCode) {
        this.dataElementCode = dataElementCode;
    }

    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }

    public String getDataStandRefId() {
        return dataStandRefId;
    }

    public void setDataStandRefId(String dataStandRefId) {
        this.dataStandRefId = dataStandRefId;
    }

    public String getDataStandRefName() {
        return dataStandRefName;
    }

    public void setDataStandRefName(String dataStandRefName) {
        this.dataStandRefName = dataStandRefName;
    }

    public String getDataElementType() {
        return dataElementType;
    }

    public void setDataElementType(String dataElementType) {
        this.dataElementType = dataElementType;
    }

    public String getDataUnion() {
        return dataUnion;
    }

    public void setDataUnion(String dataUnion) {
        this.dataUnion = dataUnion;
    }

    public String getChildrenFlag() {
        return childrenFlag;
    }

    public void setChildrenFlag(String childrenFlag) {
        this.childrenFlag = childrenFlag;
    }

    public String getDataGroupId() {
        return dataGroupId;
    }

    public void setDataGroupId(String dataGroupId) {
        this.dataGroupId = dataGroupId;
    }

    public String getHasGroupId() {
        return hasGroupId;
    }

    public void setHasGroupId(String hasGroupId) {
        this.hasGroupId = hasGroupId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
