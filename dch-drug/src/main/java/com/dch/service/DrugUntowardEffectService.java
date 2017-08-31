package com.dch.service;

import com.dch.entity.DrugUntowardEffect;
import com.dch.facade.DrugUntowardEffectFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drug/drug-untoward-effect")
@Produces("application/json")
@Controller
public class DrugUntowardEffectService {
    @Autowired
    private DrugUntowardEffectFacade drugUntowardEffectFacade;

    /**
     * 添加、删除、修改不良反应信息
     * @param drugUntowardEffect
     * @return
     */
    @POST
    @Path("merge-drug-untoward-effect")
    @Transactional
    public Response mergeDrugUntowardEffect(DrugUntowardEffect drugUntowardEffect){
        return drugUntowardEffectFacade.mergeDrugUntowardEffect(drugUntowardEffect);
    }

    /**
     * 获取药品不良反应信息
     * @param drugId
     * @return
     */
    @GET
    @Path("get-drug-untoward-effects")
    public Page<DrugUntowardEffect> getDrugUntowardEffects(@QueryParam("drugId") String drugId,
                                                           @QueryParam("perPage") int perPage,
                                                           @QueryParam("currentPage") int currentPage){
        return drugUntowardEffectFacade.getDrugUntowardEffects(drugId,perPage,currentPage);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-untoward-effect")
    public DrugUntowardEffect getDrugUntowardEffect(@QueryParam("id") String id) throws Exception {
        return drugUntowardEffectFacade.getDrugUntowardEffect(id);
    }

}
