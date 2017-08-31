package com.dch.service;

import com.dch.entity.DrugPatent;
import com.dch.facade.DrugPatentFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 药品专利文献维护
 * Created by Administrator on 2017/8/23.
 */
@Produces("application/json")
@Path("drug/drug-patent")
@Controller
public class DrugPatentService {

    @Autowired
    private DrugPatentFacade drugPatentFacade;

    /**
     * 添加、修改、删除药品专利文献
     * @param drugPatent
     * @return
     */
    @POST
    @Path("merge-drug-patent")
    public Response mergeDrugPatent(DrugPatent drugPatent){
        return Response.status(Response.Status.OK).entity(drugPatentFacade.mergeDrugPatent(drugPatent)).build();
    }

    /**
     * 获取专利文献
     * @param patentName
     * @param wherehql
     * @return
     */
    @GET
    @Path("get-drug-patents")
    public Page<DrugPatent> getDrugPatents(@QueryParam("patentName")String patentName, @QueryParam("perPage")int perPage, @QueryParam("currentPage")int currentPage, @QueryParam("wherehql")String wherehql){
        return drugPatentFacade.getDrugPatents(patentName,perPage,currentPage,wherehql);
    }

    /**
     * 获取单个专利文献
     * @param patentId
     * @return
     */
    @GET
    @Path("get-drug-patent")
    public DrugPatent getDrugPatent(@QueryParam("patentId")String patentId) throws Exception{
        return drugPatentFacade.getDrugPatent(patentId);
    }
}
