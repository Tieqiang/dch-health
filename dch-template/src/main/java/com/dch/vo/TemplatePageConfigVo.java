package com.dch.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TemplatePageConfigVo implements Serializable{
    private String document ;
    private String config ;
    private String pageId ;
    private String templateId ;
    private String templatePageDataModel;
    private List<ElementVO> elements = new ArrayList<>();


    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplatePageDataModel() {
        return templatePageDataModel;
    }

    public void setTemplatePageDataModel(String templatePageDataModel) {
        this.templatePageDataModel = templatePageDataModel;
    }

    public List<ElementVO> getElements() {
        return elements;
    }

    public void setElements(List<ElementVO> elements) {
        this.elements = elements;
    }
}
