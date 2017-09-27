package com.dch.service;

import com.dch.entity.ProjectMaster;
import com.dch.facade.ProjectMasterFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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
    public Response mergeProjectMaster(ProjectMaster projectMaster){
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
                                                 @QueryParam("perPage") int perPage,
                                                 @QueryParam("currentPage") int currentPage){
        return projectMasterFacade.getProjectMasters(projectName,projectPerson,perPage,currentPage);

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
}
