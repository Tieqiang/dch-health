package com.dch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunkqa on 2018/10/24.
 */
public class TableUponFieldVo implements Serializable{
    private String id;
    private String templateId;
    private String table;
    private String status;
    private List<FieldUponValue> fieldUponValueList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTable() {
        return table;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<FieldUponValue> getFieldUponValueList() {
        return fieldUponValueList;
    }

    public void setFieldUponValueList(List<FieldUponValue> fieldUponValueList) {
        this.fieldUponValueList = fieldUponValueList;
    }
}
