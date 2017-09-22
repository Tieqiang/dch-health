package com.dch.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/21.
 */
public class DrugCountryVo implements Serializable{
    private int count;
    private String country;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
