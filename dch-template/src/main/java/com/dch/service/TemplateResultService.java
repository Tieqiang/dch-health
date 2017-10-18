package com.dch.service;

import com.dch.entity.TemplateResult;
import com.dch.facade.TemplateResultFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.TemplateMasterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("template/template-result")
@Produces("application/json")
@Controller
public class TemplateResultService {

    @Autowired
    private TemplateResultFacade templateResultFacade;

    /**
     * 获取表单结果
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-results")
    public Page<TemplateMasterVo> getTemplateResults(@QueryParam("projectId") String projectId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateResults(projectId,perPage,currentPage);

    }

    /**
     * 获取某一具体表单结果
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-result")
    public Page<TemplateResult> getTemplateResult(@QueryParam("templateId") String templateId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateResult(templateId,perPage,currentPage);
    }

}
