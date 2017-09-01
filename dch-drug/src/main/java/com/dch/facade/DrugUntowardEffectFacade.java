package com.dch.facade;

import com.dch.entity.DrugUntowardEffect;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugUntowardEffectFacade extends BaseFacade {

    /**
     * 添加、删除、修改不良反应信息
     * @param drugUntowardEffect
     * @return
     */
    @Transactional
    public Response mergeDrugUntowardEffect(DrugUntowardEffect drugUntowardEffect) {

        DrugUntowardEffect merge = merge(drugUntowardEffect);

        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取药品不良反应信息
     * @param drugId
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugUntowardEffect> getDrugUntowardEffects(String drugId, int perPage, int currentPage) {
        String hql="from DrugUntowardEffect where status <> '-1' ";
        String hqlCount="select count(*) from DrugUntowardEffect where status <> '-1' ";
        if(drugId!=null && !"".equals(drugId)){
            hql+="and drugId= '" + drugId + "' ";
            hqlCount+="and drugId= '" + drugId + "' ";
        }
        TypedQuery<DrugUntowardEffect> query = createQuery(DrugUntowardEffect.class, hql, new ArrayList<>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugUntowardEffect> drugUntowardEffectList = query.getResultList();
        page.setCounts(counts);
        page.setData(drugUntowardEffectList);
        return page;
    }

    /**
     * 获取单一的药品不良信息
     * @param id
     * @return
     * @throws Exception
     */
    public DrugUntowardEffect getDrugUntowardEffect(String id) throws Exception {
        String hql="from DrugUntowardEffect where status <> '-1' and id= '" +id+ "' ";
        List<DrugUntowardEffect> resultList = createQuery(DrugUntowardEffect.class, hql, new ArrayList<>()).getResultList();
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("无药品不良反应信息！");
        }
    }
}
