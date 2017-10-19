package com.dch.vo;

import java.sql.Timestamp;
import java.util.Date;

public class projectMemberVo {
    private String id;
    private String personId;
    private String userName;
    private String loginName;
    private String projectId;
    private String personStatus;
    private Date createDate;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;

    public projectMemberVo(String id, String personId, String userName, String loginName, String projectId, String personStatus, Date createDate, Date modifyDate, String createBy, String modifyBy, String status) {
        this.id = id;
        this.personId = personId;
        this.userName = userName;
        this.loginName = loginName;
        this.projectId = projectId;
        this.personStatus = personStatus;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
        this.status = status;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }
}
