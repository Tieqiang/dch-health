package com.dch.facade;

import com.dch.entity.DrugNaturalChemicalComposition;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Component
public class DrugNaturalChemicalCompositionFacade extends BaseFacade {
    public List<DrugNaturalChemicalComposition> getDrugNaturalChemicalCompsitions(String name, String wherehql) {
        String hql = "from DrugNaturalChemicalComposition as a where a.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql +=" and (a.nameCn like '%"+name+"%' or a.nameEn like '%"+name+"%')";
        }
        if(wherehql!=null&&!"".equals(wherehql)){
            hql +=wherehql;
        }

        return createQuery(DrugNaturalChemicalComposition.class,hql,new ArrayList<Object>()).getResultList();
    }

    @Transactional
    public DrugNaturalChemicalComposition mergeDrugNaturalChemicalCompositon(DrugNaturalChemicalComposition drugNaturalChemicalComposition) {
        return merge(drugNaturalChemicalComposition);
    }
}
