package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_untoward_effect", schema = "dch", catalog = "")
public class DrugUntowardEffect extends BaseEntity{
    private String sex;
    private Integer age;
    private String drugId;
    private String userDrugReason;
    private String administration;
    private String quantity;
    private Timestamp untowardEffectStartTime;
    private String familyDrugUntowardEffect;
    private String disease;
    private String untowardEffectResult;
    private String solution;
    private String treadResult;
    private String unionDrug;


    @Basic
    @Column(name = "sex", nullable = true, length = 4)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "drug_id", nullable = true, length = 64)
    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "user_drug_reason", nullable = true, length = -1)
    public String getUserDrugReason() {
        return userDrugReason;
    }

    public void setUserDrugReason(String userDrugReason) {
        this.userDrugReason = userDrugReason;
    }

    @Basic
    @Column(name = "administration", nullable = true, length = -1)
    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    @Basic
    @Column(name = "quantity", nullable = true, length = -1)
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "untoward_effect_start_time", nullable = true)
    public Timestamp getUntowardEffectStartTime() {
        return untowardEffectStartTime;
    }

    public void setUntowardEffectStartTime(Timestamp untowardEffectStartTime) {
        this.untowardEffectStartTime = untowardEffectStartTime;
    }

    @Basic
    @Column(name = "family_drug_untoward_effect", nullable = true, length = -1)
    public String getFamilyDrugUntowardEffect() {
        return familyDrugUntowardEffect;
    }

    public void setFamilyDrugUntowardEffect(String familyDrugUntowardEffect) {
        this.familyDrugUntowardEffect = familyDrugUntowardEffect;
    }

    @Basic
    @Column(name = "disease", nullable = true, length = -1)
    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    @Basic
    @Column(name = "untoward_effect_result", nullable = true, length = -1)
    public String getUntowardEffectResult() {
        return untowardEffectResult;
    }

    public void setUntowardEffectResult(String untowardEffectResult) {
        this.untowardEffectResult = untowardEffectResult;
    }

    @Basic
    @Column(name = "solution", nullable = true, length = -1)
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Basic
    @Column(name = "tread_result", nullable = true, length = -1)
    public String getTreadResult() {
        return treadResult;
    }

    public void setTreadResult(String treadResult) {
        this.treadResult = treadResult;
    }

    @Basic
    @Column(name = "union_drug", nullable = true, length = -1)
    public String getUnionDrug() {
        return unionDrug;
    }

    public void setUnionDrug(String unionDrug) {
        this.unionDrug = unionDrug;
    }

}
