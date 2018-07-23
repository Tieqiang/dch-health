package com.dch.vo;

import java.io.Serializable;

/**
 * Created by sunkqa on 2018/7/5.
 */
public class TemplateResultVo implements Serializable{
    private String id;
    private String masterId;
    private String templateResult;

    public TemplateResultVo() {
    }

    public TemplateResultVo(String id, String masterId, String templateResult) {
        this.id = id;
        this.masterId = masterId;
        this.templateResult = templateResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
    }
}
