package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/24.
 */
public class SolrVo implements Serializable{
    private String id;
    private String categoryCode;
    private String title;
    private String desc;
    private String label;
    private String category;
    private String firstPy;//首字母拼音码
    private String belong;//所属模块

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getFirstPy() {
        return firstPy;
    }

    public void setFirstPy(String firstPy) {
        this.firstPy = firstPy;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "SolrVo{" +
                "id='" + id + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
