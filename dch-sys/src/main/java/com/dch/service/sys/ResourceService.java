package com.dch.service.sys;


import com.dch.entity.ResourceDict;
import com.dch.entity.ResourceOperation;
import com.dch.facade.ResourceFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */
@Controller
@Path("sys/resource")
@Produces("application/json")
public class ResourceService {

    @Autowired
    private ResourceFacade resourceFacade ;

    /**
     * 修改删除一个操作
     * @param resourceDict
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeResource(ResourceDict resourceDict){
        return Response.status(Response.Status.OK).entity(resourceFacade.merge(resourceDict)).build();
    }


    /**
     * 获取所有模块的操作
     * @param moduleId
     * @return
     */
    @GET
    @Path("get-resources")
    public List<ResourceDict> getResources(@QueryParam("moduleId") String moduleId){
        return resourceFacade.getResources(moduleId);
    }

    /**
     * 获取所有模块的操作
     * @param functionId
     * @return
     */
    @GET
    @Path("get-resource")
    public ResourceDict getResource(@QueryParam("resourceId") String functionId){
        return resourceFacade.get(ResourceDict.class,functionId);
    }

    /**
     * 获取某个资源的所有操作
     * @param functionId
     * @return
     */
    @GET
    @Path("get-resource-operations")
    public List<ResourceOperation> getResourceOperations(@QueryParam("resourceDictId") String functionId){
        return resourceFacade.getResourceOperations(functionId);
    }

    /**
     * 修改操作对象
     * @param resourceOperation
     * @return
     */
    @POST
    @Path("merge-resource-operation")
    @Transactional
    public Response mergeResourceOperation(ResourceOperation resourceOperation){
        return Response.status(Response.Status.OK).entity(resourceFacade.merge(resourceOperation)).build();
    }

    /**
     *根据模块id获取模块下的操作权限
     * @param moduleId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-resource-operations-by-moduleId")
    public List<ResourceOperation> getResourceOperationsByModuleId(@QueryParam("moduleId") String moduleId) throws Exception{
        if(StringUtils.isEmptyParam(moduleId)){
            throw new Exception("模块信息不能为空");
        }
        return resourceFacade.getResourceOperationsByModuleId(moduleId);
    }
}
