package com.dch.schedule;

import java.util.Calendar;
import java.util.Date;

public class PeriodicTimer {
	public PeriodicTimer()
    {
    }

    public int getPeriod()
    {
        return period;
    }

    public String getUnit()
    {
        return unit;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public Date setStartTime(String startTimeStr)
    {
        return setStartTime(TimeFormatHelper.convertDate(startTimeStr, "yyyy-MM-dd HH:mm:ss"));
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public Date setStartTime(Date startTime)
    {
        this.startTime = startTime;
        curTime = startTime;
        if(unit != null && period > 0)
            curTime = adjustStartTime(startTime, unit, period);
        return curTime;
    }

    public Date nextPeriodicTime()
    {
        curTime = nextPeriodicTime(curTime, unit, period);
        return curTime;
    }

    private static Date nextPeriodicTime(Date startTime, String unit, int period)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime.getTime());
        Date nextPeriodicTime;
        if("year".equals(unit))
        {
            calendar.add(1, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        if("month".equals(unit))
        {
            calendar.add(2, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        if("day".equals(unit))
        {
            calendar.add(6, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        if("hour".equals(unit))
        {
            calendar.add(10, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        if("min".equals(unit))
        {
            calendar.add(12, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        if("sec".equals(unit))
        {
            calendar.add(13, period);
            nextPeriodicTime = new Date(calendar.getTimeInMillis());
        } else
        {
            nextPeriodicTime = null;
        }
        return nextPeriodicTime;
    }

    private static Date adjustStartTime(Date startTime, String unit, int period)
    {
        Date adjustedTime = startTime;
        if(startTime.getTime() < System.currentTimeMillis())
            if("year".equals(unit) || "month".equals(unit))
            {
                do
                    adjustedTime = nextPeriodicTime(adjustedTime, unit, period);
                while(adjustedTime.getTime() <= System.currentTimeMillis());
            } else
            {
                Date nextTime = nextPeriodicTime(startTime, unit, period);
                long periodicMillSec = nextTime.getTime() - startTime.getTime();
                long curMillSec = System.currentTimeMillis() - startTime.getTime();
                long interval = curMillSec / periodicMillSec;
                adjustedTime = new Date(startTime.getTime() + (interval + 1L) * periodicMillSec);
            }
        return adjustedTime;
    }

    public static final String PERIOD_UNIT_YEAR = "year";
    public static final String PERIOD_UNIT_MONTH = "month";
    public static final String PERIOD_UNIT_DAY = "day";
    public static final String PERIOD_UNIT_HOUR = "hour";
    public static final String PERIOD_UNIT_MINUTE = "min";
    public static final String PERIOD_UNIT_SECOND = "sec";
    public static final String START_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private int period;
    private String unit;
    private Date curTime;
    private Date startTime;
}
