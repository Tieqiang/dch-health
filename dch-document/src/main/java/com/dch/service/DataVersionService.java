package com.dch.service;

import com.dch.entity.DataVersion;
import com.dch.facade.DataVersionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by lenovo on 2017/8/7.
 */

@Controller
@Path("doc/data-version")
@Produces("application/json")
public class DataVersionService {
    @Autowired
    private DataVersionFacade dataVersionFacade;

    /**
     * 获取某个体系下的元数据版本信息
     * @param standardId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-versions")
    public List<DataVersion> getDataVersions(@QueryParam("standardId") String standardId)throws Exception{
        return dataVersionFacade.getDataVersions(standardId);
    }

    /**
     * 添加、修改、删除元数据版本
     * @param dataVersion
     * @return
     */
    @POST
    @Path("merge-data-version")
    @Transactional
    public Response mergeDataVersion(DataVersion dataVersion){
        DataVersion merge = dataVersionFacade.merge(dataVersion);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取某一个元数据版本信息
     * @param versionId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-version")
    public DataVersion getDataVersion(@QueryParam("versionId") String versionId)throws Exception{
        return dataVersionFacade.getDataVersion(versionId);
    }

}
