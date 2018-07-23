package com.dch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunkqa on 2018/5/30.
 */
public class TemplateReportVo implements Serializable{
    private String id;
    private String templateId;
    private String reportName;
    private String maker;
    private String status;
    private List<TemplateQueryRuleVo> queryRuleVoList;

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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public List<TemplateQueryRuleVo> getQueryRuleVoList() {
        return queryRuleVoList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQueryRuleVoList(List<TemplateQueryRuleVo> queryRuleVoList) {
        this.queryRuleVoList = queryRuleVoList;
    }
}
