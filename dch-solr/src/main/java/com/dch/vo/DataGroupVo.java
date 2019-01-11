package com.dch.vo;

import java.io.Serializable;

/**
 * Created by sunkqa on 2019/1/11.
 */
public class DataGroupVo implements Serializable {
    private String code;
    private long count;

    public DataGroupVo() {
    }

    public DataGroupVo(String code, long count) {
        this.code = code;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
