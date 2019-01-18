package com.dch.service;

import com.dch.entity.OrgInfo;
import com.dch.entity.ResearchProject;
import com.dch.facade.ResearchProjectFacade;
import com.dch.vo.ResearchOrgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
@Produces(APPLICATION_JSON)
@Path("research-project")
public class ResearchProjectService {

    @Autowired
    private ResearchProjectFacade researchProjectFacade ;


    /***
     * 根据机构ID获取机构所负责的项目等信息
     * @param orgId
     * @return
     */
    @GET
    @Path("get-org")
    public ResearchOrgVO getResearchOrg(@QueryParam("orgId") String orgId){
        return researchProjectFacade.getResearchOrg(orgId);
    }


    @GET
    @Path("get-research-project")
    public List<ResearchProject> getResearcProjectByUserId(@QueryParam("userId") String userId){
        return researchProjectFacade.getResearchProject(userId);
    }


    /**
     * 获取所有机构，并包含机构的信息
     * @param orgName
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-all-org")
    public List<ResearchOrgVO> getAllResearchOrg(@QueryParam("orgName") String orgName ,@QueryParam("perPage") int perPage,
                                                 @QueryParam("currentPage") int currentPage){

        return researchProjectFacade.getAllResearchOrg(orgName,perPage,currentPage);
    }


    /**
     * 修改机构信息
     * @param orgInfo
     * @return
     */
    @POST
    @Path("merge-org")
    @Transactional
    public OrgInfo mergeOrgInfo(OrgInfo orgInfo){
        return researchProjectFacade.merge(orgInfo);
    }

    /**
     * 修改科研项目信息
     * @param project
     * @return
     */
    @POST
    @Path("merge-research-project")
    @Transactional
    public ResearchProject mergeResearchProject(ResearchProject project){
        return researchProjectFacade.merge(project);
    }

}
