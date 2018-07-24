package com.dch.schedule;

import java.util.Date;

import javax.management.Notification;

public abstract class AbstractScheduleTaskBO implements IScheduleTask {
	private Integer taskId;
    private Date scheduleTime;
    private String taskType;
    private Object taskUserData;
    private String taskMessage;
    private final PeriodicTimer periodicTimer;
    private Date endTime;
    
    public AbstractScheduleTaskBO()
    {
        taskType = "TransNmsScheduleTask";
        taskMessage = "";
        periodicTimer = new PeriodicTimer();
    }

    public AbstractScheduleTaskBO(String boName)
    {
        taskType = "TransNmsScheduleTask";
        taskMessage = "";
        periodicTimer = new PeriodicTimer();
    }
    
	public Date getScheduleTime() {
		 return scheduleTime;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public String getTaskMessage() {
		return taskMessage;
	}

	public String getTaskType() {
		return taskType;
	}

	public Object getTaskUserData() {
		return taskUserData;
	}

	public Date nextScheduleTime() {
		scheduleTime = periodicTimer.nextPeriodicTime();
        return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		 this.scheduleTime = scheduleTime;
	}

	public void setTaskId(Integer taskId) {
        this.taskId = taskId;
	}

	public void setTaskMessage(String taskMessage) {

		this.taskMessage = taskMessage;
	}

	public void setTaskType(String taskType) {
        this.taskType = taskType;
	}

	public void setTaskUserData(Object userData) {
		taskUserData = userData;
	}
	public void setStartTime(String startTimeStr)
    {
        Date startTime = periodicTimer.setStartTime(startTimeStr);
        setScheduleTime(startTime);
    }
	
    public void setPeriod(int period)
    {
        periodicTimer.setPeriod(period);
    }

    public void setUnit(String unit)
    {
        periodicTimer.setUnit(unit);
    }

    protected abstract void doScheduleTask(Notification notification, Object obj)
        throws Exception;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
