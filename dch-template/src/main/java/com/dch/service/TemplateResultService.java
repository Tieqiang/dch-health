package com.dch.service;

import com.dch.entity.TemplateResult;
import com.dch.facade.TemplateResultFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.IDUtils;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateMasterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("template/template-result")
@Produces("application/json")
@Controller
public class TemplateResultService {

    @Autowired
    private TemplateResultFacade templateResultFacade;

    /**
     * 保存表单数据
     * @param templateResult
     * @return
     */
    @POST
    @Path("merge-template-result")
    @Transactional
    public Response mergeTemplateResult(TemplateResult templateResult){
        if(StringUtils.isEmptyParam(templateResult.getId())){
            if(StringUtils.isEmptyParam(templateResult.getDocId())){
                templateResult.setDocId(IDUtils.getDocId());
            }
        }
        return Response.status(Response.Status.OK).entity(templateResultFacade.merge(templateResult)).build();
    }
    /**
     * 获取表单结果
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-mastervos")
    public Page<TemplateMasterVo> getTemplateMasterVos(@QueryParam("projectId") String projectId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateMasterVos(projectId,perPage,currentPage);

    }

    /**
     * 获取某一具体表单结果
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-results")
    public Page<TemplateResult> getTemplateResults(@QueryParam("templateId") String templateId,@QueryParam("docId")String docId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateResults(templateId,docId,perPage,currentPage);
    }

}
