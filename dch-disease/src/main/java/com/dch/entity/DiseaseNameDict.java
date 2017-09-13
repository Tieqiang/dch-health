package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/13.
 */
@Entity
@Table(name = "disease_name_dict", schema = "dch", catalog = "")
public class DiseaseNameDict extends BaseEntity {
    private String refId;
    private String name;
    private String categoryFlag;


    @Basic
    @Column(name = "ref_id", nullable = true, length = 64)
    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 600)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "category_flag", nullable = true, length = 2)
    public String getCategoryFlag() {
        return categoryFlag;
    }

    public void setCategoryFlag(String categoryFlag) {
        this.categoryFlag = categoryFlag;
    }

}
