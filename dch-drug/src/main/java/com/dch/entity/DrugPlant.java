package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_plant", schema = "dch", catalog = "")
public class DrugPlant extends BaseEntity {
    private String nameCn;
    private String nameLatin;
    private String bioFamily;
    private String bioGenus;
    private String livePlace;
    private String functions;
    private String liveEnv;
    private String plantCharacter;

    @Basic
    @Column(name = "name_cn", nullable = true, length = 200)
    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Basic
    @Column(name = "name_latin", nullable = true, length = 200)
    public String getNameLatin() {
        return nameLatin;
    }

    public void setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    @Basic
    @Column(name = "bio_family", nullable = true, length = 200)
    public String getBioFamily() {
        return bioFamily;
    }

    public void setBioFamily(String bioFamily) {
        this.bioFamily = bioFamily;
    }

    @Basic
    @Column(name = "bio_genus", nullable = true, length = 200)
    public String getBioGenus() {
        return bioGenus;
    }

    public void setBioGenus(String bioGenus) {
        this.bioGenus = bioGenus;
    }

    @Basic
    @Column(name = "live_place", nullable = true, length = -1)
    public String getLivePlace() {
        return livePlace;
    }

    public void setLivePlace(String livePlace) {
        this.livePlace = livePlace;
    }

    @Basic
    @Column(name = "functions", nullable = true, length = -1)
    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    @Basic
    @Column(name = "live_env", nullable = true, length = -1)
    public String getLiveEnv() {
        return liveEnv;
    }

    public void setLiveEnv(String liveEnv) {
        this.liveEnv = liveEnv;
    }

    @Basic
    @Column(name = "plant_character", nullable = true, length = -1)
    public String getPlantCharacter() {
        return plantCharacter;
    }

    public void setPlantCharacter(String plantCharacter) {
        this.plantCharacter = plantCharacter;
    }

}
