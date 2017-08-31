package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.codehaus.jackson.map.Serializers;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_disease_treatment_guide", schema = "dch", catalog = "")
public class DrugDiseaseTreatmentGuide extends BaseEntity {
    private String fileId;
    private String guideName;
    private String publisher;
    private Date pubDate;
    private String specialty;
    private String origin;


    @Basic
    @Column(name = "file_id", nullable = true, length = 64)
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "guide_name", nullable = true, length = 200)
    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    @Basic
    @Column(name = "publisher", nullable = true, length = 200)
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "pub_date", nullable = true)
    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Basic
    @Column(name = "specialty", nullable = true, length = 200)
    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Basic
    @Column(name = "origin", nullable = true, length = -1)
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

}
