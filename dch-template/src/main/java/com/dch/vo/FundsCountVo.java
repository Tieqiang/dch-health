package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/5/22.
 */
public class FundsCountVo {
    private String topic;
    private List<UnitFunds> fundsList;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<UnitFunds> getFundsList() {
        return fundsList;
    }

    public void setFundsList(List<UnitFunds> fundsList) {
        this.fundsList = fundsList;
    }
}
