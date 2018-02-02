package com.dch.vo;

import com.dch.entity.PolicyResourcesDetail;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
public class SolrPageVo {
    private String id;
    private String categoryCode;
    private String title;
    private String desc;
    private String label;
    private String category;
    private List<PolicyResourcesDetail> detailList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PolicyResourcesDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<PolicyResourcesDetail> detailPage) {
        this.detailList = detailPage;
    }
}
