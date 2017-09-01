package com.dch.facade;

import com.dch.entity.DrugAd;
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
public class DrugAdFacade extends BaseFacade {

    /**
     * 添加、删除、修改药品品广告
     * @param drugAd
     * @return
     */
    @Transactional
    public Response mergeDrugAd(DrugAd drugAd) {
        DrugAd merge= merge(drugAd);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取药品广告信息
     * @param drugId
     * @param drugCode
     * @return
     * @throws Exception
     */
    public Page<DrugAd> getDrugAds(String drugId, String drugCode, int perPage, int currentPage) throws Exception {
        String hql="from DrugAd where status<> '-1' ";
        String hqlCount="select count(*) from DrugAd where status<> '-1'";

        if(StringUtils.isEmptyParam(drugId)&&StringUtils.isEmptyParam(drugCode)){

            throw new Exception("所传参数不可以为空哦！");
        }
        if(drugId!=null&&!"".equals(drugId)){
            hql+="and drugId='"+ drugId +"' ";
            hqlCount+="and drugId='"+ drugId +"' ";
        }
        if(drugCode!=null&&!"".equals(drugCode)){
            hql+="and drugCode =' "+ drugCode +"'";
            hqlCount+="and drugCode =' "+ drugCode +"'";
        }
        TypedQuery<DrugAd> query = createQuery(DrugAd.class, hql, new ArrayList<>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugAd> drugAdList = query.getResultList();
        page.setCounts(counts);
        page.setData(drugAdList);
        return page;
    }

    /**
     * 获取单一药品广告
     * @param adId
     * @return
     * @throws Exception
     */
    public DrugAd getDrugAd(String adId) throws Exception {

        String hql=" from DrugAd where status <> '-1' and id='"+ adId +"'";
        List<DrugAd> drugAds = createQuery(DrugAd.class, hql, new ArrayList<>()).getResultList();
        if(drugAds!=null &&drugAds.size()>0){
            return drugAds.get(0);
        }else{
            throw new Exception("该药品的广告信息不存在");
        }

    }
}
