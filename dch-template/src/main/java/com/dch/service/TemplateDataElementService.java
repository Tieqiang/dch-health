package com.dch.service;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataValue;
import com.dch.facade.TemplateDataElementFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateDataElementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
    public Response mergeTemplateDataElement(TemplateDataElement templateDataElement) throws Exception{
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
    public Page<TemplateDataElementVo> getTemplateDataElements(@QueryParam("groupId") String groupId, @QueryParam("dataElementName")String dataElementName,
                                                               @QueryParam("templateId") String templateId,
                                                               @QueryParam("perPage") int perPage,
                                                               @QueryParam("currentPage") int currentPage){
        return templateDataElementFacade.getTemplateDataElements(groupId,dataElementName,templateId,perPage,currentPage);
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

    /**
     * 根据传入的元数据值域集合进行保存
     * @param templateDataValues
     * @return
     */
    @POST
    @Path("merge-data-values")
    public Response mergeTemplateDataValues(List<TemplateDataValue> templateDataValues) throws Exception{
        return templateDataElementFacade.mergeTemplateDataValues(templateDataValues);
    }

    /**
     * 根据表单元数据id,元数据组名和元数据名称生成元数据编码
     * @param dataElementId
     * @param
     * @param dataName
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-code-by-dataName")
    public List<String> getDataCodeByDataName(@QueryParam("parentDataId")String parentDataId,@QueryParam("parentCode")String parentCode,
                                              @QueryParam("dataElementId")String dataElementId,@QueryParam("dataName")String dataName) throws Exception{
        if(StringUtils.isEmptyParam(dataName)){
            throw new Exception("表单元数据名称不能为空");
        }
        if(StringUtils.isEmptyParam(dataElementId)){
            dataElementId = "";
        }
        String realCode = templateDataElementFacade.getRealCode(parentDataId,parentCode,dataElementId,dataName);
        List<String> list = new ArrayList<String>();
        list.add(realCode);
        return list;
    }
}
