package com.dch.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatHelper {
	 private TimeFormatHelper()
	    {
	    }

	    public static String getTimeFormat(String dateStr)
	    {
	        String timeFormat = "yyyy-MM-dd HH:mm:ss";
	        if(dateStr != null)
	        {
	            String str1[] = dateStr.split(":");
	            String str2[] = dateStr.split("-");
	            boolean existDot = dateStr.contains(".");
	            if(str1.length == 3 && str2.length == 3)
	            {
	                if(!existDot)
	                    timeFormat = "yyyy-MM-dd HH:mm:ss";
	                else
	                    timeFormat = "yyyy-MM-dd HH:mm:ss.SSS";
	            } else
	            if(str1.length == 1 && str2.length == 3)
	                timeFormat = "yyyy-MM-dd";
	            else
	            if(dateStr.length() == 14)
	                timeFormat = "yyyyMMddHHmmss";
	            else
	            if(dateStr.length() == 6)
	                timeFormat = "yyMMdd";
	            else
	            if(dateStr.length() == 4)
	                timeFormat = "yyyy";
	            else
	            if(str1.length == 4)
	                timeFormat = "yyyy-MM-dd HH:mm:ss:SSS";
	        }
	        System.out.println((new StringBuilder()).append("timeFormat is:").append(timeFormat).toString());
	        return timeFormat;
	    }

	    public static Timestamp getFormatTimestamp(String dateStr)
	    {
	        String format = getTimeFormat(dateStr);
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	        Date date = null;
	        try
	        {
	            date = simpleDateFormat.parse(dateStr);
	        }
	        catch(Exception ex)
	        {
	           ex.printStackTrace();
	        }
	        Timestamp timestamp = getFormatTimestamp(date, format);
	        return timestamp;
	    }

	    public static Timestamp getFormatTimestamp(Date date, String format)
	    {
	        Timestamp timestamp = null;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	        String resStr = simpleDateFormat.format(date);
	        timestamp = Timestamp.valueOf(resStr);
	        return timestamp;
	    }

	    public static Timestamp getFormatTimestampNow()
	    {
	        return getFormatTimestamp(new Date(), "yyyy-MM-dd HH:mm:ss");
	    }

	    public static String getFormatDate(Date date, String format)
	    {
	        String dateStr = null;
	        try
	        {
	            if(date != null)
	            {
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	                dateStr = simpleDateFormat.format(date);
	            }
	        }
	        catch(Exception ex)
	        {
	            ex.printStackTrace();
	        }
	        return dateStr;
	    }

	    public static Date convertDate(String dateStr, String format)
	    {
	        Date date = null;
	        try
	        {
	            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	            date = simpleDateFormat.parse(dateStr);
	        }
	        catch(Exception ex)
	        {
	        	ex.printStackTrace();
	        }
	        return date;
	    }

	    public static void main(String args[])
	    {
	        String str = "1900-1-1 1:1:1";
	        try
	        {
	            Timestamp timestamp = getFormatTimestamp(str);
	            System.out.println(timestamp);
	        }
	        catch(Exception ex)
	        {
	            ex.printStackTrace();
	        }
	    }

	    public static final String TIME_FORMAT_A = "yyyy-MM-dd HH:mm:ss";
	    public static final String TIME_FORMAT_B = "yyyyMMddHHmmss";
	    public static final String TIME_FORMAT_C = "yyyy-MM-dd HH:mm:ss:SSS";
	    public static final String TIME_FORMAT_D = "yyyy-MM-dd HH:mm:ss.SSS";
	    public static final String DATE_FORMAT = "yyyy-MM-dd";
	    public static final String DATE_FORMAT_B = "yyMMdd";
	    public static final String YEAR_FORMAT = "yyyy";
}
