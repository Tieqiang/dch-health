package com.dch.vo;

import com.dch.entity.DrugNaturalActive;
import com.dch.entity.DrugNaturalEvaluation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
public class DrugNatureVo implements Serializable {

    private DrugNaturalActive drugNaturalActive ;
    private List<DrugNaturalEvaluation> drugNaturalEvaluations = new ArrayList<>();

    public DrugNaturalActive getDrugNaturalActive() {
        return drugNaturalActive;
    }

    public void setDrugNaturalActive(DrugNaturalActive drugNaturalActive) {
        this.drugNaturalActive = drugNaturalActive;
    }

    public List<DrugNaturalEvaluation> getDrugNaturalEvaluations() {
        return drugNaturalEvaluations;
    }

    public void setDrugNaturalEvaluations(List<DrugNaturalEvaluation> drugNaturalEvaluations) {
        this.drugNaturalEvaluations = drugNaturalEvaluations;
    }
}
