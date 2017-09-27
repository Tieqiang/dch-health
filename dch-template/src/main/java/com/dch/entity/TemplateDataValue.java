package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_data_value", schema = "dch", catalog = "")
public class TemplateDataValue extends BaseEntity {
    private String dataElementId;
    private String dataValueName;
    private String dataValue;

    @Basic
    @Column(name = "data_element_id", nullable = true, length = 64)
    public String getDataElementId() {
        return dataElementId;
    }

    public void setDataElementId(String dataElementId) {
        this.dataElementId = dataElementId;
    }

    @Basic
    @Column(name = "data_value_name", nullable = true, length = 200)
    public String getDataValueName() {
        return dataValueName;
    }

    public void setDataValueName(String dataValueName) {
        this.dataValueName = dataValueName;
    }

    @Basic
    @Column(name = "data_value", nullable = true, length = 200)
    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

}
