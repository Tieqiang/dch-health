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
@Table(name = "template_result_master", schema = "dch", catalog = "")
public class TemplateResultMaster extends BaseEntity {
    private String templateId;
    private String templateName ;
    private Double completeRate;

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

    @Basic
    @Column(name="complete_rate")
    public Double getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(Double completeRate) {
        if(completeRate==null){
            completeRate = 0D;
        }
        this.completeRate = completeRate;
    }
}
