package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/9/18.
 */
public class TemplateReportRuleVo {
    private String id;
    private String templateId;
    private String ruleName;
    private String content;
    private String ruleDesc;//描述
    private String createBy;
    private String modifyBy;
    private List<TemplateReportRuleVo> reportRuleVoList;

    public TemplateReportRuleVo(String id, String templateId, String ruleName, String content, String ruleDesc, String createBy, String modifyBy) {
        this.id = id;
        this.templateId = templateId;
        this.ruleName = ruleName;
        this.content = content;
        this.ruleDesc = ruleDesc;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
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

    public List<TemplateReportRuleVo> getReportRuleVoList() {
        return reportRuleVoList;
    }

    public void setReportRuleVoList(List<TemplateReportRuleVo> reportRuleVoList) {
        this.reportRuleVoList = reportRuleVoList;
    }
}
