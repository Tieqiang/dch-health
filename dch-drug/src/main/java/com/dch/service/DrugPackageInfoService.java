package com.dch.service;

import com.dch.entity.DrugPackageInfo;
import com.dch.facade.DrugPackageInfoFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Path("drug/drug-package-info")
@Controller
public class DrugPackageInfoService {

    @Autowired
    private DrugPackageInfoFacade drugPackageInfoFacade;

    /**
     * 添加、删除、修改药品规格信息
     * @param drugPackageInfo
     * @return
     */
    @POST
    @Path("merge-drug-package-info")
    public Response mergeDrugPackage(DrugPackageInfo drugPackageInfo) throws Exception{
        return Response.status(Response.Status.OK).entity(drugPackageInfoFacade.mergeDrugPackage(drugPackageInfo)).build();
    }

    /**
     * 获取药品规格信息
     * @param drugId 药品信息id
     * @return
     */
    @GET
    @Path("get-drug-package-infos")
    public Page<DrugPackageInfo> getDrugPackInfos(@QueryParam("drugId") String drugId,
                                                  @QueryParam("perPage") int perPage,
                                                  @QueryParam("currentPage") int currentPage){
        return drugPackageInfoFacade.getDrugPackInfos(drugId,perPage,currentPage);
    }

    /**
     * 根据ID获取单一的药品规格信息
     * @param packageInfoId
     * @return
     */
    @GET
    @Path("get-drug-package-info")
    public DrugPackageInfo getDrugPackageInfo(@QueryParam("packageInfoId") String packageInfoId) throws Exception{
        return drugPackageInfoFacade.getDrugPackageInfo(packageInfoId);
    }
}
