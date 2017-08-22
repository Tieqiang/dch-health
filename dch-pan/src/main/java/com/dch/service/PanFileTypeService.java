package com.dch.service;

import com.dch.entity.PanFileType;
import com.dch.facade.PanFileTypeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
@Produces("application/json")
@Controller
@Path("pan/file-type")
public class PanFileTypeService {
    @Autowired
    private PanFileTypeFacade panFileTypeFacade ;

    /**
     * 添加、修改、删除文件类型
     * @param panFileType
     * @return
     * @throws Exception
     */
    @POST
    @Transactional
    @Path("merge-file-type")
    public Response mergePanFileType(PanFileType panFileType)throws Exception{
        return panFileTypeFacade.mergePanFileType(panFileType);
    }
    /**
     * 获取文件类型
     * @param id
     * @param typeName
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-file-type")
    public PanFileType getPanFileType(@QueryParam("id") String id,@QueryParam("typeName")String typeName)throws Exception{
        return panFileTypeFacade.getPanFileType(id,typeName);
    }
    /**
     * 获取所有文件类型
     * @param typeName
     * @return
     */
    @GET
    @Path("get-file-types")
    public List<PanFileType> getPanFileTypes(@QueryParam("typeName") String typeName){
        return panFileTypeFacade.getPanFileTypes(typeName);
    }



}
