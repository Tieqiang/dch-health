package com.dch.service;

import com.dch.entity.DrugNaturalChemicalComposition;
import com.dch.facade.DrugNaturalChemicalCompositionFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Controller
@Path("drug/drug-chemical-composition")
public class DrugNaturalChemicalCompositionService {

    @Autowired
    private DrugNaturalChemicalCompositionFacade compositionFacade ;

    /**
     * 查询天然药物化学成分
     * @param name
     * @param wherehql
     * @return
     */
    @GET
    @Path("get-drug-chemical-compositions")
    public Page<DrugNaturalChemicalComposition> getDrugNaturalChemicalCompsitions(@QueryParam("name") String name,
                                                                                  @QueryParam("wherehql")String wherehql, @QueryParam("perPage") int perPage,
                                                                                  @QueryParam("currentPage")int currentPage){
        return compositionFacade.getDrugNaturalChemicalCompsitions(name,wherehql,perPage,currentPage);
    }

    /**
     * 获取具体的天然药物化学成分
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-chemical-composition")
    public DrugNaturalChemicalComposition getDrugNaturalChemicalCompsition(@QueryParam("id") String id) throws Exception {
        DrugNaturalChemicalComposition drugNaturalChemicalComposition = compositionFacade.get(DrugNaturalChemicalComposition.class, id);
        String status = drugNaturalChemicalComposition.getStatus();
        if(status==null||"".equals(status)){
            throw  new Exception("对象已经被删除");
        }
        return drugNaturalChemicalComposition;
    }


    @POST
    @Path("merge-drug-chemical-composition")
    public DrugNaturalChemicalComposition mergeDrugNaturalChemicalCompositon(DrugNaturalChemicalComposition drugNaturalChemicalComposition){
        return compositionFacade.mergeDrugNaturalChemicalCompositon(drugNaturalChemicalComposition);
    }

}
