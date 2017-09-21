package com.dch.vo;

/**
 * Created by Administrator on 2017/9/15.
 */
public class PageParam {
    private String categoryCode;
    private String keyWords;
    private int perPage;
    private int currentPage;
    private String exact;//是否精确查询0:否 1:是

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getExact() {
        return exact;
    }

    public void setExact(String exact) {
        this.exact = exact;
    }
}
