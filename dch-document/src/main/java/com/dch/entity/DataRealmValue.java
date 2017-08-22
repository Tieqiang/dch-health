package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_realm_value", schema = "dch", catalog = "")
public class DataRealmValue extends BaseEntity {
    private String dataRealmId;
    private String dataValue;
    private String dataValueMeans;
    private String dataValueInstruction;



    @Basic
    @Column(name = "data_realm_id")
    public String getDataRealmId() {
        return dataRealmId;
    }

    public void setDataRealmId(String dataValueRealmMasterId) {
        this.dataRealmId = dataValueRealmMasterId;
    }

    @Basic
    @Column(name = "data_value")
    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    @Basic
    @Column(name = "data_value_means")
    public String getDataValueMeans() {
        return dataValueMeans;
    }

    public void setDataValueMeans(String dataValueMeans) {
        this.dataValueMeans = dataValueMeans;
    }

    @Basic
    @Column(name = "data_value_instruction")
    public String getDataValueInstruction() {
        return dataValueInstruction;
    }

    public void setDataValueInstruction(String dataValueInstruction) {
        this.dataValueInstruction = dataValueInstruction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRealmValue that = (DataRealmValue) o;

        if (dataRealmId != null ? !dataRealmId.equals(that.dataRealmId) : that.dataRealmId != null)
            return false;
        if (dataValue != null ? !dataValue.equals(that.dataValue) : that.dataValue != null) return false;
        if (dataValueMeans != null ? !dataValueMeans.equals(that.dataValueMeans) : that.dataValueMeans != null)
            return false;
        if (dataValueInstruction != null ? !dataValueInstruction.equals(that.dataValueInstruction) : that.dataValueInstruction != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (dataRealmId != null ? dataRealmId.hashCode() : 0);
        result = 31 * result + (dataValue != null ? dataValue.hashCode() : 0);
        result = 31 * result + (dataValueMeans != null ? dataValueMeans.hashCode() : 0);
        result = 31 * result + (dataValueInstruction != null ? dataValueInstruction.hashCode() : 0);
        return result;
    }
}
