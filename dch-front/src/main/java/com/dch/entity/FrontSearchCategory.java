package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/28.
 */
@Entity
@Table(name = "front_search_category", schema = "dch", catalog = "")
public class FrontSearchCategory extends BaseEntity {
    private String parentId;
    private String categoryName;
    private String categoryCode ;
    private String dataApiPath;
    private String viewPath;



    @Basic
    @Column(name = "parent_id", nullable = true, length = 64)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "category_name", nullable = true, length = 200)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Basic
    @Column(name = "data_api_path", nullable = true, length = 500)
    public String getDataApiPath() {
        return dataApiPath;
    }

    public void setDataApiPath(String dataApiPath) {
        this.dataApiPath = dataApiPath;
    }

    @Basic
    @Column(name = "view_path", nullable = true, length = 200)
    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    @Column(name="category_code")
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
