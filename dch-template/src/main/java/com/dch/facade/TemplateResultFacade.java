package com.dch.facade;

import com.dch.entity.TemplateResult;
import com.dch.entity.User;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateMasterVo;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Component
public class TemplateResultFacade extends BaseFacade {
    /**
     * 获取表单结果
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateMasterVo> getTemplateMasterVos(String projectId, int perPage, int currentPage) {
        String hql="select new com.dch.vo.TemplateMasterVo(m.id,m.templateName,m.templateLevel,m.templateStatus,m.projectId,m.templateDesc" +
                ",m.createDate,m.modifyDate,m.createBy,m.modifyBy,m.status,(SELECT COUNT(distinct docId) FROM TemplateResult r WHERE r.templateId = m.id) as num) from TemplateMaster as m where m.status <> '-1' and projectId='"+projectId+"'";

        String hqlCount="select count(*) from TemplateMaster m where m.status <> '-1' and projectId='"+projectId+"'";


        TypedQuery<TemplateMasterVo> query = createQuery(TemplateMasterVo.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateMasterVo> templateMasterVoList = query.getResultList();
        page.setCounts(counts);
        page.setData(templateMasterVoList);
        return page;
    }

    /**
     * 获取某一具体表单结果
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateResult> getTemplateResults(String templateId,String docId, int perPage, int currentPage) {
        String hql=" from TemplateResult where status <> '-1' and templateId='"+templateId+"'";
        if(!StringUtils.isEmptyParam(docId)){
            hql += " and docId = '"+docId+"'";
        }
        Page<TemplateResult> pageResult = getPageResult(TemplateResult.class, hql, perPage, currentPage);
        List<TemplateResult> results = pageResult.getData();

        List<User> users = findAll(User.class);
        for (TemplateResult result:results){
            String userid = result.getCreateBy();
            for(User user:users){
                if(userid.equals(user.getId())){
                    result.setCreateBy(user.getUserName());
                }
            }
        }

        return pageResult;
    }
}
