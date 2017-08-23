package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/23.
 */
@Entity
@Table(name = "cms_content", schema = "dch", catalog = "")
public class CmsContent extends BaseEntity{

    private String categoryId;
    private String contentTitle;
    private String content;
    private Date pubTime;
    private String pubStatus;
    private String facePic;



    @Basic
    @Column(name = "category_id", nullable = true, length = 64)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "content_title", nullable = true, length = 200)
    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "pub_time", nullable = true)
    public Date getPubTime() {
        return pubTime;
    }

    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    @Basic
    @Column(name = "pub_status", nullable = true, length = 2)
    public String getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }

    @Basic
    @Column(name = "face_pic", nullable = true, length = 200)
    public String getFacePic() {
        return facePic;
    }

    public void setFacePic(String facePic) {
        this.facePic = facePic;
    }

}
