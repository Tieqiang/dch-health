package com.dch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunkqa on 2018/10/24.
 */
public class FieldUponValue implements Serializable{
    private String field;
    private String fieldZn;
    private List<UponValue> uponValueList;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldZn() {
        return fieldZn;
    }

    public void setFieldZn(String fieldZn) {
        this.fieldZn = fieldZn;
    }

    public List<UponValue> getUponValueList() {
        return uponValueList;
    }

    public void setUponValueList(List<UponValue> uponValueList) {
        this.uponValueList = uponValueList;
    }
}
