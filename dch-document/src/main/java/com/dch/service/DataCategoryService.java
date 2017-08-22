package com.dch.service;

import com.dch.entity.DataElementCategory;
import com.dch.facade.DataCategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by lenovo on 2017/8/8.
 */
@Controller
@Path("doc/data-category")
@Produces("application/json")
public class DataCategoryService {

    @Autowired
    private DataCategoryFacade dataCategoryFacade;

    /**
     * 获取某个体系下的元数据或者值域目录
     * @param standardId
     * @param categoryType
     * @return
     */
    @GET
    @Path("get-data-categorys")
    public List<DataElementCategory> getDataCategorys(@QueryParam("standardId") String standardId, @QueryParam("categoryType") String categoryType)throws Exception{
        return dataCategoryFacade.getDataCategorys(standardId,categoryType);
    }

    /**
     * 根据ID获取元数据或者值域分类
     * @param categoryId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-category")
    public DataElementCategory getDataCategory(@QueryParam("categoryId")String categoryId)throws Exception{
        return dataCategoryFacade.getDataCategory(categoryId);
    }

    /**
     * 添加、删除、修改元数据值域目录
     * @param dataElementCategory
     * @return
     */
    @POST
    @Path("merge-data-category")
    @Transactional
    public Response mergeDataCategory(DataElementCategory dataElementCategory){
        DataElementCategory merge = dataCategoryFacade.merge(dataElementCategory);
        return Response.status(Response.Status.OK).entity(merge).build();
    }







}
