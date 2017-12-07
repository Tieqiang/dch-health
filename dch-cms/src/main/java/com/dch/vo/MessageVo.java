package com.dch.vo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class MessageVo {

    //站内信id
    private String id;
    private String sendId;
    private String flag;
    private List<String> recIds;
    private String title;
    private String content;
    private Timestamp sendDate;
    private Timestamp createDate;

    public MessageVo() {
    }

    public MessageVo(String id, String sendId,String flag, String title, String content, Date sendDate, Date createDate) {
        this.id = id;
        this.sendId = sendId;
        this.flag = flag;
        this.title = title;
        this.content = content;
        this.sendDate = new Timestamp(sendDate.getTime());
        this.createDate = new Timestamp(createDate.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<String> getRecIds() {
        return recIds;
    }

    public void setRecIds(List<String> recIds) {
        this.recIds = recIds;
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

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
