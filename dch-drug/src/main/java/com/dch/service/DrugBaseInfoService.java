package com.dch.service;

import com.dch.entity.DrugBaseInfo;
import com.dch.entity.DrugNameDict;
import com.dch.facade.DrugBaseInfoFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

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
        return drugBaseInfoFacade.mergeDrugBaseInfo(drugBaseInfo);
    }

    /**
     * 获取药品基本信息
     * classId:药品类别（选传）perPage:每页显示数量（选传）currentPage：当前页（选传）wherehql:自己拼装的条件（选传）
     * inputCode:不区分大小写的模糊匹配（选传）
     * drugName:模糊匹配（选传）如果和inputCode同时传递，则取并集;另外drugName和可以是商品名和其他任何一种名称
     * @param classId
     * @param perPage
     * @param currentPage
     * @param wherehql
     * @param inputCode
     * @param drugName
     * @return
     */
    @GET
    @Path("get-drug-base-infos")
    public Page<DrugBaseInfo> getDrugBaseInfos(@QueryParam("classId")String classId, @QueryParam("perPage")int perPage, @QueryParam("currentPage") int currentPage,
                                               @QueryParam("wherehql")String wherehql, @QueryParam("inputCode")String inputCode,
                                               @QueryParam("drugName")String drugName){
        return drugBaseInfoFacade.getDrugBaseInfos(classId,perPage,currentPage,wherehql,inputCode,drugName);
    }

    /**
     * 获取药品基名称
     * classId:药品类别（选传）perPage:每页显示数量（选传）currentPage：当前页（选传）wherehql:自己拼装的条件（选传）
     * inputCode:不区分大小写的模糊匹配（选传）
     * drugName:模糊匹配（选传）如果和inputCode同时传递，则取并集;另外drugName和可以是商品名和其他任何一种名称
     * @param classId
     * @param perPage
     * @param currentPage
     * @param wherehql
     * @param inputCode
     * @param drugName
     * @return
     */
    @GET
    @Path("get-drug-name")
    public Page<DrugNameDict> getDrugName(@QueryParam("classId")String classId, @QueryParam("perPage")int perPage, @QueryParam("currentPage") int currentPage,
                                          @QueryParam("wherehql")String wherehql, @QueryParam("inputCode")String inputCode,
                                          @QueryParam("drugName")String drugName){
        return drugBaseInfoFacade.getDrugName(classId,perPage,currentPage,wherehql,inputCode,drugName);
    }

    /**
     * 根据ID获取单一药品基本信息
     * baseInfoId和drugCode两者选传其中一个或者同时传递，如果都不传需报异常错误
     * @param baseInfoId
     * @param drugCode
     * @return
     */
    @GET
    @Path("get-drug-base-info")
    public DrugBaseInfo getDrugBaseInfo(@QueryParam("baseInfoId") String baseInfoId,@QueryParam("drugCode")String drugCode) throws Exception{
        return drugBaseInfoFacade.getDrugBaseInfo(baseInfoId,drugCode);
    }
}
