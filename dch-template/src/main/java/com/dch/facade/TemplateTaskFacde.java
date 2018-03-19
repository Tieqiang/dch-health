package com.dch.facade;

import com.dch.entity.TemplateTask;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PeriodicTimer;
import com.dch.util.TemplateSchedulerTaskBO;
import com.dch.util.TimeFormatHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/8.
 */
@Component
public class TemplateTaskFacde extends BaseFacade {

    private static Map<String, TemplateSchedulerTaskBO> templateSchedulerTaskMap = new HashMap<String, TemplateSchedulerTaskBO>();

    private int getTaskPeriod(long taskCycle){
        int periodUnit = 24*60;//默认为24小时执行一次
        if(taskCycle>0){
            int cycle = Long.valueOf(taskCycle).intValue();
            //periodUnit = cycle*60;
            periodUnit = cycle;//测试1分钟为粒度
        }
        return periodUnit;
    }

    @Transactional
    public Response addTemplateTask(TemplateTask templateTask) throws Exception{
        if(templateTask!=null){
            templateTask = merge(templateTask);//保存到数据库
            //添加定时任务
            TemplateSchedulerTaskBO templateSchedulerTaskBO = new TemplateSchedulerTaskBO();
            int period = getTaskPeriod(templateTask.getTaskCycle());
            templateSchedulerTaskBO.setPeriod(period);
            templateSchedulerTaskBO.setUnit(PeriodicTimer.PERIOD_UNIT_MINUTE);
            Date startTime = null;
            if(templateTask.getStartTime() == null){
                startTime = new Date(TimeFormatHelper.getFormatTimestampNow().getTime() + 30*1000);
                templateSchedulerTaskBO.setStartTime(TimeFormatHelper.getFormatDate(startTime, TimeFormatHelper.TIME_FORMAT_A));
                //templateSchedulerTaskBO.setScheduleTime(new Date(startTime.getTime()+ period*60*1000));
            }else{
                startTime = new Date(templateTask.getStartTime().getTime()+ 30*1000);
                templateSchedulerTaskBO.setStartTime(TimeFormatHelper.getFormatDate(startTime, TimeFormatHelper.TIME_FORMAT_A));
                //templateSchedulerTaskBO.setScheduleTime(new Date(startTime.getTime()+ period*60*1000));
            }
            templateSchedulerTaskBO.setTaskUserData(templateTask);
            templateSchedulerTaskMap.put(templateTask.getId(), templateSchedulerTaskBO);
            templateSchedulerTaskBO.addScheduleTask(templateSchedulerTaskBO);
            System.out.println("成功添加定时任务[ " + templateTask.getTaskName() + " ],任务执行时间"
                    + startTime + " - " +  templateTask.getEndTime()
                    + ",执行间隔周期：" + period + " " + PeriodicTimer.PERIOD_UNIT_MINUTE);
        }
        return Response.status(Response.Status.OK).entity(templateTask).build();
    }

    @Transactional
    public Response modifyTemplateTask(TemplateTask templateTask) {
        TemplateTask merge = merge(templateTask);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 更改任务运行状态为停止运行
     * @param templateTask
     * @return
     * @throws Exception
     */
    public Response changeRunStatus(TemplateTask templateTask) throws Exception{
        removeTemplateTask(templateTask.getId());
        templateTask.setIsRun("0");
        TemplateTask merge = merge(templateTask);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 移除定时任务
     * @param taskId
     * @throws Exception
     */
    public synchronized void removeTemplateTask(String taskId) throws Exception{
        TemplateSchedulerTaskBO taskBO = getTemplateTask(taskId);
        if (taskBO != null) {
            taskBO.removeScheduleTask(taskBO);
            templateSchedulerTaskMap.remove(templateSchedulerTaskMap.get(taskId));
            System.out.println("移除定时任务：" + taskId);
        }
    }

    private TemplateSchedulerTaskBO getTemplateTask(String taskId) {
        return templateSchedulerTaskMap.get(taskId);
    }

    public Response startTemplateTask(TemplateTask templateTask) throws Exception{
       return addTemplateTask(templateTask);
    }

    /**
     * 删除数据库定时任务
     * @param templateTask
     * @return
     */
    public Response deleteTemplateTask(TemplateTask templateTask) throws Exception{
        TemplateTask merge = merge(templateTask);
        removeTemplateTask(templateTask.getId());
        return Response.status(Response.Status.OK).entity(merge).build();
    }
}
