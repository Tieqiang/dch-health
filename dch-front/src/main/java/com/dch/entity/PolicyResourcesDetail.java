package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/1/10.
 */
@Entity
@Table(name = "policy_resources_detail", schema = "dch", catalog = "")
public class PolicyResourcesDetail extends BaseEntity {
    private String relatedPolicyId;
    private Integer pageNumber;
    private String content;

    @Basic
    @Column(name = "related_policy_id", nullable = true, length = 64)
    public String getRelatedPolicyId() {
        return relatedPolicyId;
    }

    public void setRelatedPolicyId(String relatedPolicyId) {
        this.relatedPolicyId = relatedPolicyId;
    }

    @Basic
    @Column(name = "page_number", nullable = true)
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
