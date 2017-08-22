package com.dch.service;

import com.dch.entity.DrugClass;
import com.dch.facade.DrugClassFacade;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 药品类别维护
 * Created by Administrator on 2017/8/22.
 */
@Produces("application/json")
@Path("drug/drug-class")
@Controller
public class DrugClassService {

    @Autowired
    private DrugClassFacade drugClassFacade;

    /**
     * 添加、删除、修改药品类别
     * @param drugClass
     * @return
     */
    @POST
    @Path("merge-drug-class")
    public Response mergeDrugclass(DrugClass drugClass) throws Exception{
        return Response.status(Response.Status.OK).entity(drugClassFacade.mergeDrugclass(drugClass)).build();
    }

    /**
     * 查询药品类别
     * @param className
     * @param parentId
     * @return
     */
    @GET
    @Path("get-drug-classes")
    public List<DrugClass> getDrugClasses(@QueryParam("className")String className,@QueryParam("parentId")String parentId){
        return drugClassFacade.getDrugClasses(className,parentId);
    }

    /**
     * 根据ID获取单一药品类别
     * @param classId
     * @return
     */
    @GET
    @Path("get-drug-class")
    public DrugClass getDrugClass(@QueryParam("classId")String classId) throws Exception{
        return drugClassFacade.getDrugClass(classId);
    }
}
