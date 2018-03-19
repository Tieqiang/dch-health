package com.dch.util;

import com.dch.entity.TemplateTask;

import javax.management.Notification;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sunkqa on 2018/3/8.
 */
public class TemplateSchedulerTaskBO extends AbstractScheduleTaskBO{
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
            String taskRunTime = getScheduleDate(notification);
            this.setStartTime(taskRunTime);
            addScheduleTask(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getScheduleDate(Notification notification) {
        // TODO Auto-generated method stub
        TemplateTask templateTask = (TemplateTask)notification.getUserData();
        Calendar calendar = Calendar.getInstance();
        //calendar.set(calendar.DAY_OF_MONTH, 1);
        //calendar.add(calendar.MONTH, 1);
        //calendar.set(calendar.HOUR_OF_DAY, 0);
        //calendar.set(calendar.MINUTE, 30);
        if(templateTask.getTaskCycle()>0){
            int value = Long.valueOf(templateTask.getTaskCycle()).intValue();
            calendar.add(calendar.MINUTE, value);
        }else{
            calendar.add(calendar.MINUTE, 1);
        }
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
    public void addScheduleTask(TemplateSchedulerTaskBO bo) throws Exception {
        getDSchedulerBO().addScheduleTask(bo);
    }

    public void removeScheduleTask(TemplateSchedulerTaskBO bo) throws Exception {
        getDSchedulerBO().removeScheduleTask(bo);
    }
}
