package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunkqa on 2018/3/19.
 */
@Entity
@Table(name = "template_result_support", schema = "dch", catalog = "")
public class TemplateResultSupport extends BaseEntity {
    private String relatedMasterId;
    private String relatedFileId;

    @Basic
    @Column(name = "related_master_id")
    public String getRelatedMasterId() {
        return relatedMasterId;
    }

    public void setRelatedMasterId(String relatedMasterId) {
        this.relatedMasterId = relatedMasterId;
    }

    @Basic
    @Column(name = "related_file_id")
    public String getRelatedFileId() {
        return relatedFileId;
    }

    public void setRelatedFileId(String relatedFileId) {
        this.relatedFileId = relatedFileId;
    }
}
