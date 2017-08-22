package com.dch.service;

import com.dch.entity.DrugAdministrationProtect;
import com.dch.facade.DrugAdministrationProtectFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("drug/drug-administration-protect")
@Produces("application/json")
public class DrugAdministrationProtectService {
    @Autowired
    private DrugAdministrationProtectFacade drugAdministrationProtectFacade;

    /**
     * 添加、删除、修改药品行政政策
     * @param drugAdministrationProtect
     * @return
     */
    @Path("merge-drug-administration-protect")
    @POST
    @Transactional
    public Response mergeAdministrationProtect(DrugAdministrationProtect drugAdministrationProtect) {

        return drugAdministrationProtectFacade.mergeAdministrationProtect(drugAdministrationProtect);
    }

    /**
     * 获取药品行政曾策保护
     * @param drugId
     * @return
     */
    @GET
    @Path("get-protects")
    public List<DrugAdministrationProtect> getAdministrationProtects(@QueryParam("drugId") String drugId){
        return drugAdministrationProtectFacade.getAdministrationProtects(drugId);

    }

    /**
     * 获取单一的药品行政政策保护
     * @param protectId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-protect")
    public DrugAdministrationProtect getAdministrationProtect(@QueryParam("protectId") String protectId) throws Exception {
        return drugAdministrationProtectFacade.getAdministrationProtect(protectId);
    }

}
