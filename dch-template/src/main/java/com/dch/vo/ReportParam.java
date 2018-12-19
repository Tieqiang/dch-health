package com.dch.vo;

import java.io.Serializable;

/**
 * Created by sunkqa on 2018/10/16.
 */
public class ReportParam implements Serializable{
    private String name;
    private String icon;
    private String active;
    private String chart;
    private ReportQueryParam config;
    private ReportData reportData;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public ReportQueryParam getConfig() {
        return config;
    }

    public void setConfig(ReportQueryParam config) {
        this.config = config;
    }

    public ReportData getReportData() {
        return reportData;
    }

    public void setReportData(ReportData reportData) {
        this.reportData = reportData;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
