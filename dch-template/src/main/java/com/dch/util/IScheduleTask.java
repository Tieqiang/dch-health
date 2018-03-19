package com.dch.util;

import javax.management.NotificationListener;
import java.util.Date;

public interface IScheduleTask extends NotificationListener {

	    public abstract Integer getTaskId();

	    public abstract String getTaskType();

	    public abstract Date getScheduleTime();

	    public abstract String getTaskMessage();

	    public abstract Object getTaskUserData();

	    public abstract Date nextScheduleTime();

	    public abstract void setTaskId(Integer integer);

	    public abstract void setTaskType(String s);

	    public abstract void setScheduleTime(Date date);

	    public abstract void setTaskMessage(String s);

	    public abstract void setTaskUserData(Object obj);

	    public abstract Date getEndTime();

	    public abstract void setEndTime(Date endTime);
}
