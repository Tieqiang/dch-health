package com.dch.schedule;

import java.util.Date;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.timer.TimerNotification;



public class DSchedulerBO {

	private ObjectInstance timer;
	private MBeanServer mBeanServer;
	public DSchedulerBO() throws Exception{
		System.out.println("调用==========================1");
        mBeanServer = MBeanServerFactory.createMBeanServer();
        timer = mBeanServer.createMBean("javax.management.timer.Timer",
                                        new ObjectName("DefaultDomain", "service", "timer"));
        start();
    }
	public void addScheduleTask(IScheduleTask schedulerTask) {
		try{
			Date scheduleTime = schedulerTask.getScheduleTime();
			if(scheduleTime != null){
				if(schedulerTask.getTaskId()!=null){
					removeScheduleTask(schedulerTask);
				}
				System.out.println("添加定时任务[message=" + schedulerTask.getTaskMessage() +
                        ", time=" + TimeFormatHelper.getFormatDate(schedulerTask.getScheduleTime(), TimeFormatHelper.TIME_FORMAT_A) + "]");

				final Integer taskId = (Integer)mBeanServer.invoke(timer.getObjectName(), "addNotification",
						new Object[]{schedulerTask.getTaskType(),schedulerTask.getTaskMessage(),schedulerTask.getTaskUserData(),scheduleTime},
						new String[]{String.class.getName(),String.class.getName(),Object.class.getName(),Date.class.getName()});
				schedulerTask.setTaskId(taskId);
					mBeanServer.addNotificationListener(timer.getObjectName(), schedulerTask,
							new NotificationFilter() {
								public boolean isNotificationEnabled(Notification notification) {
									// TODO Auto-generated method stub
									if(notification instanceof TimerNotification){
										TimerNotification timerNotification = (TimerNotification)notification;
										return taskId.equals(timerNotification.getNotificationID());
									}
									return false;
								}
							},
							this);
				Date nowDate = new Date();
				String nowDateStr = TimeFormatHelper.getFormatDate(nowDate,TimeFormatHelper.TIME_FORMAT_A);
				String endDateStr = TimeFormatHelper.getFormatDate(schedulerTask.getEndTime(),TimeFormatHelper.TIME_FORMAT_A);
				System.out.println("开始时间11="+nowDateStr);
				System.out.println("结束时间11="+TimeFormatHelper.getFormatDate(schedulerTask.getEndTime(),TimeFormatHelper.TIME_FORMAT_A));
				if(schedulerTask.getEndTime()!=null && (nowDateStr.equals(endDateStr)||nowDate.after(schedulerTask.getEndTime()))){
					removeScheduleTask(schedulerTask);
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void stop(){
        try {
            removeAllScheduleTask();
            mBeanServer.invoke(timer.getObjectName(), "stop", new Object[] {}, new String[] {});
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
	private void removeAllScheduleTask() {
		try{
			mBeanServer.invoke(timer.getObjectName(), "removeAllNotifications", new Object[] {}, new String[] {});
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void removeScheduleTask(IScheduleTask schedulerTask) {
        try {
        	System.out.println(schedulerTask);
			System.out.println("删除定时任务:" + schedulerTask.getTaskMessage());
            mBeanServer.removeNotificationListener(timer.getObjectName(), schedulerTask);
        } catch (Exception ex) {
			System.out.println("删除定时任务出错===============");
           ex.printStackTrace();
        }
    }
	private void start() {
		System.out.println("调用==========================2");
		try{
			mBeanServer.invoke(timer.getObjectName(), "start", new Object[]{}, new String[]{});
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
