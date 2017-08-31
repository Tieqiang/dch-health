package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
@Table(name = "drug_new_research_policy", schema = "dch", catalog = "")
public class DrugNewResearchPolicy extends BaseEntity{
    private String policyName ;
    private String policyTypeFlag;
    private String releaseOrgCn;
    private String releaseOrgEn;
    private String pubDate;
    private String writerCn;
    private String writerEn;
    private String pubOrgCn;
    private String pubOrgEn;
    private String keyWordsCn;
    private String keyWordsEn;
    private String summaryCn;
    private String summaryEn;
    private String contactsWay;
    private String expirationDate;
    private String country;
    private String other;
    private String attachmentUrl;


    @Column(name = "policy_name")
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    @Basic
    @Column(name = "policy_type_flag", nullable = true, length = 20)
    public String getPolicyTypeFlag() {
        return policyTypeFlag;
    }

    public void setPolicyTypeFlag(String policyTypeFlag) {
        this.policyTypeFlag = policyTypeFlag;
    }

    @Basic
    @Column(name = "release_org_cn", nullable = true, length = 200)
    public String getReleaseOrgCn() {
        return releaseOrgCn;
    }

    public void setReleaseOrgCn(String releaseOrgCn) {
        this.releaseOrgCn = releaseOrgCn;
    }

    @Basic
    @Column(name = "release_org_en", nullable = true, length = 200)
    public String getReleaseOrgEn() {
        return releaseOrgEn;
    }

    public void setReleaseOrgEn(String releaseOrgEn) {
        this.releaseOrgEn = releaseOrgEn;
    }

    @Basic
    @Column(name = "pub_date", nullable = true)
    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Basic
    @Column(name = "writer_cn", nullable = true, length = 200)
    public String getWriterCn() {
        return writerCn;
    }

    public void setWriterCn(String writerCn) {
        this.writerCn = writerCn;
    }

    @Basic
    @Column(name = "writer_en", nullable = true, length = 200)
    public String getWriterEn() {
        return writerEn;
    }

    public void setWriterEn(String writerEn) {
        this.writerEn = writerEn;
    }

    @Basic
    @Column(name = "pub_org_cn", nullable = true, length = 200)
    public String getPubOrgCn() {
        return pubOrgCn;
    }

    public void setPubOrgCn(String pubOrgCn) {
        this.pubOrgCn = pubOrgCn;
    }

    @Basic
    @Column(name = "pub_org_en", nullable = true, length = 200)
    public String getPubOrgEn() {
        return pubOrgEn;
    }

    public void setPubOrgEn(String pubOrgEn) {
        this.pubOrgEn = pubOrgEn;
    }

    @Basic
    @Column(name = "key_words_cn", nullable = true, length = -1)
    public String getKeyWordsCn() {
        return keyWordsCn;
    }

    public void setKeyWordsCn(String keyWordsCn) {
        this.keyWordsCn = keyWordsCn;
    }

    @Basic
    @Column(name = "key_words_en", nullable = true, length = -1)
    public String getKeyWordsEn() {
        return keyWordsEn;
    }

    public void setKeyWordsEn(String keyWordsEn) {
        this.keyWordsEn = keyWordsEn;
    }

    @Basic
    @Column(name = "summary_cn", nullable = true, length = -1)
    public String getSummaryCn() {
        return summaryCn;
    }

    public void setSummaryCn(String summaryCn) {
        this.summaryCn = summaryCn;
    }

    @Basic
    @Column(name = "summary_en", nullable = true, length = -1)
    public String getSummaryEn() {
        return summaryEn;
    }

    public void setSummaryEn(String summaryEn) {
        this.summaryEn = summaryEn;
    }

    @Basic
    @Column(name = "contacts_way", nullable = true, length = 200)
    public String getContactsWay() {
        return contactsWay;
    }

    public void setContactsWay(String contactsWay) {
        this.contactsWay = contactsWay;
    }

    @Basic
    @Column(name = "expiration_date", nullable = true)
    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Basic
    @Column(name = "country", nullable = true, length = 200)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "other", nullable = true, length = -1)
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Basic
    @Column(name = "attachment_url", nullable = true, length = 500)
    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

}
