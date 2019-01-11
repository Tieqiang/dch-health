package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 天然药物属性
 */
@Entity
@Table(name = "drug_natural_active", schema = "dch", catalog = "")
public class DrugNaturalActive extends BaseEntity{
    private String sampleNo;
    private String drugNaturalNameCn;
    private String drugNaturalNameLatin;
    private String bioFamily;
    private String bioGenus;
    private String livePlace;
    private String attrAndFocus;
    private String functions;
    private String useFor;
    private String contraindication;
    private String usePosition;
    private Date collectTime;
    private String collectPlace;
    private String collector;
    private String extractMethod;


    @Basic
    @Column(name = "sample_no", nullable = true, length = 20)
    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    @Basic
    @Column(name = "drug_natural_name_cn", nullable = true, length = 200)
    public String getDrugNaturalNameCn() {
        return drugNaturalNameCn;
    }

    public void setDrugNaturalNameCn(String drugNaturalNameCn) {
        this.drugNaturalNameCn = drugNaturalNameCn;
    }

    @Basic
    @Column(name = "drug_natural_name_latin", nullable = true, length = 200)
    public String getDrugNaturalNameLatin() {
        return drugNaturalNameLatin;
    }

    public void setDrugNaturalNameLatin(String drugNaturalNameLatin) {
        this.drugNaturalNameLatin = drugNaturalNameLatin;
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
    @Column(name = "attr_and_focus", nullable = true, length = -1)
    public String getAttrAndFocus() {
        return attrAndFocus;
    }

    public void setAttrAndFocus(String attrAndFocus) {
        this.attrAndFocus = attrAndFocus;
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
    @Column(name = "use_for", nullable = true, length = -1)
    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    @Basic
    @Column(name = "contraindication", nullable = true, length = -1)
    public String getContraindication() {
        return contraindication;
    }

    public void setContraindication(String contraindication) {
        this.contraindication = contraindication;
    }

    @Basic
    @Column(name = "use_position", nullable = true, length = 200)
    public String getUsePosition() {
        return usePosition;
    }

    public void setUsePosition(String usePosition) {
        this.usePosition = usePosition;
    }

    @Basic
    @Column(name = "collect_time", nullable = true)
    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    @Basic
    @Column(name = "collect_place", nullable = true, length = -1)
    public String getCollectPlace() {
        return collectPlace;
    }

    public void setCollectPlace(String collectPlace) {
        this.collectPlace = collectPlace;
    }

    @Basic
    @Column(name = "collector", nullable = true, length = 20)
    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    @Basic
    @Column(name = "extract_method", nullable = true, length = 20)
    public String getExtractMethod() {
        return extractMethod;
    }

    public void setExtractMethod(String extractMethod) {
        this.extractMethod = extractMethod;
    }

}
