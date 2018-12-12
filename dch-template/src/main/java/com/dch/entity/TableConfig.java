package com.dch.entity;


import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
@Entity
@Table(name = "table_config", schema = "dch", catalog = "")
public class TableConfig extends BaseEntity {
    private String tableName;
    private String tableDesc;
    private String formId;
    private String createFrom;
    private String executeSql ;
    private String tableDefineObject ;

    @Basic
    @Column(name = "table_name")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "table_desc")
    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    @Basic
    @Column(name = "form_id")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Basic
    @Column(name = "create_from")
    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }

    @Basic
    @Column(name = "execute_sql")
    public String getExecuteSql() {
        return executeSql;
    }

    public void setExecuteSql(String executeSql) {
        this.executeSql = executeSql;
    }


    public String getTableDefineObject() {
        return tableDefineObject;
    }

    public void setTableDefineObject(String tableDefineObject) {
        this.tableDefineObject = tableDefineObject;
    }
}
