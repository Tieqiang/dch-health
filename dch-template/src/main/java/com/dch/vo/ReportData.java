package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/10/16.
 */
public class ReportData {
    private List result;
    private Integer perPage;
    private Integer currentPage;
    private Long totalCount;

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public Integer getPerPage() {
        return perPage==null?20:perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getCurrentPage() {
        return currentPage==null?1:currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
