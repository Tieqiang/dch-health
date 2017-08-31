package com.dch.service;

import com.dch.entity.DrugPackage;
import com.dch.facade.DrugPackageFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 药品包材维护
 * Created by Administrator on 2017/8/23.
 */
@Produces("application/json")
@Path("drug/drug-package")
@Controller
public class DrugPackageService {

    @Autowired
    private DrugPackageFacade drugPackageFacade;

    /**
     * 添加、删除、修改药品包材
     * @param drugPackage
     * @return
     */
    @POST
    @Path("merge-drug-package")
    public Response mergeDrugPackage(DrugPackage drugPackage){
        return Response.status(Response.Status.OK).entity(drugPackageFacade.mergeDrugPackage(drugPackage)).build();
    }

    /**
     * 获取药品包材信息
     * @param packageName
     * @param wherehql
     * @return
     */
    @GET
    @Path("get-drug-packages")
    public Page<DrugPackage> getDrugPackages(@QueryParam("packageName")String packageName, @QueryParam("wherehql")String wherehql,
                                             @QueryParam("perPage") int perPage,
                                             @QueryParam("currentPage") int currentPage){
        return drugPackageFacade.getDrugPackages(packageName,wherehql,perPage,currentPage);
    }

    /**
     * 获取单一的包材信息
     * @param packageId
     * @return
     */
    @GET
    @Path("get-drug-package")
    public DrugPackage getDrugPackage(@QueryParam("packageId")String packageId) throws Exception{
        return drugPackageFacade.getDrugPackage(packageId);
    }
}
