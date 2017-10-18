package com.dch.vo;


import java.util.Date;

public class TemplateMasterVo  {

    private String id;
    private String templateName;
    private String templateLevel;
    private String templateStatus;
    private String projectId;
    private String templateDesc;
    private Date createDate;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;
    private long num;

    public TemplateMasterVo(String id, String templateName, String templateLevel, String templateStatus, String projectId, String templateDesc, Date createDate, Date modifyDate, String createBy, String modifyBy, String status, long num) {
        this.id = id;
        this.templateName = templateName;
        this.templateLevel = templateLevel;
        this.templateStatus = templateStatus;
        this.projectId = projectId;
        this.templateDesc = templateDesc;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
        this.status = status;
        this.num = num;
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

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
