package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_natural_chemical_composition", schema = "dch", catalog = "")
public class DrugNaturalChemicalComposition extends BaseEntity {
    private String no;
    private String nameCn;
    private String nameEn;
    private String structuralFormula;
    private String molecularWeight;
    private String bioactivity;
    private String plantOrigin;
    private String chemicalStruct;



    @Basic
    @Column(name = "no", nullable = true, length = 10)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "name_cn", nullable = true, length = 200)
    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Basic
    @Column(name = "name_en", nullable = true, length = 200)
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Basic
    @Column(name = "structural_formula", nullable = true, length = 500)
    public String getStructuralFormula() {
        return structuralFormula;
    }

    public void setStructuralFormula(String structuralFormula) {
        this.structuralFormula = structuralFormula;
    }

    @Basic
    @Column(name = "molecular_weight", nullable = true, length = 500)
    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    @Basic
    @Column(name = "bioactivity", nullable = true, length = -1)
    public String getBioactivity() {
        return bioactivity;
    }

    public void setBioactivity(String bioactivity) {
        this.bioactivity = bioactivity;
    }

    @Basic
    @Column(name = "plant_origin", nullable = true, length = 500)
    public String getPlantOrigin() {
        return plantOrigin;
    }

    public void setPlantOrigin(String plantOrigin) {
        this.plantOrigin = plantOrigin;
    }

    @Basic
    @Column(name = "chemical_struct", nullable = true, length = 500)
    public String getChemicalStruct() {
        return chemicalStruct;
    }

    public void setChemicalStruct(String chemicalStruct) {
        this.chemicalStruct = chemicalStruct;
    }

}
