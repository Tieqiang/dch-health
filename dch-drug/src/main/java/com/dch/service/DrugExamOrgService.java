package com.dch.service;

import com.dch.entity.DrugExamOrg;
import com.dch.facade.DrugExamOrgFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces("application/json")
@Path("drug/drug-exam-org")
@Controller
public class DrugExamOrgService {
    @Autowired
    private DrugExamOrgFacade drugExamOrgFacade;

    /**
     * 添加、删除、修改临床药物试验机构
     * @param drugExamOrg
     * @return
     */
    @POST
    @Path("merge-drug-exam-org")
    @Transactional
    public Response mergeDrugExamOrg(DrugExamOrg drugExamOrg){
        return drugExamOrgFacade.mergeDrugExamOrg(drugExamOrg);
    }

    /**
     * 获取临床药物试验机构
     * @param orgName
     * @return
     */
    @GET
    @Path("get-drug-exam-orgs")
    public Page<DrugExamOrg> getDrugExamOrgs(@QueryParam("orgName") String orgName,
                                             @QueryParam("wherehql") String wherehql,
                                             @QueryParam("perPage") int perPage,
                                             @QueryParam("currentPage") int currentPage){

        return drugExamOrgFacade.getDrugExamOrgs(orgName,wherehql,perPage,currentPage);
    }

    /**
     * 获取单一的临床药物试验机构
     * @param examOrgId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-exam-org")
    public DrugExamOrg getDrugExamOrg(@QueryParam("examOrgId") String examOrgId) throws Exception {

        return drugExamOrgFacade.getDrugExamOrg(examOrgId);
    }
}
