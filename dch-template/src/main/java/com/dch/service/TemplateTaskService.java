package com.dch.service;

import com.dch.entity.TemplateTask;
import com.dch.facade.TemplateTaskFacde;
import com.dch.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sunkqa on 2018/3/8.
 */
@Produces("application/json")
@Path("template/task1")
@Controller
public class TemplateTaskService {

    @Autowired
    private TemplateTaskFacde templateTaskFacde;

    @GET
    @Path("test")
    @Transactional
    public List<String> testTask() throws Exception{
        List<String> list = new ArrayList<>();
        TemplateTask templateTask = new TemplateTask();
        templateTask.setTaskName("test定时任务");
        templateTask.setTaskType("定时任务");
        templateTask.setTaskCycle(2);
        templateTask.setRelatedTemplateMasterId("001");
        templateTask.setIsRun("2");
        templateTask.setCreateBy("sun");
        templateTask.setModifyBy("sun");
        templateTask.setStatus("1");
        addTemplateTask(templateTask);
        list.add("1");
        return list;
    }
    /**
     * 添加表单定时任务
     * @param templateTask
     * @return
     */
    @POST
    @Path("add")
    @Transactional
    public Response addTemplateTask(TemplateTask templateTask)throws Exception{
        Response response = null;
        try {
            templateTask.setStartTime(new Date());
            templateTask.setCreateDate(new Timestamp(System.currentTimeMillis()));
            templateTask.setModifyDate(new Timestamp(System.currentTimeMillis()));
            response = templateTaskFacde.addTemplateTask(templateTask);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    /**
     * 修改表单定时任务
     * @param templateTask
     * @return
     * @throws Exception
     */
    @POST
    @Path("modify")
    @Transactional
    public Response modifyTemplateTask(TemplateTask templateTask) throws Exception{
        if("1".equals(templateTask.getIsRun())){//说明该定时任务在运行
            throw new Exception("定时任务:"+templateTask.getTaskName()+"在运行，请先停止运行");
        }
        Response response = null;
        try {
            response = templateTaskFacde.modifyTemplateTask(templateTask);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    /**
     * 停止定时任务执行
     * @param templateTask
     * @return
     * @throws Exception
     */
    @POST
    @Path("stop")
    @Transactional
    public Response changeRunStatus(TemplateTask templateTask) throws Exception{
        Response response = null;
        try {
            response = templateTaskFacde.changeRunStatus(templateTask);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    /**
     * 重新启动定时任务
     * @param templateTask
     * @return
     * @throws Exception
     */
    @POST
    @Path("start")
    @Transactional
    public Response startTemplateTask(TemplateTask templateTask) throws Exception{
        Response response = null;
        try {
            templateTask.setIsRun("1");
            response = templateTaskFacde.startTemplateTask(templateTask);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    @GET
    @Path("delete")
    @Transactional
    public Response deleteTemplateTask(@QueryParam("id") String taskId) throws Exception{
        Response response = null;
        if(StringUtils.isEmptyParam(taskId)){
            throw new Exception("任务主键不能为空");
        }
        try {
            TemplateTask templateTask = templateTaskFacde.get(TemplateTask.class,taskId);
            if(templateTask==null){
                throw new Exception("任务信息不存在");
            }
            templateTask.setStatus("-1");//设置状态为-1 代表删除了
            response = templateTaskFacde.deleteTemplateTask(templateTask);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return response;
    }
}
