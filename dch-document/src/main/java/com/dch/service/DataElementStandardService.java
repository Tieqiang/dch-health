package com.dch.service;

import com.dch.entity.DataElementStandard;
import com.dch.facade.DataElementStandardFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Produces("application/json")
@Controller
@Path("doc/data-element-standard")
public class DataElementStandardService {

    @Autowired
    private DataElementStandardFacade dataElementStandardFacade  ;


    /**
     * 获取元数据科研体系
     * @return
     */
    @GET
    @Path("get-data-element-standards")
    public List<DataElementStandard> getDataElementStandards(){
        return dataElementStandardFacade.getDataElementStandards();
    }

    /**
     * 添加、修改、删除元数据体系
     * @param dataElementStandard
     * @return
     */
    @POST
    @Path("merge-data-element-standard")
    @Transactional
    public Response mergeDataElementStandards(DataElementStandard dataElementStandard){
        DataElementStandard merge = dataElementStandardFacade.merge(dataElementStandard);
        return Response.status(Response.Status.OK).entity(merge).build();
    }



}
