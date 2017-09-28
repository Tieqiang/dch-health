package com.dch.service;

import com.dch.entity.TemplateDataGroup;
import com.dch.facade.TemplateDataGroupFacade;
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
@Path("template/template-data-group")
public class TemplateDataGroupService {

    @Autowired
    private TemplateDataGroupFacade templateDataGroupFacade;

    /**
     *添加、删除、修改表单元数据
     * @param templateDataGroup
     * @return
     */
    @POST
    @Path("merge-template-data-group")
    public Response mergeTemplateDataGroup(TemplateDataGroup templateDataGroup) throws Exception{
        return templateDataGroupFacade.mergeTemplateDataGroup(templateDataGroup);
    }

    /**
     * 获取表单的元数据组
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-data-groups")
    public List<TemplateDataGroup> getTemplateGroups(@QueryParam("templateId")String templateId){
        return templateDataGroupFacade.getTemplateGroups(templateId);
    }

    /**
     * 获取表单元具体的元数据组
     * @param groupId
     * @return
     */
    @GET
    @Path("get-template-data-group")
    public TemplateDataGroup getTemplateGroup(@QueryParam("groupId")String groupId) throws Exception{
        return templateDataGroupFacade.getTemplateGroup(groupId);
    }
}
