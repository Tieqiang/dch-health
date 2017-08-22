package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Administrator on 2017/8/14.
 */
@Entity
@Table(name = "pan_file_type", schema = "dch", catalog = "")
public class PanFileType extends BaseEntity{
    private String typeName;
    private String icon;
    private String assocationType;



    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "assocation_type")
    public String getAssocationType() {
        return assocationType;
    }

    public void setAssocationType(String assocationType) {
        this.assocationType = assocationType;
    }

}
