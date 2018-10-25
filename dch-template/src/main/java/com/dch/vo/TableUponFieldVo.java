package com.dch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunkqa on 2018/10/24.
 */
public class TableUponFieldVo implements Serializable{
    private String id;
    private String templateId;
    private String tableId;
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FieldUponValue> getFieldUponValueList() {
        return fieldUponValueList;
    }

    public void setFieldUponValueList(List<FieldUponValue> fieldUponValueList) {
        this.fieldUponValueList = fieldUponValueList;
    }
}
