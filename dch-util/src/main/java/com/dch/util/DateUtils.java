package com.dch.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/13.
 */
public class DateUtils {
    public static final String TIME_FORMAT_A = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_B = "yyyyMMddHHmmss";
    public static final String TIME_FORMAT_C = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String TIME_FORMAT_D = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_XG = "yyyy/MM/dd";
    public static final String TELCORDIA_DATE_FORMAT = "MM-dd-yyyy";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String TIME_FORMAT_SYSTEM = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DATE_FORMAT_B   = "yyMMdd";

    public static Timestamp convertStringToDate(String dateStr){
        if (dateStr == null || "".equals(dateStr.trim())) {
            return null;
        }
        java.util.Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                DateUtils.TIME_FORMAT_A);
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException ex) {
            simpleDateFormat = new SimpleDateFormat(
                    DateUtils.DATE_FORMAT_XG);
            try {
                date = simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                simpleDateFormat = new SimpleDateFormat(
                        DateUtils.DATE_FORMAT);
                try{
                    date = simpleDateFormat.parse(dateStr);
                }catch (Exception ec){
                    try {
                        date = new Date(Long.valueOf(dateStr));
                    } catch (Exception ecn) {
                        try {
                            date = simpleDateFormat.parse("1999-01-01");
                        } catch (ParseException e1) {
                            System.out.println("无法翻译" + dateStr + "取初始化时间！");
                        }
                    }
                }
            }
        }
        return new Timestamp(date.getTime());
    }
}
