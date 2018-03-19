package com.dch.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/13.
 */
public class TemplateQueryResultVo {
    private String templateId;//表单id
    private String ruleName;//每个自定义统计项名称
    private List<CountQueryResultVo> countQueryResultVoList;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<CountQueryResultVo> getCountQueryResultVoList() {
        return countQueryResultVoList;
    }

    public void setCountQueryResultVoList(List<CountQueryResultVo> countQueryResultVoList) {
        this.countQueryResultVoList = countQueryResultVoList;
    }
}
