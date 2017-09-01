package com.dch.facade;

import com.dch.entity.DrugDiseaseTreatmentGuide;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugDiseaseTreatmentFacade extends BaseFacade {

    /**
     * 获取疾病治疗指南
     * @param guideName
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugDiseaseTreatmentGuide> getTreatments(String guideName, int perPage, int currentPage) {
        String hql="from DrugDiseaseTreatmentGuide where status <> '-1' ";
        String hqlCount="select count(*) from DrugDiseaseTreatmentGuide where status <> '-1' ";
        if(null!=guideName&&!"".equals(guideName)){
            hql+="and guideName like '%"+guideName+"%'";
            hqlCount+="and guideName like '%"+guideName+"%'";
        }
        TypedQuery<DrugDiseaseTreatmentGuide> query = createQuery(DrugDiseaseTreatmentGuide.class, hql, new ArrayList<>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugDiseaseTreatmentGuide> treatmentGuideList = query.getResultList();
        page.setData(treatmentGuideList);
        page.setCounts(counts);
        return page;
    }

    /**
     * 获取单一疾病治疗指南
     * @param treatmentId
     * @return
     * @throws Exception
     */
    public DrugDiseaseTreatmentGuide getTreatment(String treatmentId) throws Exception {
        String hql="from DrugDiseaseTreatmentGuide where status <> '-1' and id='" +treatmentId+ "' ";
        List<DrugDiseaseTreatmentGuide> treatmentGuideList = createQuery(DrugDiseaseTreatmentGuide.class, hql, new ArrayList<>()).getResultList();
        if(treatmentGuideList!=null && treatmentGuideList.size()>0){
            return treatmentGuideList.get(0);
        }else{
            throw new Exception("该疾病治疗指南还未注册！");
        }
    }

    /**
     * 疾病治疗指南、添加、删除修改
     * @param drugDiseaseTreatmentGuide
     * @return
     */
    @Transactional
    public Response mergeTreatment(DrugDiseaseTreatmentGuide drugDiseaseTreatmentGuide) {
        DrugDiseaseTreatmentGuide merge = merge(drugDiseaseTreatmentGuide);
        return Response.status(Response.Status.OK).entity(merge).build();

    }
}
