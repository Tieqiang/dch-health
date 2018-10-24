package com.dch.vo;

/**
 * Created by sunkqa on 2018/10/16.
 */
public class FieldChange {
    //字段名(英文)
    private String title;
    //要更改的字段（中文）
    private String changeTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChangeTitle() {
        return changeTitle;
    }

    public void setChangeTitle(String changeTitle) {
        this.changeTitle = changeTitle;
    }
}
