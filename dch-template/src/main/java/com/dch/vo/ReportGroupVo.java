package com.dch.vo;

import com.dch.entity.ReportGroup;

import java.util.List;

/**
 * Created by sunkqa on 2018/10/19.
 */
public class ReportGroupVo {
    private String id;
    private String tableId;
    private String reportName;
    private List<ReportGroup> reportGroupList;

    public ReportGroupVo() {
    }

    public ReportGroupVo(String id, String tableId, String reportName) {
        this.id = id;
        this.tableId = tableId;
        this.reportName = reportName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<ReportGroup> getReportGroupList() {
        return reportGroupList;
    }

    public void setReportGroupList(List<ReportGroup> reportGroupList) {
        this.reportGroupList = reportGroupList;
    }
}
