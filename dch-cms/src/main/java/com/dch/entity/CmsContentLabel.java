package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/23.
 */
@Entity
@Table(name = "cms_content_label", schema = "dch", catalog = "")
public class CmsContentLabel extends BaseEntity {
    private String contentId;
    private String labelName;

    @Basic
    @Column(name = "content_id", nullable = true, length = 64)
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Basic
    @Column(name = "label_name", nullable = true, length = 200)
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

}
