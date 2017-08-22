package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_plant_picture", schema = "dch", catalog = "")
public class DrugPlantPicture extends BaseEntity {
    private String drugPlantId;
    private String picTitle;
    private String picUrl;
    private Date picDate;
    private String picAddress;
    private String picPerson;



    @Basic
    @Column(name = "drug_plant_id", nullable = true, length = 64)
    public String getDrugPlantId() {
        return drugPlantId;
    }

    public void setDrugPlantId(String drugPlantId) {
        this.drugPlantId = drugPlantId;
    }

    @Basic
    @Column(name = "pic_title", nullable = true, length = 200)
    public String getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(String picTitle) {
        this.picTitle = picTitle;
    }

    @Basic
    @Column(name = "pic_url", nullable = true, length = 200)
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Basic
    @Column(name = "pic_date", nullable = true)
    public Date getPicDate() {
        return picDate;
    }

    public void setPicDate(Date picDate) {
        this.picDate = picDate;
    }

    @Basic
    @Column(name = "pic_address", nullable = true, length = 500)
    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    @Basic
    @Column(name = "pic_person", nullable = true, length = 20)
    public String getPicPerson() {
        return picPerson;
    }

    public void setPicPerson(String picPerson) {
        this.picPerson = picPerson;
    }

}
