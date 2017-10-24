package com.dch.vo;

import com.dch.entity.TemplatePage;
import com.dch.entity.TemplateResult;

/**
 * Created by Administrator on 2017/10/24.
 */
public class TemplatePageAndResultVo {

    private TemplatePage templatePage;

    private TemplateResult templateResult;

    public TemplatePage getTemplatePage() {
        return templatePage;
    }

    public void setTemplatePage(TemplatePage templatePage) {
        this.templatePage = templatePage;
    }

    public TemplateResult getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(TemplateResult templateResult) {
        this.templateResult = templateResult;
    }
}
