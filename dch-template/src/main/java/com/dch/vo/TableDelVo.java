package com.dch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunkqa on 2018/12/14.
 */
public class TableDelVo implements Serializable{
    private String id;
    private String templateId;
    private String tableId;
    private List<FieldDelCondition> fieldUponValueList;

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

    public List<FieldDelCondition> getFieldUponValueList() {
        return fieldUponValueList;
    }

    public void setFieldUponValueList(List<FieldDelCondition> fieldUponValueList) {
        this.fieldUponValueList = fieldUponValueList;
    }
}
