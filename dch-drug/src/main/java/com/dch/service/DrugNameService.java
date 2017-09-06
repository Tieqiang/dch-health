package com.dch.service;

import com.dch.entity.DrugNameDict;
import com.dch.facade.DrugNameFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 药品名称维护
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Path("drug/drug-name")
@Controller
public class DrugNameService {

    @Autowired
    private DrugNameFacade drugNameFacade;

    /**
     * 添加、删除、修改药品名称
     * @param drugNameDict
     * @return
     */
    @POST
    @Path("merge-drug-name")
    public Response mergeDrugName(DrugNameDict drugNameDict) throws Exception{
        return Response.status(Response.Status.OK).entity(drugNameFacade.mergeDrugName(drugNameDict)).build();
    }

    /**
     * 获取药品名称集合
     * @param drugCode
     * @param inputCode
     * @return
     */
    @GET
    @Path("get-drug-names")
    public Page<DrugNameDict> getDrugNames(@QueryParam("drugCode")String drugCode, @QueryParam("inputCode")String inputCode,
                                           @QueryParam("perPage")int perPage,@QueryParam("currentPage")int currentPage,
                                           @QueryParam("drugId")String drugId){
        return drugNameFacade.getDrugNames(drugCode,inputCode,perPage,currentPage,drugId);
    }

    /**
     * 根据ID获取单一药品名称
     * @param nameId
     * @return
     */
    @GET
    @Path("get-drug-name")
    public DrugNameDict getDrugName(@QueryParam("nameId")String nameId) throws Exception{
        return drugNameFacade.getDrugName(nameId);
    }
}
