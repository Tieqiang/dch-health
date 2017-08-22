package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "document_template_group", schema = "dch", catalog = "")
public class DocumentTemplateGroup extends BaseEntity {
    private String groupName;
    private String owner;


    @Basic
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
