package com.dch.util;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/7/13.
 */
public class DateUtils {
    public static Timestamp convertStringToDate(String date){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }
}
