package com.dch.vo;

import com.dch.entity.TemplateMaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataElementVO implements Serializable {

    private TemplateMaster templateMaster ;
    private List<PageVO> pageVOS = new ArrayList<>();

    public TemplateMaster getTemplateMaster() {
        return templateMaster;
    }

    public void setTemplateMaster(TemplateMaster templateMaster) {
        this.templateMaster = templateMaster;
    }

    public List<PageVO> getPageVOS() {
        return pageVOS;
    }

    public void setPageVOS(List<PageVO> pageVOS) {
        this.pageVOS = pageVOS;
    }
}
