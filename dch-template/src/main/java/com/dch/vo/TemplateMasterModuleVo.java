package com.dch.vo;

import java.util.Date;

/**
 * Created by sunkqa on 2018/3/16.
 */
public class TemplateMasterModuleVo {
    private String id;
    private String templateName;
    private String templateLevel;
    private String templateStatus;
    private String projectId;
    private Integer fillLimit;
    private String templateDesc;
    private String publishStatus;// 0为未发布 1表示已发布
    private String modelId;//所属模块
    private String displayConfig;//展示配置信息
    private Date createDate;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;
    private long fillNum;
    private long commitNum;


    public TemplateMasterModuleVo(String id, String templateName, String templateLevel, String templateStatus, String projectId, Integer fillLimit, String templateDesc, String publishStatus,
                                   String modelId, String displayConfig, Date createDate, Date modifyDate, String createBy, String modifyBy, String status, long fillNum, long commitNum) {
        this.id = id;
        this.templateName = templateName;
        this.templateLevel = templateLevel;
        this.templateStatus = templateStatus;
        this.projectId = projectId;
        this.fillLimit = fillLimit;
        this.templateDesc = templateDesc;
        this.publishStatus = publishStatus;
        this.modelId = modelId;
        this.displayConfig = displayConfig;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
        this.status = status;
        this.fillNum = fillNum;
        this.commitNum = commitNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateLevel() {
        return templateLevel;
    }

    public void setTemplateLevel(String templateLevel) {
        this.templateLevel = templateLevel;
    }

    public String getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(String templateStatus) {
        this.templateStatus = templateStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
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

    public long getFillNum() {
        return fillNum;
    }

    public void setFillNum(long fillNum) {
        this.fillNum = fillNum;
    }

    public long getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(long commitNum) {
        this.commitNum = commitNum;
    }

    public Integer getFillLimit() {
        return fillLimit;
    }

    public void setFillLimit(Integer fillLimit) {
        this.fillLimit = fillLimit;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(String displayConfig) {
        this.displayConfig = displayConfig;
    }
}
