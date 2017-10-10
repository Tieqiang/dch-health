package com.dch.service;

import com.dch.entity.ProjectMember;
import com.dch.facade.ProjectMemberFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("project/project-member")
@Produces("application/json")
@Controller
public class ProjectMemberService {
    @Autowired
    private ProjectMemberFacade projectMemberFacade;

    /**
     * 添加、删除、修改科员项目成员
     * @param projectMember
     * @return
     */
    @Path("merge-project-member")
    @POST
    @Transactional
    public Response mergeProjectMember(ProjectMember projectMember){
        return projectMemberFacade.mergeProjectMember(projectMember);
    }

    /**
     * 获取具体的项目组成员
     * @param projectId
     * @return
     */
    @GET
    @Path("get-project-members")
    public List<ProjectMember> getProjectMembers(@QueryParam("projectId") String projectId){
        return projectMemberFacade.getProjectMembers(projectId);
    }
}
