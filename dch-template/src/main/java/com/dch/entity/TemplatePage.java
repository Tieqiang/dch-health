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
@Table(name = "template_page", schema = "dch", catalog = "")
public class TemplatePage extends BaseEntity {
    private String templatePageName;
    private String templatePageOrder;
    private String templatePageContent;
    private String templateId;
    private String templatePageDataModel;

    @Basic
    @Column(name = "template_page_name", nullable = true, length = 200)
    public String getTemplatePageName() {
        return templatePageName;
    }

    public void setTemplatePageName(String templatePageName) {
        this.templatePageName = templatePageName;
    }

    @Basic
    @Column(name = "template_page_order", nullable = true, length = 20)
    public String getTemplatePageOrder() {
        return templatePageOrder;
    }

    public void setTemplatePageOrder(String templatePageOrder) {
        this.templatePageOrder = templatePageOrder;
    }

    @Basic
    @Column(name = "template_page_content", nullable = true, length = -1)
    public String getTemplatePageContent() {
        return templatePageContent;
    }

    public void setTemplatePageContent(String templatePageContent) {
        this.templatePageContent = templatePageContent;
    }

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "template_page_data_model", nullable = true, length = -1)
    public String getTemplatePageDataModel() {
        return templatePageDataModel;
    }

    public void setTemplatePageDataModel(String templatePageDataModel) {
        this.templatePageDataModel = templatePageDataModel;
    }
}
