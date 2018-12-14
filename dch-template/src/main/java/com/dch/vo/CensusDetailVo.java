package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/12/14.
 */
public class CensusDetailVo {
    private List<String> fieldList;
    private List resultList;

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }
}
