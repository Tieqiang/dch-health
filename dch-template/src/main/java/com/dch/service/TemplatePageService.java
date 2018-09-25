package com.dch.service;

import com.dch.entity.TemplatePage;
import com.dch.facade.TemplatePageFacade;
import com.dch.util.StringUtils;
import com.dch.vo.TemplatePageAndResultVo;
import com.dch.vo.TemplatePageVo;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces("application/json")
@Path("template/template-page")
@Controller
public class TemplatePageService {

    @Autowired
    private TemplatePageFacade templatePageFacade;

    /**
     * 添加、删除、删除表单页
     * @param templatePageVo
     * @return
     */
    @Path("merge-template-page")
    @POST
    @Transactional
    public Response mergeTemplatePage(TemplatePageVo templatePageVo) throws Exception{
        TemplatePage templatePage = new TemplatePage();
        if(!StringUtils.isEmptyParam(templatePageVo.getId())){
            templatePage.setId(templatePageVo.getId());
        }
        if(!"-1".equals(templatePageVo.getStatus())){
            String onlySql = "select templatePageName from TemplatePage where status<>'-1' and templatePageName='"+templatePageVo.getTemplatePageName()+"'" +
                    " and id <>'"+templatePageVo.getId()+"' and templateId = '"+templatePageVo.getTemplateId()+"'";
            List<String> nameList = templatePageFacade.createQuery(String.class,onlySql,new ArrayList<Object>()).getResultList();
            if(nameList!=null && !nameList.isEmpty()){
                throw new Exception("表单名称已存在，请重新填写");
            }
        }
        templatePage.setTemplateId(templatePageVo.getTemplateId());
        templatePage.setTemplatePageContent(templatePageVo.getTemplatePageContent());
        templatePage.setTemplatePageName(templatePageVo.getTemplatePageName());
        templatePage.setTemplatePageOrder(templatePageVo.getTemplatePageOrder());
        String json = JSON.toString(templatePageVo.getTemplatePageDataModel());
        if("null".equals(json)){
            templatePage.setTemplatePageDataModel(null);
        }else{
            templatePage.setTemplatePageDataModel(json);
        }
        templatePage.setStatus(templatePageVo.getStatus());
        templatePage.setParentId(templatePageVo.getParentId());
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

    /**
     * 获取表单页面及表单值
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-pages-and-results")
    public List<TemplatePageAndResultVo> getTemplatePagesAndResults(@QueryParam("templateId") String templateId, @QueryParam("masterId")String masterId){
        return templatePageFacade.getTemplatePagesAndResults(templateId,masterId);
    }

    /**
     * 获取表单页面及表单值(只展示一个表单页面的值)
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-pages-and-first-result")
    public List<TemplatePageAndResultVo> getTemplatePagesAndFirstResult(@QueryParam("templateId") String templateId, @QueryParam("masterId")String masterId){
        return templatePageFacade.getTemplatePagesAndFirstResult(templateId,masterId);
    }

    /**
     * 根据表单id和表单页id获取该表单页的值
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-page-result")
    public List<TemplatePageAndResultVo> getTemplatePageResult(@QueryParam("templateId") String templateId,
                                                                @QueryParam("masterId")String masterId, @QueryParam("pageId")String pageId){
        return templatePageFacade.getTemplatePageResult(templateId,masterId,pageId);
    }
}
