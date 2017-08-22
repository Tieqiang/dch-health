package com.dch.facade.common.VO;

/**
 * Created by Administrator on 2017/7/20.
 */
public class ReturnInfo {

    private String success ;

    private String info ;


    public ReturnInfo(String success, String info) {
        this.success = success;
        this.info = info;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
