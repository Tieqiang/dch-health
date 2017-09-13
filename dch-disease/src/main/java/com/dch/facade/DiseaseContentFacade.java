package com.dch.facade;

import com.dch.entity.DiseaseContent;
import com.dch.entity.DiseaseNameDict;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

@Service
public class DiseaseContentFacade extends BaseFacade {


    /**
     * 获取疾病知识列表
     * @param categoryId
     * @param name
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<DiseaseContent> getDiseaseContents(String categoryId, String name, int perPage, int currentPage) {
        String hql = "from DiseaseContent as d where d.status<>'-1'" ;
        String hqlCount = "select count(d) "+hql ;
        if(!StringUtils.isEmptyParam(categoryId)){
            hql+=" and d.categoryId='"+categoryId+"'" ;
            hqlCount+=" and d.categoryId='"+categoryId+"'" ;
        }
        if(!StringUtils.isEmptyParam(name)){
            hql+=" and d.name like '%"+name+"%'";
            hqlCount+= " and d.name like '%"+name+"%'";
        }

        hql+=" order by d.name";
        Long count = createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        TypedQuery<DiseaseContent> diseaseContentTypedQuery = createQuery(DiseaseContent.class, hql, new ArrayList<Object>());
        if(perPage==0){
            perPage=20 ;
        }
        if(currentPage==0){
            currentPage=1 ;
        }

        diseaseContentTypedQuery.setFirstResult((currentPage-1)*perPage);
        diseaseContentTypedQuery.setMaxResults(perPage);

        List<DiseaseContent> diseaseContents = diseaseContentTypedQuery.getResultList();
        Page<DiseaseContent> page = new Page<>();
        page.setData(diseaseContents);
        page.setPerPage((long) perPage);
        page.setCounts(count);
        return page;
    }

    /**
     * 获取疾病知识的名字
     * @param contentId
     * @return
     */
    public List<DiseaseNameDict> getDiseaseContentNames(String contentId) {
        String hql ="from DiseaseNameDict as dnd where dnd.status<> '-1' and dnd.refId='"+contentId+"'" ;
        List<DiseaseNameDict> diseaseNameDicts = createQuery(DiseaseNameDict.class, hql, new ArrayList<Object>()).getResultList();
        return diseaseNameDicts;
    }
}
