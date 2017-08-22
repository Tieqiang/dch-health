package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_set", schema = "dch", catalog = "")
public class DataSet extends BaseEntity {
    private String dataSetName;
    private String dataSetDescription;
    private String manageOrg;


    @Basic
    @Column(name = "data_set_name")
    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    @Basic
    @Column(name = "data_set_description")
    public String getDataSetDescription() {
        return dataSetDescription;
    }

    public void setDataSetDescription(String dataSetDescription) {
        this.dataSetDescription = dataSetDescription;
    }

    @Basic
    @Column(name = "manage_org")
    public String getManageOrg() {
        return manageOrg;
    }

    public void setManageOrg(String manageOrg) {
        this.manageOrg = manageOrg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSet dataSet = (DataSet) o;

        if (dataSetName != null ? !dataSetName.equals(dataSet.dataSetName) : dataSet.dataSetName != null) return false;
        if (dataSetDescription != null ? !dataSetDescription.equals(dataSet.dataSetDescription) : dataSet.dataSetDescription != null)
            return false;
        if (manageOrg != null ? !manageOrg.equals(dataSet.manageOrg) : dataSet.manageOrg != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (dataSetName != null ? dataSetName.hashCode() : 0);
        result = 31 * result + (dataSetDescription != null ? dataSetDescription.hashCode() : 0);
        result = 31 * result + (manageOrg != null ? manageOrg.hashCode() : 0);

        return result;
    }
}
