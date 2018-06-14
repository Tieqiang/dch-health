package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/30.
 */
public class TemplateQueryRuleVo {
    private String id;
    private String mark;//备注

    public TemplateQueryRuleVo() {
    }

    public TemplateQueryRuleVo(String id,String mark) {
        this.id = id;
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
