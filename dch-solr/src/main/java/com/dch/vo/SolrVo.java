package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/24.
 */
public class SolrVo implements Serializable{
    private String id;
    private String categoryId;
    private String title;
    private String desc;
    private String label;
    private String category;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "SolrVo{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
