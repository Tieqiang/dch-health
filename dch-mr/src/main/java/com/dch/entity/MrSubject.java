package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/19.
 */
@Entity
@Table(name = "mr_subject", schema = "dch", catalog = "")
public class MrSubject extends BaseEntity {
    private String subjectName;
    private String parentSubjectId;
    private String subjectCode;

    @Basic
    @Column(name = "subject_code", nullable = true, length = 10)
    public String getSubjectCode() { return subjectCode; }

    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    @Basic
    @Column(name = "subject_name", nullable = true, length = 200)
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Basic
    @Column(name = "parent_subject_id", nullable = true, length = 64)
    public String getParentSubjectId() {
        return parentSubjectId;
    }

    public void setParentSubjectId(String parentSubjectId) {
        this.parentSubjectId = parentSubjectId;
    }



}
