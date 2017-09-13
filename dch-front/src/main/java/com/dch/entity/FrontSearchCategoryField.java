package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import com.dch.facade.common.BaseFacade;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/28.
 */
@Entity
@Table(name = "front_search_category_field", schema = "dch", catalog = "")
public class FrontSearchCategoryField extends BaseEntity {
    private String categoryId;
    private String fieldName;
    private String fieldArgs;




    @Basic
    @Column(name = "category_id", nullable = true, length = 64)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "field_name", nullable = true, length = 200)
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Basic
    @Column(name = "field_args", nullable = true, length = 500)
    public String getFieldArgs() {
        return fieldArgs;
    }

    public void setFieldArgs(String fieldArgs) {
        this.fieldArgs = fieldArgs;
    }

}
