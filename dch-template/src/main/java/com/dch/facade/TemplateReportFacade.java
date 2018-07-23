package com.dch.facade;

import com.dch.entity.FormReportCompare;
import com.dch.entity.TemplateReport;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.TemplateQueryRuleVo;
import com.dch.vo.TemplateReportVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/5/30.
 */
@Component
public class TemplateReportFacade extends BaseFacade{
    /**
     * 保存报告关联报表
     * @param queryRuleVoList
     * @param reportId
     */
    @Transactional
    public void saveFormReportCompare(List<TemplateQueryRuleVo> queryRuleVoList,String reportId) {
        if(queryRuleVoList!=null && !queryRuleVoList.isEmpty()){
            List<FormReportCompare> formReportCompareList = new ArrayList<>();
            for(TemplateQueryRuleVo templateQueryRuleVo:queryRuleVoList){
                FormReportCompare formReportCompare = new FormReportCompare();
                formReportCompare.setMark(templateQueryRuleVo.getMark());
                formReportCompare.setRelatedTemplateQueryRuleId(templateQueryRuleVo.getId());
                formReportCompare.setRelatedTemplateReportId(reportId);
                merge(formReportCompare);
                formReportCompareList.add(formReportCompare);
            }
            //batchInsert(formReportCompareList);
            //formReportCompareList = null;
        }
    }

    /**
     * 保存报告
     * @param templateReportVo
     * @return
     */
    @Transactional
    public TemplateReport saveTemplateReport(TemplateReportVo templateReportVo) {
        TemplateReport templateReport = new TemplateReport();
        if(!StringUtils.isEmptyParam(templateReportVo.getId())){
            templateReport.setId(templateReportVo.getId());
        }
        templateReport.setTemplateId(templateReportVo.getTemplateId());
        templateReport.setMaker(templateReportVo.getMaker());
        templateReport.setReportName(templateReportVo.getReportName());
        templateReport.setStatus("1");
        return merge(templateReport);
    }

    /**
     * 根据报告id删除报告下的报表
     * @param id
     */
    @Transactional
    public void deleteFormReportCompare(String id) {
        String delHql = "delete from FormReportCompare where relatedTemplateReportId = '"+id+"'";
        excHql(delHql);
    }

    /**
     * 删除报告
     * @param id
     */
    @Transactional
    public void removeById(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        removeByStringIds(TemplateReport.class,ids);
    }

    /**
     * 根据报告名称获取报告  模糊匹配
     * @param reportName
     * @return
     */
    public List<TemplateReport> getTemplateReports(String reportName) {
        List<TemplateReport> templateReports = null;
        try {
            String userId = UserUtils.getCurrentUser().getId();
            String hql = " from TemplateReport where status<>'-1' and createBy = '"+userId+"'";
            if(!StringUtils.isEmptyParam(reportName)){
                hql += " and reportName like '%"+reportName+"%'";
            }
            templateReports = createQuery(TemplateReport.class,hql,new ArrayList<Object>()).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return templateReports;
    }

    public Map<String,List<TemplateQueryRuleVo>> getTemplateQueryRuleVos(List<String> templateReportIds) {
        Map<String,List<TemplateQueryRuleVo>> returnMap = new HashMap<>();
//        Map<String,Map> formReMap = new HashMap<>();
        String ids = StringUtils.getQueryIdsString(templateReportIds);
//        String hqlComp = " from FormReportCompare where relatedTemplateReportId in ("+ids+")";
//        List<FormReportCompare> formReportCompares = createQuery(FormReportCompare.class,hqlComp,new ArrayList<Object>()).getResultList();
//        if(formReportCompares!=null && !formReportCompares.isEmpty()){
//            for(FormReportCompare formReportCompare:formReportCompares){
//                if(formReMap.containsKey(formReportCompare.getRelatedTemplateReportId())){
//                    Map inMap = formReMap.get(formReportCompare.getRelatedTemplateReportId());
//                    inMap.put(formReportCompare.getRelatedTemplateQueryRuleId(),formReportCompare.getMark());
//                }else{
//                    Map inMap = new HashMap();
//                    inMap.put(formReportCompare.getRelatedTemplateQueryRuleId(),formReportCompare.getMark());
//                    formReMap.put(formReportCompare.getRelatedTemplateReportId(),inMap);
//                }
//            }
//        }
        String hqlQrule = "select new com.dch.vo.TemplateQueryRuleVo(q.id,f.mark) from FormReportCompare f,TemplateQueryRule q" +
                " where f.relatedTemplateQueryRuleId = q.id and q.status<>'-1' and f.relatedTemplateReportId in ("+ids+")";
        List<TemplateQueryRuleVo> templateQueryRuleVos = createQuery(TemplateQueryRuleVo.class,hqlQrule,new ArrayList<Object>()).getResultList();
//        for(String key:formReMap.keySet()){
//            List<TemplateQueryRuleVo> templateQueryRuleVos1 = getTemplateQueryRuleVoByFormMap(templateQueryRuleVos,formReMap.get(key));
//            returnMap.put(key,templateQueryRuleVos1);
//        }
//        formReMap = null;
        returnMap.put(templateReportIds.get(0),templateQueryRuleVos);
        return returnMap;
    }

    public List<TemplateQueryRuleVo> getTemplateQueryRuleVoByFormMap(List<TemplateQueryRuleVo> templateQueryRuleVos,Map map){
        List<TemplateQueryRuleVo> newList = new ArrayList<>();
        if(templateQueryRuleVos==null||templateQueryRuleVos.isEmpty()){
            return newList;
        }
        for(int i=0;i<templateQueryRuleVos.size();i++){
            if(map.containsKey(templateQueryRuleVos.get(i).getId())){
                newList.add(templateQueryRuleVos.remove(i));
            }
        }
        return newList;
    }
}
