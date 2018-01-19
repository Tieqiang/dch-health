package com.dch.service;

import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.facade.TemplateResultFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateMasterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    public Response mergeTemplateResult(TemplateResult templateResult) throws Exception {

        //如果是保存的第一个页面，则自动生成
        if(StringUtils.isEmptyParam(templateResult.getId())){
            if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                String templateId = templateResult.getTemplateId();
                TemplateMaster templateMaster = templateResultFacade.get(TemplateMaster.class, templateId);
                TemplateResultMaster master = new TemplateResultMaster();
                master.setTemplateId(templateId);
                master.setTemplateName(templateMaster.getTemplateName());
                master.setStatus("1");
                TemplateResultMaster merge = templateResultFacade.merge(master);
                templateResult.setMasterId(merge.getId());
            }
        }else{
            if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                throw new Exception("masterId不能为空");
            }
        }
        TemplateResult templateResultMerge = templateResultFacade.merge(templateResult);
        String totalHql = "select count(*) from TemplatePage where status<>'-1' and templatePageContent is not null and templateId = '"+templateResultMerge.getTemplateId()+"'";
        Long total = templateResultFacade.createQuery(Long.class,totalHql,new ArrayList<Object>()).getSingleResult();
        String doneHql = "select count(*) from TemplateResult where status<>'-1' and templateResult is not null and templateId = '"+templateResultMerge.getTemplateId()+"'";
        Long doneNum = templateResultFacade.createQuery(Long.class,doneHql,new ArrayList<Object>()).getSingleResult();
        doneNum = doneNum+1;
        String completeRate = "0.0";
        if(total>0){
            DecimalFormat df = new DecimalFormat("#.00");
            completeRate =df.format(Double.valueOf(doneNum/total));
            String tmHql = "update TemplateResultMaster set completeRate = "+completeRate+" where templateId = '"+templateResultMerge.getTemplateId()+"'";
            templateResultFacade.excHql(tmHql);
        }
        return Response.status(Response.Status.OK).entity(templateResultMerge).build();
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
    public Page<TemplateResultMaster> getTemplateResults(@QueryParam("templateId") String templateId,@QueryParam("masterId")String masterId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateResults(templateId,masterId,perPage,currentPage);
    }


    /**
     * 获取某个人填写的表单项目
     * @param templateId 模板的ID
     * @param perPage    单页显示数量
     * @param userId     用户的ID，不传则表示获取这个表单的所有录入的数据
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-template-result-masters")
    public Page<TemplateResultMaster> getTemplateResultMasters(@QueryParam("templateId")String templateId,@QueryParam("perPage")int perPage,
                                                              @QueryParam("userId")String userId,@QueryParam("currentPage")int currentPage){
        return  templateResultFacade.getTemplateResultMasters(templateId,perPage,currentPage,userId);
    }

}
