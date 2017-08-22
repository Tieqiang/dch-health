package com.dch.service;

import com.dch.entity.DrugFirm;
import com.dch.facade.DrugFirmFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 药品厂商维护
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Path("drug/drug-firm")
@Controller
public class DrugFirmService {

    @Autowired
    private DrugFirmFacade drugFirmFacade;

    /**
     * 添加、删除、修改药品厂商
     * @param drugFirm
     * @return
     */
    @POST
    @Path("merge-drug-firm")
    public Response mergeDrugFirm(DrugFirm drugFirm){
        return Response.status(Response.Status.OK).entity(drugFirmFacade.mergeDrugFirm(drugFirm)).build();
    }

    /**
     * 查询药品厂商 inputCode:不区分大小写的模糊匹配（选传）firmName:模糊匹配（选传）如果和inputCode同时传递，则取并集
     * @param inputCode
     * @param firmName
     * @return
     */
    @GET
    @Path("get-drug-firms")
    public List<DrugFirm> getDrugFirms(@QueryParam("inputCode")String inputCode,@QueryParam("firmName")String firmName){
        return drugFirmFacade.getDrugFirms(inputCode,firmName);
    }

    /**
     * 根据ID获取单独的药品厂商
     * @param firmId 厂商ID
     * @return
     */
    @GET
    @Path("get-drug-firm")
    public DrugFirm getDrugFirm(@QueryParam("firmId")String firmId) throws Exception{
        return drugFirmFacade.getDrugFirm(firmId);
    }
}
