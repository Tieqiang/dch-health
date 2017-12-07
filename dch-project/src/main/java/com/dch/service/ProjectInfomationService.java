package com.dch.service;

import com.dch.entity.ProjectInfomation;
import com.dch.facade.ProjectInfomationFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Controller
@Path("project/project-infomation")
@Produces("application/json")
public class ProjectInfomationService {

    @Autowired
    private ProjectInfomationFacade projectInfomationFacade;

    /**
     * 添加、删除、修改项目动态
     * @param projectInfomation
     * @return
     */
    @POST
    @Path("merge-project-infomation")
    public Response mergeProjectInfomation(ProjectInfomation projectInfomation){
        return projectInfomationFacade.mergeProjectInfomation(projectInfomation);
    }

    /**
     * 获取项目动态
     * @param projectId 项目id
     * @param perPage 每页条数
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-project-infomations")
    public Page<ProjectInfomation> getProjectInfoMations(@QueryParam("projectId")String projectId,@QueryParam("perPage")int perPage,
                                                         @QueryParam("currentPage")int currentPage){
        return projectInfomationFacade.getProjectInfoMations(projectId,perPage,currentPage);
    }

    /**
     * 获取具体的项目动态
     * @param infoId
     * @return
     */
    @GET
    @Path("get-project-infomation")
    public ProjectInfomation getProjectInfoMation(@QueryParam("infoId")String infoId) throws Exception{
        return projectInfomationFacade.getProjectInfoMation(infoId);
    }

    /**
     * 获取最新的N条项目动态信息
     * @return
     */
    @GET
    @Path("get-recent-informations")
    public List<ProjectInfomation> getRecentProjectInformations(@QueryParam("playNum")int playNum){
        return projectInfomationFacade.getRecentProjectInformations(playNum);
    }
}
