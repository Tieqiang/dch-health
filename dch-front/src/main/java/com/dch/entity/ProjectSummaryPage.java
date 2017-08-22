package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "project_summary_page", schema = "dch", catalog = "")
public class ProjectSummaryPage extends BaseEntity {
    private String pageName;
    private String icon;
    private String content;
    private String publishStatus;
    private String projectId;
    private Integer orderNo;

    @Basic
    @Column(name = "page_name")
    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String categoryContent) {
        this.content = categoryContent;
    }

    @Basic
    @Column(name = "publish_status")
    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    @Basic
    @Column(name = "project_id")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Basic
    @Column(name = "order_no")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectSummaryPage that = (ProjectSummaryPage) o;

        if (pageName != null ? !pageName.equals(that.pageName) : that.pageName != null) return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null)
            return false;
        if (publishStatus != null ? !publishStatus.equals(that.publishStatus) : that.publishStatus != null)
            return false;
        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) return false;
        if (orderNo != null ? !orderNo.equals(that.orderNo) : that.orderNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (publishStatus != null ? publishStatus.hashCode() : 0);
        result = 31 * result + (projectId != null ? projectId.hashCode() : 0);
        result = 31 * result + (orderNo != null ? orderNo.hashCode() : 0);
        return result;
    }
}
