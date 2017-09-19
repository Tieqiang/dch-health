package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/19.
 */
@Entity
@Table(name = "mr_file", schema = "dch", catalog = "")
public class MrFile extends BaseEntity {

    private String fileTitle;
    private String subjectCode;
    private String fileContent;
    private String attachmentFileId;
    private String keyWords;



    @Basic
    @Column(name = "file_title", nullable = true, length = 200)
    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    @Basic
    @Column(name = "subject_code", nullable = true, length = 64)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Basic
    @Column(name = "file_content", nullable = true, length = -1)
    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
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

}
