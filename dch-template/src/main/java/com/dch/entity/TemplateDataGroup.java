package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "template_data_group", schema = "dch", catalog = "")
public class TemplateDataGroup extends BaseEntity {
    private String dataElementGroupName;
    private String dataElementGroupCode;
    private String templateId;

    @Basic
    @Column(name = "data_element_group_name", nullable = true, length = 200)
    public String getDataElementGroupName() {
        return dataElementGroupName;
    }

    public void setDataElementGroupName(String dataElementGroupName) {
        this.dataElementGroupName = dataElementGroupName;
    }

    @Basic
    @Column(name = "data_element_group_code", nullable = true, length = 200)
    public String getDataElementGroupCode() {
        return dataElementGroupCode;
    }

    public void setDataElementGroupCode(String dataElementGroupCode) {
        this.dataElementGroupCode = dataElementGroupCode;
    }

    @Basic
    @Column(name = "template_id", nullable = true, length = 64)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
