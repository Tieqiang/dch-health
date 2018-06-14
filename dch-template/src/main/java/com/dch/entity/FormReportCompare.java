package com.dch.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by sunkqa on 2018/5/30.
 */
@Entity
@Table(name = "form_report_compare", schema = "dch", catalog = "")
public class FormReportCompare {
    private String id;
    private String relatedTemplateReportId;
    private String relatedTemplateQueryRuleId;
    private String mark;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "related_template_report_id")
    public String getRelatedTemplateReportId() {
        return relatedTemplateReportId;
    }

    public void setRelatedTemplateReportId(String relatedTemplateReportId) {
        this.relatedTemplateReportId = relatedTemplateReportId;
    }

    @Basic
    @Column(name = "related_template_query_rule_id")
    public String getRelatedTemplateQueryRuleId() {
        return relatedTemplateQueryRuleId;
    }

    public void setRelatedTemplateQueryRuleId(String relatedTemplateQueryRuleId) {
        this.relatedTemplateQueryRuleId = relatedTemplateQueryRuleId;
    }

    @Basic
    @Column(name = "mark" , nullable = true, length = -1)
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
