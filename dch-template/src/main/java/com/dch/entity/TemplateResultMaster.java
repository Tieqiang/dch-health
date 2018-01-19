package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_result_master", schema = "dch", catalog = "")
public class TemplateResultMaster extends BaseEntity {
    private String templateId;
    private String templateName ;
    private Double completeRate;
    private String templateResult ;

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

    @Transient
    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
    }
}
