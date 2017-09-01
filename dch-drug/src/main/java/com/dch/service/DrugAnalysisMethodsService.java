package com.dch.service;

import com.dch.entity.DrugAnalysisMethods;
import com.dch.entity.DrugExamOrg;
import com.dch.facade.DrugAnalysisMethodsFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drug/drug-analysis-methods")
@Produces("application/json")
@Controller
public class DrugAnalysisMethodsService {
    @Autowired
    private DrugAnalysisMethodsFacade drugAnalysisMethodsFacade;

    /**
     * 添加、修改、删除药品专利文献
     * @param drugAnalysisMethods
     * @return
     */
    @POST
    @Path("merge-drug-analysis-methods")
    @Transactional
    public Response mergeDrugAnalysisMehtods(DrugAnalysisMethods drugAnalysisMethods){
        return drugAnalysisMethodsFacade.mergeDrugAnalysisMehtods(drugAnalysisMethods);
    }

    /**
     * 获取药品专利文献
     * @param methodName
     * @param wherehql
     * @return
     */
    @GET
    @Path("get-drug-analysis-methodses")
    public Page<DrugAnalysisMethods> getDrugAnalysisMethodses(@QueryParam("methodName") String methodName,
                                                              @QueryParam("wherehql") String wherehql,@QueryParam("perPage")int perPage,
                                                              @QueryParam("currentPage")int currentPage){

        return drugAnalysisMethodsFacade.getDrugAnalysisMethodses(methodName,wherehql,perPage,currentPage);
    }

    /**
     * 获取单个药品专利文献
     * @param methodId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-analysis-methods")
    public DrugAnalysisMethods getDrugAnalysisMethods(@QueryParam("methodId") String methodId) throws Exception {

        return drugAnalysisMethodsFacade.getDrugAnalysisMethods(methodId);
    }
}
