package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/4.
 */
@Entity
@Table(name = "data_set_vs_element", schema = "dch", catalog = "")
public class DataSetVsElement{
    private String id;
    private String dataElementId;
    private String dataSetId;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "data_element_id")
    public String getDataElementId() {
        return dataElementId;
    }

    public void setDataElementId(String dataElementId) {
        this.dataElementId = dataElementId;
    }

    @Basic
    @Column(name = "data_set_id")
    public String getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSetVsElement that = (DataSetVsElement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (dataElementId != null ? !dataElementId.equals(that.dataElementId) : that.dataElementId != null)
            return false;
        if (dataSetId != null ? !dataSetId.equals(that.dataSetId) : that.dataSetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataElementId != null ? dataElementId.hashCode() : 0);
        result = 31 * result + (dataSetId != null ? dataSetId.hashCode() : 0);
        return result;
    }
}
