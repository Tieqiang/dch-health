package com.dch.facade;

import com.dch.entity.DrugAdministrationProtect;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugAdministrationProtectFacade extends BaseFacade {

    /**
     * 添加、删除、修改药品行政政策
     * @param drugAdministrationProtect
     * @return
     */
    @Transactional
    public Response mergeAdministrationProtect(DrugAdministrationProtect drugAdministrationProtect) {

        DrugAdministrationProtect merge = merge(drugAdministrationProtect);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取药品行政曾策保护
     * @param drugId
     * @param perPage
     *@param currentPage @return
     */
    public List<DrugAdministrationProtect> getAdministrationProtects(String drugId, int perPage, int currentPage) {
        String hql="from DrugAdministrationProtect where status <> '-1' ";
        if(drugId!=null && !"".equals(drugId)){
            hql+="and drugId = '" +drugId+ "'";
        }
        List<DrugAdministrationProtect> drugAdministrationProtectList = createQuery(DrugAdministrationProtect.class, hql, new ArrayList<>()).getResultList();
        if(drugAdministrationProtectList!=null && !drugAdministrationProtectList.isEmpty()){
            drugAdministrationProtectList = drugAdministrationProtectList.subList(0,1);
        }
        return drugAdministrationProtectList;
    }

    /**
     * 获取单一的药品行政政策保护
     * @param protectId
     * @return
     * @throws Exception
     */
    public DrugAdministrationProtect getAdministrationProtect(String protectId) throws Exception {

        String hql="from DrugAdministrationProtect where status <> '-1' and id='" +protectId+ "'";
        List<DrugAdministrationProtect> drugAdministrationProtectList = createQuery(DrugAdministrationProtect.class, hql, new ArrayList<>()).getResultList();
        if(drugAdministrationProtectList!=null && drugAdministrationProtectList.size()>0){
            return drugAdministrationProtectList.get(0);
        }else{
            throw new Exception("该药品行政政策保护不存在！");
        }

    }
}
