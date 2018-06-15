package com.dch.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by sunkqa on 2018/6/14.
 */
@Entity
public class District {
    private String id;
    private String districtName;
    private String relatedDistrictId;
    private String disLevel;

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
    @Column(name = "district_name")
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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
    @Column(name = "dis_level")
    public String getDisLevel() {
        return disLevel;
    }

    public void setDisLevel(String disLevel) {
        this.disLevel = disLevel;
    }

}
