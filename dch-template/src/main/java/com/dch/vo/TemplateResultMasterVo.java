package com.dch.vo;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sunkqa on 2018/5/28.
 */
public class TemplateResultMasterVo {
    private String id ;
    private String templateId;
    private String templateName;
    private Double completeRate;
    private String templateResult;
    private Date createDate;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;
    private String flag;//空为未提交，flag不为空为已提交

    public TemplateResultMasterVo() {
    }

    public TemplateResultMasterVo(String id, String templateId, String templateName, Double completeRate, String templateResult, Date createDate, Date modifyDate,
                                  String createBy, String modifyBy, String status,String flag) {
        this.id = id;
        this.templateId = templateId;
        this.templateName = templateName;
        this.completeRate = completeRate;
        this.templateResult = templateResult;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
        this.status = status;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Double getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(Double completeRate) {
        this.completeRate = completeRate;
    }

    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
