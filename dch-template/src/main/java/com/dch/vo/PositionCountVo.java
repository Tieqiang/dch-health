package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/22.
 */
public class PositionCountVo {
    private String position;
    private Integer count;
    private Integer partCount;//兼职人员数量
    //private String ifPart;//是否兼职

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public void setPartCount(Integer partCount) {
        this.partCount = partCount;
    }
}
