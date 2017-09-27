package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_result", schema = "dch", catalog = "")
public class TemplateResult extends BaseEntity {
    private String templateId;
    private String templateResult;

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "template_result", nullable = true, length = 255)
    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
    }

}
