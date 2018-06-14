package com.dch.service;

import com.dch.entity.FormReportCompare;
import com.dch.entity.TemplateReport;
import com.dch.facade.TemplateReportFacade;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.TemplateQueryRuleVo;
import com.dch.vo.TemplateReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/5/30.
 */
@Produces("application/json")
@Path("template/template-report")
@Controller
public class TemplateReportService {

    @Autowired
    private TemplateReportFacade templateReportFacade;

    @Path("merge")
    @POST
    @Transactional
    public Response mergeTemplateReport(TemplateReportVo templateReportVo){
        try{
            String userId = UserUtils.getCurrentUser().getId();
            if(!StringUtils.isEmptyParam(userId)){
                if(StringUtils.isEmptyParam(templateReportVo.getId())){//说明是新增
                    TemplateReport merge = templateReportFacade.saveTemplateReport(templateReportVo);
                    templateReportFacade.saveFormReportCompare(templateReportVo.getQueryRuleVoList(),merge.getId());
                    templateReportVo.setId(merge.getId());
                }
                if(!StringUtils.isEmptyParam(templateReportVo.getId()) && !"-1".equals(templateReportVo.getStatus())){
                    TemplateReport merge = templateReportFacade.saveTemplateReport(templateReportVo);
                    templateReportFacade.deleteFormReportCompare(merge.getId());
                    templateReportFacade.saveFormReportCompare(templateReportVo.getQueryRuleVoList(),merge.getId());
                }
                if("-1".equals(templateReportVo.getStatus())){//删除
                    templateReportFacade.deleteFormReportCompare(templateReportVo.getId());
                    templateReportFacade.removeById(templateReportVo.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(templateReportVo).build();
    }

    /**
     * 查询用户创建的报告
     * @param reportName
     * @return
     */
    @GET
    @Path("get-template-reports")
    public List<TemplateReport> getTemplateReports(@QueryParam("reportName")String reportName){
        List<TemplateReportVo> templateReportVos = new ArrayList<>();
        Map<String,List<TemplateQueryRuleVo>> qrMap = new HashMap<>();
        List<TemplateReport> templateReports = templateReportFacade.getTemplateReports(reportName);
        return templateReports;
    }

    @POST
    @Path("delete")
    @Transactional
    public Response deleteTemplateReport(@QueryParam("id")String id){
        TemplateReport templateReport = templateReportFacade.get(TemplateReport.class,id);
        if(templateReport!=null){
            templateReportFacade.deleteFormReportCompare(id);
            templateReportFacade.remove(templateReport);
        }
        return Response.status(Response.Status.OK).entity(templateReport).build();
    }

    /**
     * 查询用户创建的报告
     * @param id
     * @return
     */
    @GET
    @Path("get-template-reportvo")
    public Response getTemplateReportVo(@QueryParam("id")String id){
        TemplateReportVo templateReportVo = null;
        Map<String,List<TemplateQueryRuleVo>> qrMap = new HashMap<>();
        try{
            TemplateReport templateReport = templateReportFacade.get(TemplateReport.class,id);
//        List<TemplateReport> templateReports = templateReportFacade.getTemplateReports(reportName);
            List<String> templateReportIds = new ArrayList<>();
            templateReportIds.add(id);
//        for(TemplateReport templateReport:templateReports){
//            templateReportIds.add(templateReport.getId());
//        }
//        if(templateReportIds.isEmpty()){
//            return templateReportVos;
//        }
            qrMap = templateReportFacade.getTemplateQueryRuleVos(templateReportIds);
            //for(TemplateReport templateReport:templateReports){
            templateReportVo = new TemplateReportVo();
            templateReportVo.setId(templateReport.getId());
            templateReportVo.setMaker(templateReport.getMaker());
            templateReportVo.setReportName(templateReport.getReportName());
            templateReportVo.setStatus(templateReport.getStatus());
            templateReportVo.setQueryRuleVoList(qrMap.get(id));
            //templateReportVos.add(templateReportVo);
            //}
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(templateReportVo).build();
    }
}
