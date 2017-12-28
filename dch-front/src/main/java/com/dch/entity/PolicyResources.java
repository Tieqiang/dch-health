package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by sunkqa on 2017/12/28.
 */
@Entity
@Table(name = "policy_resources", schema = "dch", catalog = "")
public class PolicyResources extends BaseEntity {
    private String title;
    private String typeName;
    private String content;

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
