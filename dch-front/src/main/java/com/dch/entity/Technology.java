package com.dch.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="technology")
public class Technology {
    private String urlObjectId;
    private String url;
    private String frontImageUrl;
    private String frontImagePath;
    private String sourceNet;
    private String sourceName;
    private String typeName;
    private String title;
    private String content;
    private Timestamp publishTime;
    private Timestamp crawlTime;

    @Id
    @Column(name = "url_object_id")
    public String getUrlObjectId() {
        return urlObjectId;
    }

    public void setUrlObjectId(String urlObjectId) {
        this.urlObjectId = urlObjectId;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "front_image_url")
    public String getFrontImageUrl() {
        return frontImageUrl;
    }

    public void setFrontImageUrl(String frontImageUrl) {
        this.frontImageUrl = frontImageUrl;
    }

    @Basic
    @Column(name = "front_image_path")
    public String getFrontImagePath() {
        return frontImagePath;
    }

    public void setFrontImagePath(String frontImagePath) {
        this.frontImagePath = frontImagePath;
    }

    @Basic
    @Column(name = "source_net")
    public String getSourceNet() {
        return sourceNet;
    }

    public void setSourceNet(String sourceNet) {
        this.sourceNet = sourceNet;
    }

    @Basic
    @Column(name = "source_name")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "publish_time")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    @Basic
    @Column(name = "crawl_time")
    public Timestamp getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Timestamp crawlTime) {
        this.crawlTime = crawlTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Technology that = (Technology) o;

        if (urlObjectId != null ? !urlObjectId.equals(that.urlObjectId) : that.urlObjectId != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (frontImageUrl != null ? !frontImageUrl.equals(that.frontImageUrl) : that.frontImageUrl != null)
            return false;
        if (frontImagePath != null ? !frontImagePath.equals(that.frontImagePath) : that.frontImagePath != null)
            return false;
        if (sourceNet != null ? !sourceNet.equals(that.sourceNet) : that.sourceNet != null) return false;
        if (sourceName != null ? !sourceName.equals(that.sourceName) : that.sourceName != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (publishTime != null ? !publishTime.equals(that.publishTime) : that.publishTime != null) return false;
        if (crawlTime != null ? !crawlTime.equals(that.crawlTime) : that.crawlTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = urlObjectId != null ? urlObjectId.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (frontImageUrl != null ? frontImageUrl.hashCode() : 0);
        result = 31 * result + (frontImagePath != null ? frontImagePath.hashCode() : 0);
        result = 31 * result + (sourceNet != null ? sourceNet.hashCode() : 0);
        result = 31 * result + (sourceName != null ? sourceName.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        result = 31 * result + (crawlTime != null ? crawlTime.hashCode() : 0);
        return result;
    }
}
