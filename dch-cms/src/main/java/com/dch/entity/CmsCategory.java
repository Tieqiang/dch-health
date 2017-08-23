package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/23.
 */
@Entity
@Table(name = "cms_category", schema = "dch", catalog = "")
public class CmsCategory extends BaseEntity {
    private String parentCategoryId;
    private String categoryLevel;
    private String categoryName;
    private String icon;
    private String router;

    @Basic
    @Column(name = "parent_category_id", nullable = true, length = 64)
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @Basic
    @Column(name = "category_level", nullable = true, length = 200)
    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
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
    @Column(name = "icon", nullable = true, length = 200)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "router", nullable = true, length = 500)
    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

}
