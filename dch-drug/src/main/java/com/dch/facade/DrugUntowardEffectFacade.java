package com.dch.facade;

import com.dch.entity.DrugUntowardEffect;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

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
     * @return
     */
    public List<DrugUntowardEffect> getDrugUntowardEffects(String drugId) {
        String hql="from DrugUntowardEffect where status <> '-1' ";
        if(drugId!=null && !"".equals(drugId)){
            hql+="and drugId= '" + drugId + "' ";
        }
        return createQuery(DrugUntowardEffect.class, hql, new ArrayList<>()).getResultList();

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
