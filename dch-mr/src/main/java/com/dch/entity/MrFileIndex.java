package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/15.
 */
@Entity
@Table(name = "mr_file_index", schema = "dch", catalog = "")
public class MrFileIndex extends BaseEntity {
    private String fileIndexName;
    private String parentIndexId;
    private String folderId;




    @Basic
    @Column(name = "file_index_name", nullable = true, length = 200)
    public String getFileIndexName() {
        return fileIndexName;
    }

    public void setFileIndexName(String fileIndexName) {
        this.fileIndexName = fileIndexName;
    }

    @Basic
    @Column(name = "parent_index_id", nullable = true, length = 64)
    public String getParentIndexId() {
        return parentIndexId;
    }

    public void setParentIndexId(String parentIndexId) {
        this.parentIndexId = parentIndexId;
    }

    @Basic
    @Column(name = "folder_id", nullable = true, length = 64)
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}
