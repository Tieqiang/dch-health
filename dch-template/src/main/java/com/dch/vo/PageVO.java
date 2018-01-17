package com.dch.vo;

import com.dch.entity.TemplatePage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageVO implements Serializable {

    private TemplatePage templatePage ;

    private List<ElementVO> elementVOS = new ArrayList<>();

    public TemplatePage getTemplatePage() {
        return templatePage;
    }

    public void setTemplatePage(TemplatePage templatePage) {
        this.templatePage = templatePage;
    }

    public List<ElementVO> getElementVOS() {
        return elementVOS;
    }

    public void setElementVOS(List<ElementVO> elementVOS) {
        this.elementVOS = elementVOS;
    }
}
