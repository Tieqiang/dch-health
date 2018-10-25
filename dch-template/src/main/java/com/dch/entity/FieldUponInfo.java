package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunkqa on 2018/10/25.
 */
@Entity
@Table(name = "field_upon_info", schema = "dch", catalog = "")
public class FieldUponInfo extends BaseEntity{
    private String fieldEn;
    private String fieldZn;
    private String originalValue;
    private String uponValue;
    private String relatedTableUponId;
    private Integer dataVersion;

    @Basic
    @Column(name = "field_en")
    public String getFieldEn() {
        return fieldEn;
    }

    public void setFieldEn(String fieldEn) {
        this.fieldEn = fieldEn;
    }

    @Basic
    @Column(name = "field_zn")
    public String getFieldZn() {
        return fieldZn;
    }

    public void setFieldZn(String fieldZn) {
        this.fieldZn = fieldZn;
    }

    @Basic
    @Column(name = "original_value")
    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    @Basic
    @Column(name = "upon_value")
    public String getUponValue() {
        return uponValue;
    }

    public void setUponValue(String uponValue) {
        this.uponValue = uponValue;
    }

    @Basic
    @Column(name = "related_table_upon_id")
    public String getRelatedTableUponId() {
        return relatedTableUponId;
    }

    public void setRelatedTableUponId(String relatedTableUponId) {
        this.relatedTableUponId = relatedTableUponId;
    }

    @Basic
    @Column(name = "data_version")
    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }
}
