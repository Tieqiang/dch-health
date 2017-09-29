package com.dch.service;

import com.dch.entity.TemplateMaster;
import com.dch.facade.TemplateMasterFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Controller
@Produces("application/json")
@Path("template/template-master")
public class TemplateMasterService {

    @Autowired
    private TemplateMasterFacade templateMasterFacade;

    /**
     * 添加、删除、修改表单名称
     * @param templateMaster
     * @return
     */
    @POST
    @Path("merge-template-master")
    public Response mergeTemplateMaster(TemplateMaster templateMaster) throws Exception{
        return templateMasterFacade.mergeTemplateMaster(templateMaster);
    }
    /**
     *获取表单
     * @param projectId 项目Id（选传）
     * @param templateLevel 表单级别（选传）
     * @param templateStauts 表单状态（注意区分stauts)
     * @param whereHql 前端拼接条件
     * @param perPage 每页显示条数，默认15条
     * @param currentPage 当前第几页，默认为1
     * @return
     */
    @GET
    @Path("get-template-masters")
    public Page<TemplateMaster> getTemplateMasters(@QueryParam("projectId")String projectId,@QueryParam("templateLevel")String templateLevel,
                                                   @QueryParam("templateStauts")String templateStauts,@QueryParam("whereHql")String whereHql,
                                                   @QueryParam("perPage") int perPage,@QueryParam("currentPage")int currentPage){
        return templateMasterFacade.getTemplateMasters(projectId,templateLevel,templateStauts,whereHql,perPage,currentPage);
    }

    /**
     * 获取具体的表单
     * @param templateMasterId 表单Id
     * @return
     */
    @GET
    @Path("get-template-master")
    public TemplateMaster getTemplateMaster(@QueryParam("templateMasterId")String templateMasterId) throws Exception{
        return templateMasterFacade.getTemplateMaster(templateMasterId);
    }

    /**
     * 根据用户id获取所属表单
     * @param userId
     * @return
     */
    @GET
    @Path("get-template-masters-By-creater")
    public List<TemplateMaster> getTemplateMastersByCreater(@QueryParam("userId")String userId){
        return templateMasterFacade.getTemplateMastersByCreater(userId);
    }
}
