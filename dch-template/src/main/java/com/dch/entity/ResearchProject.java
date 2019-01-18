package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.springframework.stereotype.Controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "research_project", schema = "dch", catalog = "")
public class ResearchProject extends BaseEntity {

    private String projectName;

    private String orgId;

    private String projectCode;

    private String reportPerson;//填报人

    private String templateId;//对应的表单

    private String resultId;//对应的填报结果


    private String projectType;

    @Column(name = "project_type")
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "org_id")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Column(name = "project_code")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "report_person")
    public String getReportPerson() {
        return reportPerson;
    }

    public void setReportPerson(String reportPerson) {
        this.reportPerson = reportPerson;
    }

    @Column(name = "template_id")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name = "result_id")
    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
}
