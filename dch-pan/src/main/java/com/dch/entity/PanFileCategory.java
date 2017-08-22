package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Administrator on 2017/8/14.
 */
@Entity
@Table(name = "pan_file_category", schema = "dch", catalog = "")
public class PanFileCategory extends BaseEntity{
    private String categoryName;
    private String parentCategoryId;

    @Basic
    @Column(name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Basic
    @Column(name = "parent_category_id")
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

}
