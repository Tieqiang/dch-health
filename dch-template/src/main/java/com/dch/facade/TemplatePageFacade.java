package com.dch.facade;

import com.dch.entity.TemplatePage;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.TemplatePageAndResultVo;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
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
        TemplatePage merge = merge(templatePage);
        return Response.status(Response.Status.OK).entity(merge).build();
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

    public List<TemplatePageAndResultVo> getTemplatePagesAndResults(String templateId, String docId) {
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
        if(!StringUtils.isEmptyParam(templatePageIds) && !StringUtils.isEmptyParam(docId)){
            templatePageIds = templatePageIds.substring(0,templatePageIds.length()-1);
            String resultSql = " from TemplateResult where status<>'-1' and pageId in ("+templatePageIds+")";
            List<TemplateResult> templateResultList = createQuery(TemplateResult.class, resultSql, new ArrayList<Object>()).getResultList();
            for(TemplateResult templateResult:templateResultList){
                map.put(templateResult.getPageId()+templateResult.getDocId(),templateResult);
            }
            for(TemplatePage templatePage:templatePageList){
                TemplatePageAndResultVo templatePageAndResultVo = new TemplatePageAndResultVo();
                templatePageAndResultVo.setTemplatePage(templatePage);
                if(map.get(templatePage.getId()+docId)!=null){
                    templatePageAndResultVo.setTemplateResult((TemplateResult)map.get(templatePage.getId()+docId));
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
}
