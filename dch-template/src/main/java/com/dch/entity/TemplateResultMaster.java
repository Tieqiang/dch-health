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
@Table(name = "template_result", schema = "dch", catalog = "")
public class TemplateResultMaster extends BaseEntity {
    private String templateId;
    private String templateName ;

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name="template_name")
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
