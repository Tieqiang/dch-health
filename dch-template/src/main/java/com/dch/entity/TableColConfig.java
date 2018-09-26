package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "table_col_config", schema = "dch", catalog = "")
public class TableColConfig extends BaseEntity{
    private String colName;
    private String colCode;
    private String colDescription;
    private String tableId;

    private Integer dataVersion;

    @Basic
    @Column(name = "col_name")
    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    @Basic
    @Column(name = "col_code")
    public String getColCode() {
        return colCode;
    }

    public void setColCode(String colCode) {
        this.colCode = colCode;
    }

    @Basic
    @Column(name = "col_description")
    public String getColDescription() {
        return colDescription;
    }

    public void setColDescription(String colDescription) {
        this.colDescription = colDescription;
    }

    @Basic
    @Column(name = "table_id")
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Basic
    @Column(name = "data_version")
    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableColConfig that = (TableColConfig) o;


        if (dataVersion != null ? !dataVersion.equals(that.dataVersion) : that.dataVersion != null) return false;

        return true;
    }

}
