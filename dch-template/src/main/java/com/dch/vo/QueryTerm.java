package com.dch.vo;

/**
 * Created by Administrator on 2018/1/24.
 */
public class QueryTerm {
    private String paramName;
    private String operator;
    private String value;
    private String logicOpt;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogicOpt() {
        return logicOpt;
    }

    public void setLogicOpt(String logicOpt) {
        this.logicOpt = logicOpt;
    }
}
