package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/15.
 */
@Entity
@Table(name = "mr_subject", schema = "dch", catalog = "")
public class MrSubject extends BaseEntity {
    private String subjectName;
    private String parentSubjectId;

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
