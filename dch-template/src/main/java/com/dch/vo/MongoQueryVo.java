package com.dch.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */
public class MongoQueryVo {
    private String templateId;
    private String target;
    private String targetName;
    private List<QueryTerm> queryParamList;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public List<QueryTerm> getQueryParamList() {
        return queryParamList;
    }

    public void setQueryParamList(List<QueryTerm> queryParamList) {
        this.queryParamList = queryParamList;
    }
}
