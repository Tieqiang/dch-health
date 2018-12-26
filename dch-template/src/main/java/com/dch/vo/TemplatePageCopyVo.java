package com.dch.vo;

import com.dch.entity.TemplatePage;

import java.util.ArrayList;
import java.util.List;

public class TemplatePageCopyVo {

    private TemplatePage templatePage ;
    private List<TemplatePageCopyVo> children=new ArrayList<>();

    public TemplatePage getTemplatePage() {
        return templatePage;
    }

    public void setTemplatePage(TemplatePage templatePage) {
        this.templatePage = templatePage;
    }

    public List<TemplatePageCopyVo> getChildren() {
        return children;
    }

    public void setChildren(List<TemplatePageCopyVo> children) {
        this.children = children;
    }
}
