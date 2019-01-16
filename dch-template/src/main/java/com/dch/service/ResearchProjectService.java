package com.dch.service;

import com.dch.facade.ResearchProjectFacade;
import com.dch.vo.ResearchOrgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
@Produces(APPLICATION_JSON)
@Path("research-project")
public class ResearchProjectService {

    @Autowired
    private ResearchProjectFacade researchProjectFacade ;


    @GET
    @Path("get-org")
    public ResearchOrgVO getResearchOrg(@QueryParam("orgId") String orgId){
        return researchProjectFacade.getResearchOrg(orgId);
    }

}
