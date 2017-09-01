package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/31.
 */
public class CmsContentVo implements Serializable{
    private String id;
    private String content;
    private String contentTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

}
