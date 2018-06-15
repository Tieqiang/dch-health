package com.dch.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunkqa on 2018/6/14.
 */
@Entity
@Table(name = "future_target", schema = "dch", catalog = "")
public class FutureTarget {
    private String id;
    private String relatedDistrictId;
    private String relatedHealthTarget;
    private String currentValue;
    private Timestamp date;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "related_district_id")
    public String getRelatedDistrictId() {
        return relatedDistrictId;
    }

    public void setRelatedDistrictId(String relatedDistrictId) {
        this.relatedDistrictId = relatedDistrictId;
    }

    @Basic
    @Column(name = "related_health_target")
    public String getRelatedHealthTarget() {
        return relatedHealthTarget;
    }

    public void setRelatedHealthTarget(String relatedHealthTarget) {
        this.relatedHealthTarget = relatedHealthTarget;
    }

    @Basic
    @Column(name = "current_value")
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    @Basic
    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
