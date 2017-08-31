package com.dch.facade;

import com.dch.entity.DrugNaturalChemicalComposition;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Component
public class DrugNaturalChemicalCompositionFacade extends BaseFacade {
    public Page<DrugNaturalChemicalComposition> getDrugNaturalChemicalCompsitions(String name, String wherehql, int perPage, int currentPage) {
        Page<DrugNaturalChemicalComposition> drugNaturalChemicalCompositionPage = new Page<>();
        String hql = "from DrugNaturalChemicalComposition as a where a.status<>'-1'" ;
        String hqlCount = "select count(*) from DrugNaturalChemicalComposition as a where a.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql +=" and (a.nameCn like '%"+name+"%' or a.nameEn like '%"+name+"%')";
            hqlCount += " and (a.nameCn like '%"+name+"%' or a.nameEn like '%"+name+"%')";
        }
        if(wherehql!=null&&!"".equals(wherehql)){
            hql +=wherehql;
            hqlCount += wherehql;
        }
        TypedQuery<DrugNaturalChemicalComposition> typedQuery = createQuery(DrugNaturalChemicalComposition.class,hql,new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        drugNaturalChemicalCompositionPage.setCounts(counts);
        if(perPage<=0){
            perPage = 100;
        }
        if(perPage>0){
            if(currentPage<=0){
                currentPage =1;
            }
            typedQuery.setFirstResult((currentPage-1)*perPage);
            typedQuery.setMaxResults(currentPage*perPage);
            drugNaturalChemicalCompositionPage.setPerPage((long)perPage);
        }
        List<DrugNaturalChemicalComposition> drugNaturalChemicalCompositionList = typedQuery.getResultList();
        drugNaturalChemicalCompositionPage.setData(drugNaturalChemicalCompositionList);
        return drugNaturalChemicalCompositionPage;
    }

    @Transactional
    public DrugNaturalChemicalComposition mergeDrugNaturalChemicalCompositon(DrugNaturalChemicalComposition drugNaturalChemicalComposition) {
        return merge(drugNaturalChemicalComposition);
    }
}
