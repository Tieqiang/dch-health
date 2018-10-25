package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunkqa on 2018/10/25.
 */
@Entity
@Table(name = "table_upon", schema = "dch", catalog = "")
public class TableUpon extends BaseEntity{
    private String templateId;
    private String tableName;
    private Integer dataVersion;

    @Basic
    @Column(name = "templateId")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "table_name")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "data_version")
    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }
}
