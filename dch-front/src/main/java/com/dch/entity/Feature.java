package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
public class Feature extends BaseEntity{
    private String zoneId;
    private Integer sortNo;
    private String type;
    private String dataId;
    private String url;


    @Basic
    @Column(name = "zone_id")
    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    @Basic
    @Column(name = "sort_no")
    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "data_id")
    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (zoneId != null ? !zoneId.equals(feature.zoneId) : feature.zoneId != null) return false;
        if (sortNo != null ? !sortNo.equals(feature.sortNo) : feature.sortNo != null) return false;
        if (type != null ? !type.equals(feature.type) : feature.type != null) return false;
        if (dataId != null ? !dataId.equals(feature.dataId) : feature.dataId != null) return false;
        if (url != null ? !url.equals(feature.url) : feature.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (zoneId != null ? zoneId.hashCode() : 0);
        result = 31 * result + (sortNo != null ? sortNo.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (dataId != null ? dataId.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
