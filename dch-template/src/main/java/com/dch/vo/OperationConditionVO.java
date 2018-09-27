package com.dch.vo;

import com.dch.entity.TableColConfig;

import java.io.Serializable;
import java.util.List;

public class OperationConditionVO implements Serializable{

    private TableColConfig firstTableColConfig ;//第一个字段

    private OperationEnum operationEnum ;//操作符

    private TableColConfig secondTableColConfig ;//第二个字段

    private Object thanValue ;//比较对象

    private List<String>  inValues;

    private String nextOperation;//AND or OR


    public TableColConfig getFirstTableColConfig() {
        return firstTableColConfig;
    }

    public void setFirstTableColConfig(TableColConfig firstTableColConfig) {
        this.firstTableColConfig = firstTableColConfig;
    }

    public OperationEnum getOperationEnum() {
        return operationEnum;
    }

    public void setOperationEnum(OperationEnum operationEnum) {
        this.operationEnum = operationEnum;
    }

    public TableColConfig getSecondTableColConfig() {
        return secondTableColConfig;
    }

    public void setSecondTableColConfig(TableColConfig secondTableColConfig) {
        this.secondTableColConfig = secondTableColConfig;
    }

    public Object getThanValue() {
        return thanValue;
    }

    public void setThanValue(Object thanValue) {
        this.thanValue = thanValue;
    }

    public List<String> getInValues() {
        return inValues;
    }

    public void setInValues(List<String> inValues) {
        this.inValues = inValues;
    }

    public String getNextOperation() {
        return nextOperation;
    }

    public void setNextOperation(String nextOperation) {
        this.nextOperation = nextOperation;
    }
}
