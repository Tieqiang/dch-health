package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/5/24.
 */
public class TopicKeyWordVo {
    private String topic;
    private List<String> keywords;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
