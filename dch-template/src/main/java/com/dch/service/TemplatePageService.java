package com.dch.service;

import com.dch.entity.TemplatePage;
import com.dch.facade.TemplatePageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces("application/json")
@Path("template/template-page")
@Controller
public class TemplatePageService {

    @Autowired
    private TemplatePageFacade templatePageFacade;

    /**
     * 添加、删除、删除表单页
     * @param templatePage
     * @return
     */
    @Path("merge-template-page")
    @POST
    @Transactional
    public Response mergeTemplatePage(TemplatePage templatePage){
        return templatePageFacade.mergeTemplatePage(templatePage);
    }
    /**
     * 获取表单页面
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-pages")
    public List<TemplatePage> getTemplatePages(@QueryParam("templateId") String templateId){
        return templatePageFacade.getTemplatePages(templateId);
    }

    /**
     * 获取具体的表单页
     * @param pageId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-template-page")
    public TemplatePage getTemplatePage(@QueryParam("pageId") String pageId) throws Exception {
        return templatePageFacade.getTemplatePage(pageId);
    }
}
