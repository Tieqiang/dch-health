package com.dch.schedule;

import java.util.Calendar;
import java.util.Date;

import javax.management.Notification;

public class MySchedule extends AbstractScheduleTaskBO{
	
    private static DSchedulerBO ds = null;
	@Override
	protected void doScheduleTask(Notification notification, Object obj)
			throws Exception {
		System.out.println("开始执行定时任务===");
		System.out.println("开始做饭！");
		System.out.println("饭做好了！");
		System.out.println("开始吃饭！");
		System.out.println("刷碗，刷锅！");
		System.out.println("打王者荣耀！");
		System.out.println("结束执行定时任务===");
	}

	public void handleNotification(Notification notification, Object handback) {
		try{
			System.out.println("定时服务通知：message="+notification.getMessage()
					+"type="+notification.getType()
					+"date="+new Date(notification.getTimeStamp())
					+"source="+notification.getSource()
					+"userData="+notification.getUserData());
			doScheduleTask(notification,handback);
			String taskRunTime = getScheduleDate();
			this.setStartTime(taskRunTime);
			addScheduleTask(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String getScheduleDate() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		//calendar.set(calendar.DAY_OF_MONTH, 1);
		//calendar.add(calendar.MONTH, 1);
		//calendar.set(calendar.HOUR_OF_DAY, 0);
		//calendar.set(calendar.MINUTE, 30);
		calendar.add(calendar.MINUTE, 1);
		String time = TimeFormatHelper.getFormatDate(new Date(calendar.getTimeInMillis()), TimeFormatHelper.TIME_FORMAT_A);
		return time;
	}

	public  DSchedulerBO getDSchedulerBO() throws Exception{
		if(ds==null){
			synchronized (DSchedulerBO.class) {
				if(ds==null){
					ds = new DSchedulerBO();
				}
			}
		}
		return ds;
	}
	private void addScheduleTask(MySchedule bo) throws Exception {
		getDSchedulerBO().addScheduleTask(bo);
	}
	public static void main(String[] args) throws Exception {
		MySchedule ms = new MySchedule();
		ms.setPeriod(1);
		ms.setUnit(PeriodicTimer.PERIOD_UNIT_MINUTE);
		String taskRunTime = TimeFormatHelper.getFormatDate(new Date(System
				.currentTimeMillis() + 1000 * 1),
				TimeFormatHelper.TIME_FORMAT_A);
		System.out.println("开始时间="+taskRunTime);
		ms.setStartTime(taskRunTime);
		ms.setTaskUserData("MySchedule");
		ms.setTaskMessage("MySchedule");
		ms.setEndTime(new Date(System
				.currentTimeMillis() + 1000 * 70));
		System.out.println("结束时间=1"+TimeFormatHelper.getFormatDate(ms.getEndTime(),TimeFormatHelper.TIME_FORMAT_A));
		ms.addScheduleTask(ms);

	}
}
