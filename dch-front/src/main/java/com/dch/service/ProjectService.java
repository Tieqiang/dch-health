package com.dch.service;

import com.dch.entity.ProjectInfo;
import com.dch.entity.ProjectSummaryPage;
import com.dch.facade.ProjectFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Produces("application/json")
@Path("front/project")
@Controller
public class ProjectService {

    @Autowired
    private ProjectFacade projectFacade ;


    /**
     * 添加、删除、修改项目信息
     * @param projectInfo
     * @return
     */
    @Transactional
    @POST
    @Path("merge-project")
    public ProjectInfo mergeProject(ProjectInfo projectInfo){

        return projectFacade.merge(projectInfo) ;
    }

    /**
     * 获取项目信息
     * @param type 1表示数据库，0表示工具
     * @param showFlag 1显示在首页，0不显示在首页 不传递不加以判断
     * @return
     */
    @GET
    @Path("get-projects")
    public List<ProjectInfo> getProjects(@QueryParam("type") String type,@QueryParam("showFlag") String showFlag){
        return projectFacade.getProjects(type,showFlag);
    }

    /**
     * 获取单个ProjectInfo
     * @param projectId
     * @return
     */
    @GET
    @Path("get-project")
    public ProjectInfo getProject(@QueryParam("projectId") String projectId) throws Exception {
        ProjectInfo projectInfo = projectFacade.get(ProjectInfo.class, projectId);
        if ("-1".equals(projectInfo.getStatus())){
            throw new Exception("该内容已经被删除。");
        }
        return projectInfo;
    }


    /**
     * 添加、修改、删除项目页
     * @param projectSummaryPage
     * @return
     */
    @POST
    @Path("merge-project-page")
    @Transactional
    public ProjectSummaryPage mergeProjectPage(ProjectSummaryPage projectSummaryPage){
        return projectFacade.merge(projectSummaryPage);
    }


    /**
     * 获取所有的页面
     * @param projectId
     * @return
     */
    @GET
    @Path("get-project-pages")
    public List<ProjectSummaryPage> getProjectPages(@QueryParam("projectId") String  projectId){
        return projectFacade.getProjectPages(projectId);
    }


    /**
     * 获取具体的页面内容
     * @param pageId
     * @return
     */
    @GET
    @Path("get-project-page")
    public ProjectSummaryPage getProjectPage(@QueryParam("pageId") String pageId) throws Exception {
        ProjectSummaryPage projectSummaryPage = projectFacade.get(ProjectSummaryPage.class, pageId);
        if ("-1".equals(projectSummaryPage.getStatus())){
            throw new Exception("该内容已经被删除。");
        }
        return projectSummaryPage;
    }
}
