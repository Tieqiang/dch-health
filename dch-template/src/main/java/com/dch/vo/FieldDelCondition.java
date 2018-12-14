package com.dch.vo;

import java.io.Serializable;

/**
 * Created by sunkqa on 2018/12/14.
 */
public class FieldDelCondition implements Serializable{
    private String field;;
    private OperationEnum operationEnum;
    private Object deleteValue;
    private String nextOperation;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OperationEnum getOperationEnum() {
        return operationEnum;
    }

    public void setOperationEnum(OperationEnum operationEnum) {
        this.operationEnum = operationEnum;
    }

    public Object getDeleteValue() {
        return deleteValue;
    }

    public void setDeleteValue(Object deleteValue) {
        this.deleteValue = deleteValue;
    }

    public String getNextOperation() {
        return nextOperation;
    }

    public void setNextOperation(String nextOperation) {
        this.nextOperation = nextOperation;
    }
}
