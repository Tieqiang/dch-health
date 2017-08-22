package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "feature_zone", schema = "dch", catalog = "")
public class FeatureZone extends BaseEntity{
    private String featureZoneName;
    private Integer featureZoneNo;


    @Basic
    @Column(name = "feature_zone_name")
    public String getFeatureZoneName() {
        return featureZoneName;
    }

    public void setFeatureZoneName(String featureZoneName) {
        this.featureZoneName = featureZoneName;
    }

    @Basic
    @Column(name = "feature_zone_no")
    public Integer getFeatureZoneNo() {
        return featureZoneNo;
    }

    public void setFeatureZoneNo(Integer featureZoneNo) {
        this.featureZoneNo = featureZoneNo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureZone that = (FeatureZone) o;

        if (featureZoneName != null ? !featureZoneName.equals(that.featureZoneName) : that.featureZoneName != null)
            return false;
        if (featureZoneNo != null ? !featureZoneNo.equals(that.featureZoneNo) : that.featureZoneNo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (featureZoneName != null ? featureZoneName.hashCode() : 0);
        result = 31 * result + (featureZoneNo != null ? featureZoneNo.hashCode() : 0);
        return result;
    }
}
