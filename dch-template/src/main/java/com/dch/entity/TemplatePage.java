package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

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

}
