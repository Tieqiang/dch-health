package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/15.
 */
@Entity
@Table(name = "mr_file_content", schema = "dch", catalog = "")
public class MrFileContent extends BaseEntity {

    private String fileContent;
    private String indexId;
    private String attachmentFileId;
    private String keyWords;



    @Basic
    @Column(name = "file_content", nullable = true, length = -1)
    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Basic
    @Column(name = "index_id", nullable = true, length = 64)
    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
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
