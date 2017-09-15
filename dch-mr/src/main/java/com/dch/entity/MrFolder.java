package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/15.
 */
@Entity
@Table(name = "mr_folder", schema = "dch", catalog = "")
public class MrFolder extends BaseEntity {
    private String folderName;
    private String writer;
    private String version;
    private String attachmentFileId;
    private String keyWords;
    private String shareStatus;
    private String subjectId;




    @Basic
    @Column(name = "folder_name", nullable = true, length = 200)
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Basic
    @Column(name = "writer", nullable = true, length = 200)
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Basic
    @Column(name = "version", nullable = true, length = 200)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Basic
    @Column(name = "attachment_file_id", nullable = true, length = 64)
    public String getAttachmentFileId() {
        return attachmentFileId;
    }

    public void setAttachmentFileId(String attachmentFileId) {
        this.attachmentFileId = attachmentFileId;
    }

    @Basic
    @Column(name = "key_words", nullable = true, length = 500)
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Basic
    @Column(name = "share_status", nullable = true, length = 2)
    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    @Basic
    @Column(name = "subject_id", nullable = true, length = 64)
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

}
