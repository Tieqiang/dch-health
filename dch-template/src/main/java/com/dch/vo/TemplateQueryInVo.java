package com.dch.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/13.
 */
public class TemplateQueryInVo implements Serializable{
    private String templateId;
    private String ruleName;
    private List<Map> content;

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

    public List<Map> getContent() {
        return content;
    }

    public void setContent(List<Map> content) {
        this.content = content;
    }
}
