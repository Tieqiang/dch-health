package com.dch.service;

import com.dch.entity.DrugBaseInfo;
import com.dch.facade.DrugBaseInfoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * 药物基本信息维护
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Path("drug/drug-base-info")
@Controller
public class DrugBaseInfoService {

    @Autowired
    private DrugBaseInfoFacade drugBaseInfoFacade;

    /**
     * 添加、删除、修改药品基本信息
     * @param drugBaseInfo
     * @return
     */
    @POST
    @Path("merge-drug-base-info")
    public Response mergeDrugBaseInfo(DrugBaseInfo drugBaseInfo) throws Exception{
        return Response.status(Response.Status.OK).entity(drugBaseInfoFacade.mergeDrugBaseInfo(drugBaseInfo)).build();
    }
}
