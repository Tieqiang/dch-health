package com.dch.facade;

import com.dch.entity.DrugNaturalActive;
import com.dch.entity.DrugNaturalEvaluation;
import com.dch.facade.common.BaseFacade;
import com.dch.vo.DrugNatureVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 天然药物维护
 * Created by Administrator on 2017/8/22.
 */
@Component
public class DrugNatureFacade extends BaseFacade {


    /**
     *  获取药物活性列表
     * @param name
     * @param wherehql
     * @return
     */
    public List<DrugNatureVo> getDrugNatures(String name, String wherehql) {
        List<DrugNatureVo> drugNatureVos = new ArrayList<>();
        String hql = "from DrugNaturalActive as d where d.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql+=" and (d.drugNaturalNameCn like '%"+name+"%' or d.drugNaturalNameLatin like '%"+name+"%')";
        }
        if(wherehql!=null&&!"".equals(wherehql)){
            hql+=wherehql ;
        }
        List<DrugNaturalActive> drugNaturalActives =createQuery(DrugNaturalActive.class,hql,new ArrayList<Object>()).getResultList();
        for(DrugNaturalActive drugNaturalActive:drugNaturalActives){
            List<DrugNaturalEvaluation> drugNaturalEvaluation = getDrugNaturalEvaluation(drugNaturalActive.getId());
            DrugNatureVo drugNatureVo = new DrugNatureVo();
            drugNatureVo.setDrugNaturalActive(drugNaturalActive);
            drugNatureVo.setDrugNaturalEvaluations(drugNaturalEvaluation);
            drugNatureVos.add(drugNatureVo);
        }
        return drugNatureVos;
    }


    /**
     * 查询天然活性药物受体
     * @param drugNaturalId
     * @return
     */
    private List<DrugNaturalEvaluation> getDrugNaturalEvaluation(String drugNaturalId){
        String hqlTemp="from DrugNaturalEvaluation as d where d.status<>'-1' and d.drugNaturalId='"+drugNaturalId+"'";
        List<DrugNaturalEvaluation> resultList = createQuery(DrugNaturalEvaluation.class, hqlTemp, new ArrayList<Object>()).getResultList();
        return resultList ;
    }

    /**
     * 获取单一对象
     * @param id
     * @return
     */
    public DrugNatureVo getDrugNature(String id) throws Exception {

        DrugNaturalActive drugNaturalActive = get(DrugNaturalActive.class, id);
        if("-1".equals(drugNaturalActive.getStatus())||"".equals(drugNaturalActive.getStatus())){
            throw  new Exception("记录已经被删除");
        }
        List<DrugNaturalEvaluation> drugNaturalEvaluations = getDrugNaturalEvaluation(drugNaturalActive.getId());
        DrugNatureVo drugNatureVo= new DrugNatureVo();
        drugNatureVo.setDrugNaturalActive(drugNaturalActive);
        drugNatureVo.setDrugNaturalEvaluations(drugNaturalEvaluations);
        return drugNatureVo;
    }

    /**
     * 添加、删除、修改天然药物活性
     * @param drugNatureVo
     * @return
     */
    @Transactional
    public DrugNatureVo mergeDrugNature(DrugNatureVo drugNatureVo) {

        DrugNatureVo drugNatureVo1 = new DrugNatureVo();
        DrugNaturalActive drugNaturalActive = drugNatureVo.getDrugNaturalActive();
        drugNaturalActive = merge(drugNaturalActive);
        drugNatureVo1.setDrugNaturalActive(drugNaturalActive);

        List<DrugNaturalEvaluation> drugNaturalEvaluations = drugNatureVo.getDrugNaturalEvaluations();
        for(DrugNaturalEvaluation drugNaturalEvaluation:drugNaturalEvaluations){
            drugNaturalEvaluation.setDrugNaturalId(drugNaturalActive.getId());
            drugNatureVo1.getDrugNaturalEvaluations().add(merge(drugNaturalEvaluation));
        }
        return drugNatureVo1;
    }
}
