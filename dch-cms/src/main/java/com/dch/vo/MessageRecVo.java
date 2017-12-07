package com.dch.vo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class MessageRecVo {
    private String id;
    private String title;
    private String content;
    private String flag;
    private List<RecUserInfo> recUserInfos;
    private Timestamp createDate;

    public MessageRecVo(String id, String title, String content,String flag, List<RecUserInfo> recUserInfos, Date createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.flag = flag;
        this.recUserInfos = recUserInfos;
        this.createDate = new Timestamp(createDate.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<RecUserInfo> getRecUserInfos() {
        return recUserInfos;
    }

    public void setRecUserInfos(List<RecUserInfo> recUserInfos) {
        this.recUserInfos = recUserInfos;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
