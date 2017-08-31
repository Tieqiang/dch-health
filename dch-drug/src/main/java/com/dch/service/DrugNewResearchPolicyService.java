package com.dch.service;

import com.dch.entity.DrugNewResearchPolicy;
import com.dch.facade.DrugNewResearchPolicyFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drug/drug-new-research-policy")
@Produces("application/json")
@Controller
public class DrugNewResearchPolicyService {

    @Autowired
    private DrugNewResearchPolicyFacade drugNewResearchPolicyFacade;

    /**
     * 添加、删除、修改新药政策
     * @param drugNewResearchPolicy
     * @return
     */
    @POST
    @Path("merge-policy")
    @Transactional
    public Response mergeNewResearchPolicy(DrugNewResearchPolicy drugNewResearchPolicy){

        return drugNewResearchPolicyFacade.mergeNewResearchPolicy(drugNewResearchPolicy);
    }

    /**
     * 获取研发政策
     * @param policyTypeFlag
     * @return
     */
    @GET
    @Path("get-policys")
    public Page<DrugNewResearchPolicy> getNewResearchPolicys(@QueryParam("policyTypeFlag") String policyTypeFlag,
                                                             @QueryParam("perPage") int perPage,
                                                             @QueryParam("currentPage") int currentPage){

        return drugNewResearchPolicyFacade.getNewResearchPolicys(policyTypeFlag,perPage,currentPage);
    }

    @GET
    @Path("get-policy")
    public DrugNewResearchPolicy getNewResearchPolicy(@QueryParam("policyId") String policyId) throws Exception {
        return drugNewResearchPolicyFacade.getNewResearchPolicy(policyId);
    }
}
