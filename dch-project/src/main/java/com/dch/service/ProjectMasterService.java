package com.dch.service;

import com.dch.entity.ProjectMaster;
import com.dch.facade.ProjectMasterFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("project/project-master")
@Produces("application/json")
@Controller
public class ProjectMasterService {
    @Autowired
    private ProjectMasterFacade projectMasterFacade;

    /**
     * 添加、删除、修改科研项目
     * @param projectMaster
     * @return
     */
    @Path("merge-project-master")
    @POST
    @Transactional
    public Response mergeProjectMaster(ProjectMaster projectMaster) throws Exception {
        return projectMasterFacade.mergeProjectMaster(projectMaster);
    }

    /**
     * 根据条件获取科研项目
     * @param projectName
     * @param projectPerson
     * @param perPage
     * @param currentPage
     * @return
     */
    @Path("get-project-masters")
    @GET
    public Page<ProjectMaster> getProjectMasters(@QueryParam("projectName") String projectName,
                                                 @QueryParam("projectPerson") String projectPerson,
                                                 @QueryParam("createrId") String createrId,
                                                 @QueryParam("type") String type,
                                                 @QueryParam("perPage") int perPage,
                                                 @QueryParam("currentPage") int currentPage){
        return projectMasterFacade.getProjectMasters(projectName,projectPerson,createrId,type,perPage,currentPage);

    }

    /**
     * 获取具体的科研项目
     * @param projectId
     * @return
     */
    @GET
    @Path("get-project-master")
    public ProjectMaster getProjectMaster(@QueryParam("projectId") String projectId) throws Exception {
        return projectMasterFacade.getProjectMaster(projectId);
    }

    /**
     * 根据用户id获取其创建的项目
     * @param userId
     * @return
     */
    @GET
    @Path("get-project-masters-By-creater")
    public List<ProjectMaster> getProjectMastersByCreater(@QueryParam("userId")String userId){
        return projectMasterFacade.getProjectMastersByCreater(userId);
    }

    /**
     * 根据项目名称模糊匹配创建者未参与的项目
     * @param projectName 项目名称
     * @param createrId 创建者id
     * @param perPage 每页条数
     * @param currentPage 当前页
     * @return
     */
    @Path("get-not-join-project-masters")
    @GET
    public Page<ProjectMaster> getNotJoinProjectMasters(@QueryParam("projectName") String projectName,
                                                 @QueryParam("createrId") String createrId,
                                                 @QueryParam("perPage") int perPage,
                                                 @QueryParam("currentPage") int currentPage){
        return projectMasterFacade.getNotJoinProjectMasters(projectName,createrId,perPage,currentPage);
    }

    /**
     * 退出参与的项目
     * @param projectMaster
     * @return
     */
    @Path("quit-project-masters")
    @POST
    @Transactional
    public Response QuitProjectMaster(ProjectMaster projectMaster){
        return projectMasterFacade.quitProjectMaster(projectMaster);
    }
}
