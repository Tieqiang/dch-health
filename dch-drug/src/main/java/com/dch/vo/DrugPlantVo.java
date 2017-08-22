package com.dch.vo;

import com.dch.entity.DrugPlant;
import com.dch.entity.DrugPlantPicture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
public class DrugPlantVo implements Serializable {

    private DrugPlant drugPlant ;

    private List<DrugPlantPicture> drugPlantPictures = new ArrayList<>() ;

    public DrugPlant getDrugPlant() {
        return drugPlant;
    }

    public void setDrugPlant(DrugPlant drugPlant) {
        this.drugPlant = drugPlant;
    }

    public List<DrugPlantPicture> getDrugPlantPictures() {
        return drugPlantPictures;
    }

    public void setDrugPlantPictures(List<DrugPlantPicture> drugPlantPictures) {
        this.drugPlantPictures = drugPlantPictures;
    }
}
