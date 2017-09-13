package com.dch.service;

import com.dch.entity.DiseaseCategoryDict;
import com.dch.facade.DiseaseCategoryFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
@Produces("application/json")
@Path("disease/category")
@Controller
public class DiseaseCategoryService {

    @Autowired
    private DiseaseCategoryFacade diseaseCategoryFacade ;

    /**
     * 查询分类列表
     * @param categoryName 分类名称，进行模糊匹配
     * @param parentId     父分类ID，根据此字段查询子分类
     * @return
     */
    @GET
    @Path("get-disease-categorys")
    public List<DiseaseCategoryDict> getDiseaseCategorys(@QueryParam("categoryName") String categoryName,
                                                         @QueryParam("parentId") String parentId){
        return diseaseCategoryFacade.getDiseaseCategorys(categoryName,parentId);
    }

    /**
     * 添加、删除、修改疾病姿势和分类信息
     * @param diseaseCategoryDict
     * @return
     */
    @POST
    @Path("merge-disease-category")
    public Response mergeDieseaseCategory(DiseaseCategoryDict diseaseCategoryDict){
        DiseaseCategoryDict diseaseCategoryDict1 = diseaseCategoryFacade.mergeDiseaseCategory(diseaseCategoryDict);
        return Response.status(Response.Status.OK).entity(diseaseCategoryDict1).build();
    }

    /**
     * 根据ID获取所有的分类信息
     * @param categoryId
     * @return
     */
    @GET
    @Path("get-category")
    public DiseaseCategoryDict getCategory(@QueryParam("categoryId") String categoryId){
        DiseaseCategoryDict diseaseCategoryDict = diseaseCategoryFacade.get(DiseaseCategoryDict.class, categoryId);
        if(diseaseCategoryDict!=null&&!"-1".equals(diseaseCategoryDict.getStatus())){
            return diseaseCategoryDict;
        }else{
            return null ;
        }
    }
}
