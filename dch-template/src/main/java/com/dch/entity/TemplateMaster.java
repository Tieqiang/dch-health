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
@Table(name = "template_master", schema = "dch", catalog = "")
public class TemplateMaster extends BaseEntity {
    private String templateName;
    private String templateLevel;
    private String templateStatus;
    private String projectId;
    private Integer fillLimit;
    private String templateDesc;
    private String publishStatus;// 0为未发布 1表示已发布
    private String modelId;//所属模块
    private String displayConfig;//展示配置信息


    @Basic
    @Column(name = "publish_status", nullable = true, length = 10)
    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

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

    @Basic
    @Column(name="fill_limit")
    public Integer getFillLimit() {
        return fillLimit;
    }

    public void setFillLimit(Integer fillLimit) {
        if(fillLimit==null){
            fillLimit =0;
        }
        this.fillLimit = fillLimit;
    }

    @Basic
    @Column(name = "model_id", nullable = true, length = 64)
    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @Basic
    @Column(name = "display_config", nullable = true, length = -1)
    public String getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(String displayConfig) {
        this.displayConfig = displayConfig;
    }
}
