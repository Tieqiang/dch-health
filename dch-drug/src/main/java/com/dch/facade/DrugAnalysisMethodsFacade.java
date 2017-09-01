package com.dch.facade;

import com.dch.entity.DrugAnalysisMethods;
import com.dch.entity.DrugDiseaseTreatmentGuide;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugAnalysisMethodsFacade extends BaseFacade {
    /**
     * 添加、修改、删除药品专利文献
     * @param drugAnalysisMethods
     * @return
     */
    @Transactional
    public Response mergeDrugAnalysisMehtods(DrugAnalysisMethods drugAnalysisMethods) {
        return Response.status(Response.Status.OK).entity(merge(drugAnalysisMethods)).build();
    }

    /**获取药品专利文献
     *
     * @param methodName
     * @param wherehql
     * @return
     */
    public Page<DrugAnalysisMethods> getDrugAnalysisMethodses(String methodName, String wherehql,int perPage,int currentPage) {
        Page<DrugAnalysisMethods> drugAnalysisMethodsPage = new Page<>();
        String hql="from DrugAnalysisMethods where status <> '-1' ";
        String hqlCount = "select count(*) from DrugAnalysisMethods where status <> '-1' ";
        if(null!=methodName&&!"".equals(methodName)){
            hql+="and methodName like '%"+methodName+"%'";
            hqlCount += "and methodName like '%"+methodName+"%'";
        }
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += " and "+wherehql;
            hqlCount +=" and "+wherehql;
        }
        TypedQuery<DrugAnalysisMethods> typedQuery = createQuery(DrugAnalysisMethods.class,hql,new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        drugAnalysisMethodsPage.setCounts(counts);
        if(perPage<=0){
            perPage =100;
        }
        if(currentPage<=0){
            currentPage=1;
        }
        typedQuery.setFirstResult((currentPage-1)*perPage);
        typedQuery.setMaxResults(currentPage*perPage);
        drugAnalysisMethodsPage.setPerPage((long)perPage);
        List<DrugAnalysisMethods> drugAnalysisMethodsList = typedQuery.getResultList();
        drugAnalysisMethodsPage.setData(drugAnalysisMethodsList);
        return drugAnalysisMethodsPage;
    }

    /**
     *获取单个药品专利文献
     * @param methodId
     * @return
     */
    public DrugAnalysisMethods getDrugAnalysisMethods(String methodId) throws Exception {
        String hql="from DrugAnalysisMethods where status <> '-1' and id='" +methodId+ "' ";
        List<DrugAnalysisMethods> drugAnalysisMethodsList = createQuery(DrugAnalysisMethods.class, hql, new ArrayList<>()).getResultList();
        if(drugAnalysisMethodsList!=null && drugAnalysisMethodsList.size()>0){
            return drugAnalysisMethodsList.get(0);
        }else{
            throw new Exception("该药品专利文献还未注册！");
        }
    }
}
