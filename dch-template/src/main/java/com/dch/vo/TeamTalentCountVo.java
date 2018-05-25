package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/5/23.
 */
public class TeamTalentCountVo {
    private String topic;
    private List<MongoResultVo> talentList;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<MongoResultVo> getTalentList() {
        return talentList;
    }

    public void setTalentList(List<MongoResultVo> talentList) {
        this.talentList = talentList;
    }
}
