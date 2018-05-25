package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/23.
 */
public class CoursePersonJob {
    private String topic;
    private String userName;
    private String homeDeclarePs;//国内申报时任职
    private String abroadDeclarePs;//国外申报时任职
    private String homeCurrentPs;//国内目前任职
    private String abroadCurrentPs;//国外目前任职

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHomeDeclarePs() {
        return homeDeclarePs;
    }

    public void setHomeDeclarePs(String homeDeclarePs) {
        this.homeDeclarePs = homeDeclarePs;
    }

    public String getAbroadDeclarePs() {
        return abroadDeclarePs;
    }

    public void setAbroadDeclarePs(String abroadDeclarePs) {
        this.abroadDeclarePs = abroadDeclarePs;
    }

    public String getHomeCurrentPs() {
        return homeCurrentPs;
    }

    public void setHomeCurrentPs(String homeCurrentPs) {
        this.homeCurrentPs = homeCurrentPs;
    }

    public String getAbroadCurrentPs() {
        return abroadCurrentPs;
    }

    public void setAbroadCurrentPs(String abroadCurrentPs) {
        this.abroadCurrentPs = abroadCurrentPs;
    }
}
