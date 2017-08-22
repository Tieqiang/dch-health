package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Administrator on 2017/8/16.
 */
@Entity
@Table(name = "pan_file", schema = "dch", catalog = "")
public class PanFile extends BaseEntity{

    private String fileName;
    private String fileDesc;
    private String fileAttr;
    private String fileTypeId;
    private String icon;
    private String useFor;
    private String storePath;
    private String fileShare;
    private String fileOwner;
    private String parentId;
    private String categoryId;
    private String keyWords;
    private String folderFlag;


    @Basic
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file_desc")
    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    @Basic
    @Column(name = "file_attr")
    public String getFileAttr() {
        return fileAttr;
    }

    public void setFileAttr(String fileAttr) {
        this.fileAttr = fileAttr;
    }

    @Basic
    @Column(name = "file_type_id")
    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
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
    @Column(name = "use_for")
    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    @Basic
    @Column(name = "store_path")
    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    @Basic
    @Column(name = "file_share")
    public String getFileShare() {
        return fileShare;
    }

    public void setFileShare(String fileShare) {
        this.fileShare = fileShare;
    }


    @Basic
    @Column(name = "file_owner")
    public String getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(String fileOwner) {
        this.fileOwner = fileOwner;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "category_id")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "key_words")
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Basic
    @Column(name = "folder_flag")
    public String getFolderFlag() {
        return folderFlag;
    }

    public void setFolderFlag(String folderFlag) {
        this.folderFlag = folderFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PanFile panFile = (PanFile) o;
        if (fileOwner != null ? !fileOwner.equals(panFile.fileOwner) : panFile.fileOwner != null) return false;
        if (parentId != null ? !parentId.equals(panFile.parentId) : panFile.parentId != null) return false;
        if (categoryId != null ? !categoryId.equals(panFile.categoryId) : panFile.categoryId != null) return false;
        if (keyWords != null ? !keyWords.equals(panFile.keyWords) : panFile.keyWords != null) return false;
        if (folderFlag != null ? !folderFlag.equals(panFile.folderFlag) : panFile.folderFlag != null) return false;

        return true;
    }

}
