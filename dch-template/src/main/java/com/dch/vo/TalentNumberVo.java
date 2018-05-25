package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/23.
 */
public class TalentNumberVo {
    private String topic;
    private int masterNum;
    private int doctorateNum;
    private int abroadDocNum;
    private int postdoctorNum;
    private int internalMasterNum;
    private int internalDoctorateNum;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMasterNum() {
        return masterNum;
    }

    public void setMasterNum(int masterNum) {
        this.masterNum = masterNum;
    }

    public int getDoctorateNum() {
        return doctorateNum;
    }

    public void setDoctorateNum(int doctorateNum) {
        this.doctorateNum = doctorateNum;
    }

    public int getAbroadDocNum() {
        return abroadDocNum;
    }

    public void setAbroadDocNum(int abroadDocNum) {
        this.abroadDocNum = abroadDocNum;
    }

    public int getPostdoctorNum() {
        return postdoctorNum;
    }

    public void setPostdoctorNum(int postdoctorNum) {
        this.postdoctorNum = postdoctorNum;
    }

    public int getInternalMasterNum() {
        return internalMasterNum;
    }

    public void setInternalMasterNum(int internalMasterNum) {
        this.internalMasterNum = internalMasterNum;
    }

    public int getInternalDoctorateNum() {
        return internalDoctorateNum;
    }

    public void setInternalDoctorateNum(int internalDoctorateNum) {
        this.internalDoctorateNum = internalDoctorateNum;
    }
}
