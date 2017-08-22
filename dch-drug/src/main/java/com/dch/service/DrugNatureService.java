package com.dch.service;

import com.dch.entity.DrugNaturalActive;
import com.dch.facade.DrugNatureFacade;
import com.dch.vo.DrugNatureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Controller
@Path("drug/drug-nature")
@Produces("application/json")
public class DrugNatureService {



    @Autowired
    private DrugNatureFacade drugNatureFacade ;

    /**
     * 获取天然药物属性
     * @param name      名称
     * @param wherehql  前台传入的参数判断
     * @return
     */
    @GET
    @Path("get-drug-natures")
    public List<DrugNatureVo> getDrugNatures(@QueryParam("name") String name,@QueryParam("wherehql") String wherehql){
        return drugNatureFacade.getDrugNatures(name,wherehql);
    }

    /**
     * 获取单个天然药物活性
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-nature")
    public DrugNatureVo getDrugNature(@QueryParam("id") String id) throws Exception {
        DrugNatureVo drugNature = drugNatureFacade.getDrugNature(id);
        return drugNature;
    }


    /**
     * 添加、删除、修改天然药物活性
     * @param drugNatureVo
     * @return
     */
    @Path("merge-drug-nature")
    @POST
    public DrugNatureVo mergeDrugNature(DrugNatureVo drugNatureVo){
        return drugNatureFacade.mergeDrugNature(drugNatureVo);
    }

}
