package com.dch.facade;

import com.dch.entity.TemplatePage;
import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.TemplatePageAndResultVo;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplatePageFacade extends BaseFacade {

    /**
     * 添加、删除、删除表单页
     * @param templatePage
     * @return
     */
    @Transactional
    public Response mergeTemplatePage(TemplatePage templatePage) {
        Long beforPageNum = getTemplatePageNumber(templatePage.getTemplateId());
        TemplatePage merge = merge(templatePage);
        Long afterPageNum = getTemplatePageNumber(templatePage.getTemplateId());
        if(beforPageNum!=afterPageNum){//设计的表单页有增加或者减少
            //更新填报数据的完成率情况
            changeResultMasterRate(afterPageNum,templatePage.getTemplateId());
        }
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    public Long getTemplatePageNumber(String templateId){
        String hql = "select count(*) from TemplatePage where status<>'-1' and templatePageContent is not null " +
                " and templateId = '"+templateId+"'";
        return createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
    }

    @Transactional
    public void changeResultMasterRate(Long total,String templateId){
        String hql = " from TemplateResultMaster where status<>'-1' and templateId = '"+templateId+"'";
        List<TemplateResultMaster> templateResultMasters = createQuery(TemplateResultMaster.class,hql,new ArrayList<Object>()).getResultList();
        for(TemplateResultMaster templateResultMaster:templateResultMasters){
            Long realTotal = getRealTotal(total,templateId);
            Long doneNumber = getDoneNumber(templateResultMaster.getId());
            if(realTotal>0){
                String doneNumStr = doneNumber+"";
                String totalStr = realTotal+"";
                DecimalFormat df = new DecimalFormat("#0.00");
                String completeRate =df.format(Double.valueOf(doneNumStr)/Double.valueOf(totalStr));
                templateResultMaster.setCompleteRate(Double.valueOf(completeRate));
                merge(templateResultMaster);
            }
        }
    }

    public Long getDoneNumber(String masterId){
        String doneHql = "select count(pageId) from TemplateResult where status<>'-1' and templateResult is not null and masterId = '"+masterId+"'";
        Long doneNum = createQuery(Long.class,doneHql,new ArrayList<Object>()).getSingleResult();
        return doneNum;
    }
    public Long getRealTotal(Long total,String templateId){
        Long number = total;
        String totalHql = "select id from TemplatePage where status<>'-1' and templatePageContent is not null  and templateId = '"+templateId+"'";
        List<String> IdList = createQuery(String.class,totalHql,new ArrayList<Object>()).getResultList();
        for(String id:IdList){
            Boolean isHaveEmptyChildren = judgeIsHaveAllEmptyChildren(id);
            if(isHaveEmptyChildren){
                number = number-1;
            }
        }
        return number;
    }

    public Boolean judgeIsHaveAllEmptyChildren(String id){
        Boolean isAllEmpty = false;
        String hql = "select count(*) from TemplatePage where parentId = '"+id+"' and status<>'-1' ";
        Long number = createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
        if(number>0){
            String hqlIn = "select id from TemplatePage where parentId = '"+id+"' and templatePageContent is not null and status<>'-1' ";
            List<String> IdList = createQuery(String.class,hqlIn,new ArrayList<Object>()).getResultList();
            if(IdList==null || IdList.isEmpty()){
                isAllEmpty = true;
            }else{
                for(String tid:IdList){
                    isAllEmpty = judgeIsHaveAllEmptyChildren(tid);
                    if(!isAllEmpty){
                        return isAllEmpty;
                    }
                }
            }
        }
        return isAllEmpty;
    }
    /**
     * 获取表单页面
     * @param templateId
     * @return
     */
    public List<TemplatePage> getTemplatePages(String templateId) {
        String hql=" from TemplatePage where status <> '-1' and templateId = '"+templateId+"'";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<Object>()).getResultList();
        return templatePageList;
    }

    /**
     * 获取具体的表单页
     * @param pageId
     * @return
     * @throws Exception
     */
    public TemplatePage getTemplatePage(String pageId) throws Exception {
        String hql=" from TemplatePage where status <> '-1' and id = '"+pageId+"'";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<Object>()).getResultList();
        if(templatePageList!=null &&templatePageList.size()>0){
            return templatePageList.get(0);
        }else{
            throw new Exception("该模板页面不存在");
        }
    }

    public List<TemplatePageAndResultVo> getTemplatePagesAndResults(String templateId, String masterId) {
        List<TemplatePageAndResultVo> templatePageAndResultVos = new ArrayList<TemplatePageAndResultVo>();
        Map map = new HashMap();
        String hql = " from TemplatePage where status<>'-1' and templateId = '"+templateId+"'";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<Object>()).getResultList();
        StringBuffer pageIds = new StringBuffer("");
        if(templatePageList!=null && !templatePageList.isEmpty()){
            for(TemplatePage templatePage:templatePageList){
                pageIds.append("'").append(templatePage.getId()).append("',");
            }
        }
        String templatePageIds = pageIds.toString();
        if(!StringUtils.isEmptyParam(templatePageIds) && !StringUtils.isEmptyParam(masterId)){
            templatePageIds = templatePageIds.substring(0,templatePageIds.length()-1);
            String resultSql = " from TemplateResult where status<>'-1' and pageId in ("+templatePageIds+") and masterId = '"+masterId+"'";
            List<TemplateResult> templateResultList = createQuery(TemplateResult.class, resultSql, new ArrayList<Object>()).getResultList();
            for(TemplateResult templateResult:templateResultList){
                map.put(templateResult.getPageId()+templateResult.getMasterId(),templateResult);
            }
            for(TemplatePage templatePage:templatePageList){
                TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
                templatePageAndResultVo.setTemplatePage(templatePage);
                if(map.get(templatePage.getId()+masterId)!=null){
                    templatePageAndResultVo.setTemplateResult((TemplateResult)map.get(templatePage.getId()+masterId));
                }
                templatePageAndResultVos.add(templatePageAndResultVo);
            }
        }else{
            for(TemplatePage templatePage:templatePageList){
                TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
                templatePageAndResultVo.setTemplatePage(templatePage);
                templatePageAndResultVos.add(templatePageAndResultVo);
            }
        }
        return templatePageAndResultVos;
    }

    public List<TemplatePageAndResultVo> getTemplatePagesAndFirstResult(String templateId, String masterId) {
        List<TemplatePageAndResultVo> templatePageAndResultVos = new ArrayList<TemplatePageAndResultVo>();
        Map map = new HashMap();
        String hql = " from TemplatePage where status<>'-1' and templateId = '"+templateId+"'";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<Object>()).getResultList();
        StringBuffer pageIds = new StringBuffer("");
        if(templatePageList!=null && !templatePageList.isEmpty()){
            for(TemplatePage templatePage:templatePageList){
                if("1".equals(templatePage.getTemplatePageOrder())){
                    pageIds.append(templatePage.getId());
                }
            }
        }
        String templatePageIds = pageIds.toString();
        if(!"".equals(templatePageIds)){
            templatePageIds = templatePageList.get(1).getId();
        }
        if(!StringUtils.isEmptyParam(templatePageIds) && !StringUtils.isEmptyParam(masterId)){
            String resultSql = " from TemplateResult where status<>'-1' and pageId in ("+templatePageIds+") and masterId = '"+masterId+"'";
            List<TemplateResult> templateResultList = createQuery(TemplateResult.class, resultSql, new ArrayList<Object>()).getResultList();
            for(TemplateResult templateResult:templateResultList){
                map.put(templateResult.getPageId()+templateResult.getMasterId(),templateResult);
            }
            for(TemplatePage templatePage:templatePageList){
                TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
                templatePageAndResultVo.setTemplatePage(templatePage);
                if(map.get(templatePage.getId()+masterId)!=null){
                    templatePageAndResultVo.setTemplateResult((TemplateResult)map.get(templatePage.getId()+masterId));
                }
                templatePageAndResultVos.add(templatePageAndResultVo);
            }
        }else{
            for(TemplatePage templatePage:templatePageList){
                TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
                templatePageAndResultVo.setTemplatePage(templatePage);
                templatePageAndResultVos.add(templatePageAndResultVo);
            }
        }
        return templatePageAndResultVos;
    }

    public List<TemplatePageAndResultVo> getTemplatePageResult(String templateId, String masterId, String pageId) {
        List<TemplatePageAndResultVo> templatePageAndResultVos = new ArrayList<TemplatePageAndResultVo>();
        TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
        String hql = " from TemplatePage where status<>'-1' and id = '"+pageId+"'";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<Object>()).getResultList();
        if(!templatePageList.isEmpty()){
            templatePageAndResultVo.setTemplatePage(templatePageList.get(0));
        }
        String resultSql = " from TemplateResult where status<>'-1' and pageId = '"+pageId+"' and masterId = '"+masterId+"'";
        List<TemplateResult> templateResultList = createQuery(TemplateResult.class, resultSql, new ArrayList<Object>()).getResultList();
        if(!templateResultList.isEmpty()){
            templatePageAndResultVo.setTemplateResult(templateResultList.get(0));
        }
        templatePageAndResultVos.add(templatePageAndResultVo);
        return templatePageAndResultVos;
    }
}
