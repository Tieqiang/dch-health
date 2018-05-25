package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/24.
 */
public class TopicTreatiseVo {
    private String topic;
    private int treatiseNum;
    private Double keywordNum;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTreatiseNum() {
        return treatiseNum;
    }

    public void setTreatiseNum(int treatiseNum) {
        this.treatiseNum = treatiseNum;
    }

    public Double getKeywordNum() {
        return keywordNum;
    }

    public void setKeywordNum(Double keywordNum) {
        this.keywordNum = keywordNum;
    }
}
