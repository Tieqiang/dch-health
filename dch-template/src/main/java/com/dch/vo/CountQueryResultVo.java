package com.dch.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/9.
 */
public class CountQueryResultVo {
    private String title;
    private String chart;//饼状图，柱状图....
    private String config;
    private List<MongoResultVo> mongoResultVoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<MongoResultVo> getMongoResultVoList() {
        return mongoResultVoList;
    }

    public void setMongoResultVoList(List<MongoResultVo> mongoResultVoList) {
        this.mongoResultVoList = mongoResultVoList;
    }
}
