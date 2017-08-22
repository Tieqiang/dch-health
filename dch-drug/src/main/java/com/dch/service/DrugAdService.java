package com.dch.service;

import com.dch.entity.DrugAd;
import com.dch.facade.DrugAdFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("drug/drug-ad")
@Produces("application/json")
public class DrugAdService {

    @Autowired
    private DrugAdFacade drugAdFacade;

    /**
     * 添加、删除、修改药品品广告
     * @param drugAd
     * @return
     */
    @POST
    @Path("merge-drug-ad")
    @Transactional
    public Response mergeDrugAd(DrugAd drugAd){
        return drugAdFacade.mergeDrugAd(drugAd);
    }

    /**
     * 获取药品广告信息
     * @param drugId
     * @param drugCode
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-ads")
    public List<DrugAd> getDrugAds(@QueryParam("drugId") String drugId,
                                   @QueryParam("drugCode") String drugCode)throws Exception{
        return drugAdFacade.getDrugAds(drugId,drugCode);
    }

    /**
     * 获取单一药品广告
     * @param adId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-ad")
    public DrugAd getDrugAd(@QueryParam("adId") String adId)throws Exception{
        return drugAdFacade.getDrugAd(adId);
    }

}
