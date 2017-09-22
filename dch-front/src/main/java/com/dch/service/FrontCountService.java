package com.dch.service;

import com.dch.entity.*;
import com.dch.facade.FrontCountFacade;
import com.dch.vo.DrugCountryVo;
import com.dch.vo.DrugPackageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
@Path("front/count")
@Produces("application/json")
@Controller
public class FrontCountService {

    @Autowired
    private FrontCountFacade frontCountFacade;

    /**
     * 根据传入的药品生产厂商id查询该厂商生产的药品信息
     * @param firmId
     * @return
     */
    @GET
    @Path("get-firm-produce-drugs")
    public List<DrugBaseInfo> getDrugBaseInfosByFirmId(@QueryParam("firmId") String firmId){
        return frontCountFacade.getDrugBaseInfosByFirmId(firmId);
    }

    /**
     * 根据生产厂商获取药品包材信息表
     * @param firmId
     * @return
     */
    @GET
    @Path("get-firm-drug-packageVos")
    public List<DrugPackageVo> getDrugPackageVosByFirmId(@QueryParam("firmId") String firmId){
        return frontCountFacade.getDrugPackageVosByFirmId(firmId);
    }

    /**
     * 根据生产厂商获得药品广告信息
     * @param firmId
     * @return
     */
    @GET
    @Path("get-firm-drug-ads")
    public List<DrugAd> getDrugAdsByFirmId(@QueryParam("firmId") String firmId){
        return frontCountFacade.getDrugAdsByFirmId(firmId);
    }

    /**
     * 根据生产厂商获得药品专利信息
     * @param firmId
     * @return
     */
    @GET
    @Path("get-firm-drug-patents")
    public List<DrugPatent> getDrugPatentsByFirmId(@QueryParam("firmId") String firmId){
        return frontCountFacade.getDrugPatentsByFirmId(firmId);
    }

    /**
     * 根据传入的药品id查询药品别名信息
     * @param drugId
     * @return
     */
    @GET
    @Path("get-drug-name-dicts")
    public List<DrugNameDict> getDrugNameDictsByDrugId(@QueryParam("drugId") String drugId){
        return frontCountFacade.getDrugNameDictsByDrugId(drugId);
    }

    /**
     * 根据传入的药品id获取其生产厂商
     * @param drugId
     * @return
     */
    @GET
    @Path("get-drug-firms")
    public List<DrugFirm> getDrugFirmsByDrugId(@QueryParam("drugId") String drugId){
        return frontCountFacade.getDrugFirmsByDrugId(drugId);
    }

    /**
     * 根据药品查询此药进行广告的厂家列表
     * @param drugId
     * @return
     */
    @GET
    @Path("get-drug-ad-firms")
    public List<DrugFirm> getDrugAdFirmsByDrugId(@QueryParam("drugId") String drugId){
        return frontCountFacade.getDrugAdFirmsByDrugId(drugId);
    }

    /**
     * 获取所有的药品生产厂商
     * @return
     */
    @GET
    @Path("get-all-drug-firms")
    public List<DrugCountryVo> getAllDrugFirms(){
        return frontCountFacade.getAllDrugFirms();
    }

    /**
     * 获得不同国家所有的药品信息
     * @return
     */
    @GET
    @Path("get-all-drug-countryVos")
    public List<DrugCountryVo> getDrugCountryVos(){
        return frontCountFacade.getDrugCountryVos();
    }
}
