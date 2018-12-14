package com.dch.vo;

/**
 * Created by sunkqa on 2018/12/14.
 */
public class CencusCondition {
    private String tableName;
    private String field;
    private String fieldValue;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
