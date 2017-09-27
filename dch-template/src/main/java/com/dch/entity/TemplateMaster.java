package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_master", schema = "dch", catalog = "")
public class TemplateMaster extends BaseEntity {
    private String templateName;
    private String templateLevel;
    private String templateStatus;
    private String projectId;
    private String templateDesc;

    @Basic
    @Column(name = "template_name", nullable = true, length = 200)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Basic
    @Column(name = "template_level", nullable = true, length = 10)
    public String getTemplateLevel() {
        return templateLevel;
    }

    public void setTemplateLevel(String templateLevel) {
        this.templateLevel = templateLevel;
    }

    @Basic
    @Column(name = "template_status", nullable = true, length = 10)
    public String getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(String templateStatus) {
        this.templateStatus = templateStatus;
    }

    @Basic
    @Column(name = "project_id", nullable = true, length = 64)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Basic
    @Column(name = "template_desc", nullable = true, length = -1)
    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

}
