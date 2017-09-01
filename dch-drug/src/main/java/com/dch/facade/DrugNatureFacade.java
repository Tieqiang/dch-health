package com.dch.facade;

import com.dch.entity.DrugNaturalActive;
import com.dch.entity.DrugNaturalEvaluation;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugNatureVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
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
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugNatureVo> getDrugNatures(String name, String wherehql, int perPage, int currentPage) {
        List<DrugNatureVo> drugNatureVos = new ArrayList<>();
        String hql = "from DrugNaturalActive as d where d.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql+=" and (d.drugNaturalNameCn like '%"+name+"%' or d.drugNaturalNameLatin like '%"+name+"%')";
        }
        if(wherehql!=null&&!"".equals(wherehql)){
            hql+=wherehql ;
        }
        TypedQuery<DrugNaturalActive> query = createQuery(DrugNaturalActive.class, hql, new ArrayList<Object>());
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugNaturalActive> drugNaturalActives =query.getResultList();
        for(DrugNaturalActive drugNaturalActive:drugNaturalActives){
            List<DrugNaturalEvaluation> drugNaturalEvaluation = getDrugNaturalEvaluation(drugNaturalActive.getId());
            DrugNatureVo drugNatureVo = new DrugNatureVo();
            drugNatureVo.setDrugNaturalActive(drugNaturalActive);
            drugNatureVo.setDrugNaturalEvaluations(drugNaturalEvaluation);
            drugNatureVos.add(drugNatureVo);
        }
        page.setCounts((long) drugNatureVos.size());
        page.setData(drugNatureVos);
        return page;
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
