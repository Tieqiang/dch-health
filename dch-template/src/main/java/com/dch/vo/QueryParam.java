package com.dch.vo;

/**
 * Created by sunkqa on 2018/3/9.
 */
public class QueryParam {
    //private String templateId;//表单id 统计信息属于那个表单
    private String title;//标题
    private String xaxis;//x轴字段名
    private String yaxis;//y轴字段名
    private String type;//统计类型 1 sum ,2 average 求和和平均
    private String ifDuplicate;//统计是否去重 0：否 1:是
    private String sortType;//0 x轴由小到大 1 x轴由大到小
    private String chart;//图形类型 柱状图 饼状图...

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXaxis() {
        return xaxis;
    }

    public void setXaxis(String xaxis) {
        this.xaxis = xaxis;
    }

    public String getYaxis() {
        return yaxis;
    }

    public void setYaxis(String yaxis) {
        this.yaxis = yaxis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIfDuplicate() {
        return ifDuplicate;
    }

    public void setIfDuplicate(String ifDuplicate) {
        this.ifDuplicate = ifDuplicate;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }
}
