package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_version", schema = "dch", catalog = "")
public class DataVersion extends BaseEntity {
    private String versionName;
    private String dataElementStandardId;



    @Basic
    @Column(name = "version_name")
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Basic
    @Column(name = "data_element_standard_id")
    public String getDataElementStandardId() {
        return dataElementStandardId;
    }

    public void setDataElementStandardId(String dataElementStandardId) {
        this.dataElementStandardId = dataElementStandardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataVersion that = (DataVersion) o;

        if (versionName != null ? !versionName.equals(that.versionName) : that.versionName != null) return false;
        if (dataElementStandardId != null ? !dataElementStandardId.equals(that.dataElementStandardId) : that.dataElementStandardId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + (dataElementStandardId != null ? dataElementStandardId.hashCode() : 0);
        return result;
    }
}
