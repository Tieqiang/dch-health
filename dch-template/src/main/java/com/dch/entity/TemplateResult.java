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
@Table(name = "template_result", schema = "dch", catalog = "")
public class TemplateResult extends BaseEntity {
    private String templateId;
    private String templateResult;
    private String pageId;
    private String docId;

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "template_result", nullable = true, length = -1)
    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
    }

    @Basic
    @Column(name = "page_id", nullable = true, length = -1)
    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @Basic
    @Column(name = "doc_id", nullable = true, length = 64)
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
