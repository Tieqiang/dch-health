package com.dch.vo;

import java.io.Serializable;

/**
 * Created by sunkqa on 2019/1/21.
 */
public class UserFillVo implements Serializable{
    private String done;
    //flag 为1为未提交，2为已提交
    private String flag;
    private String upload;

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }
}
