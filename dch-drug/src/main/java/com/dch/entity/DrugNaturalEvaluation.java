package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_natural_evaluation", schema = "dch", catalog = "")
public class DrugNaturalEvaluation extends BaseEntity{
    private String drugNaturalId;
    private String evaluationModelName;
    private String value;
    private String modelRelativeDisease;
    private String evaluatior;
    private String evaluation;


    @Basic
    @Column(name = "drug_natural_id", nullable = true, length = 64)
    public String getDrugNaturalId() {
        return drugNaturalId;
    }

    public void setDrugNaturalId(String drugNaturalId) {
        this.drugNaturalId = drugNaturalId;
    }

    @Basic
    @Column(name = "evaluation_model_name", nullable = true, length = 200)
    public String getEvaluationModelName() {
        return evaluationModelName;
    }

    public void setEvaluationModelName(String evaluationModelName) {
        this.evaluationModelName = evaluationModelName;
    }

    @Basic
    @Column(name = "value", nullable = true, length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "model_relative_disease", nullable = true, length = -1)
    public String getModelRelativeDisease() {
        return modelRelativeDisease;
    }

    public void setModelRelativeDisease(String modelRelativeDisease) {
        this.modelRelativeDisease = modelRelativeDisease;
    }

    @Basic
    @Column(name = "evaluatior", nullable = true, length = 200)
    public String getEvaluatior() {
        return evaluatior;
    }

    public void setEvaluatior(String evaluatior) {
        this.evaluatior = evaluatior;
    }

    @Basic
    @Column(name = "evaluation", nullable = true, length = -1)
    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

}
