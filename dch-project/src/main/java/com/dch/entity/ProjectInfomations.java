package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/27.
 */
@Entity
@Table(name = "project_infomations", schema = "dch", catalog = "")
public class ProjectInfomations extends BaseEntity{
    private String infoTitle;
    private String infoContent;

    @Basic
    @Column(name = "info_title", nullable = true, length = 500)
    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    @Basic
    @Column(name = "info_content", nullable = true, length = -1)
    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

}
