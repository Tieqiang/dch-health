package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "project_members", schema = "dch", catalog = "")
public class ProjectMember extends BaseEntity {
    private String projectId;
    private String personId;
    private String personName;

    @Basic
    @Column(name = "project_id", nullable = true, length = 64)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Basic
    @Column(name = "person_id", nullable = true, length = 64)
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "person_name", nullable = true, length = 200)
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

}
