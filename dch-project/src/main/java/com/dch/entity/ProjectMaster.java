package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "project_master", schema = "dch", catalog = "")
public class ProjectMaster extends BaseEntity {
    private String projectName;
    private String projectDesc;
    private String projectPerson;

    @Basic
    @Column(name = "project_name", nullable = true, length = 200)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Basic
    @Column(name = "project_desc", nullable = true, length = -1)
    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    @Basic
    @Column(name = "project_person", nullable = true, length = 200)
    public String getProjectPerson() {
        return projectPerson;
    }

    public void setProjectPerson(String projectPerson) {
        this.projectPerson = projectPerson;
    }

}
