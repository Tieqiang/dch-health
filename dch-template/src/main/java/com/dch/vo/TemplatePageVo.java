package com.dch.vo;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/10/23.
 */
public class TemplatePageVo {
    private String id ;
    private String templatePageName;
    private String templatePageOrder;
    private String templatePageContent;
    private String templateId;
    private Object templatePageDataModel;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplatePageName() {
        return templatePageName;
    }

    public void setTemplatePageName(String templatePageName) {
        this.templatePageName = templatePageName;
    }

    public String getTemplatePageOrder() {
        return templatePageOrder;
    }

    public void setTemplatePageOrder(String templatePageOrder) {
        this.templatePageOrder = templatePageOrder;
    }

    public String getTemplatePageContent() {
        return templatePageContent;
    }

    public void setTemplatePageContent(String templatePageContent) {
        this.templatePageContent = templatePageContent;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Object getTemplatePageDataModel() {
        return templatePageDataModel;
    }

    public void setTemplatePageDataModel(Object templatePageDataModel) {
        this.templatePageDataModel = templatePageDataModel;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
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
