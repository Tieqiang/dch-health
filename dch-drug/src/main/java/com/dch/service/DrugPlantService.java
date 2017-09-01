package com.dch.service;

import com.dch.facade.DrugPlantFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugPlantVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.List;

/**
 * 天然植物
 * Created by Administrator on 2017/8/22.
 */
@Controller
@Path("drug/drug-plant")
@Produces("application/json")
public class DrugPlantService {

    @Autowired
    private DrugPlantFacade drugPlantFacade ;


    /**
     * 查询药用植物
     * @param name
     * @return
     */
    @GET
    @Path("get-drug-plants")
    public Page<DrugPlantVo> getDrugPlants(@QueryParam("name") String name,
                                           @QueryParam("perPage") int perPage,
                                           @QueryParam("currentPage")int currentPage){
        return drugPlantFacade.getDrugPlants(name,perPage,currentPage) ;
    }


    /**
     * 获取具体的药用植物信息
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-plant")
    public DrugPlantVo getDrugPlant(@QueryParam("plantId")String id) throws Exception {
        return drugPlantFacade.getDrugPlant(id);
    }

    /**
     * 添加、删除、修改药用植物
     * @param drugPlantVo
     * @return
     */
    @POST
    @Path("merge-drug-plant")
    public DrugPlantVo mergeDrugPlant(DrugPlantVo drugPlantVo){
        return drugPlantFacade.mergeDrugPlant(drugPlantVo);
    }

}
