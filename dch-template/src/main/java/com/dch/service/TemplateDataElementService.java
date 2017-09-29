package com.dch.service;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataValue;
import com.dch.facade.TemplateDataElementFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces("application/json")
@Path("template/template-data-element")
@Controller
public class TemplateDataElementService {
    @Autowired
    private TemplateDataElementFacade templateDataElementFacade;

    /**
     * 添加、删除、修改元数据
     * @param templateDataElement
     * @return
     */
    @Path("merge-template-data-element")
    @POST
    @Transactional
    public Response mergeTemplateDataElement(TemplateDataElement templateDataElement){
        return templateDataElementFacade.mergeTemplateDataElement(templateDataElement);
    }

    /**
     * 获取元数据
     * @param groupId
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-data-elements")
    public Page<TemplateDataElement> getTemplateDataElements(@QueryParam("groupId") String groupId,
                                                             @QueryParam("templateId") String templateId,
                                                             @QueryParam("perPage") int perPage,
                                                             @QueryParam("currentPage") int currentPage){
        return templateDataElementFacade.getTemplateDataElements(groupId,templateId,perPage,currentPage);
    }

    /**
     * 添加、修改、删除元数据值域
     * @param templateDataValue
     * @return
     */
    @Path("merge-template-data-value")
    @POST
    @Transactional
    public Response mergeTemplateDataValue(TemplateDataValue templateDataValue){
        return templateDataElementFacade.mergeTemplateDataValue(templateDataValue);
    }

    /**
     * 获取元数据的值域
     * @param elementId
     * @return
     */
    @GET
    @Path("get-template-data-values")
    public List<TemplateDataValue> getTemplateDataValues(@QueryParam("elementId") String elementId){
        return templateDataElementFacade.getTemplateDataValues(elementId);
    }

}
