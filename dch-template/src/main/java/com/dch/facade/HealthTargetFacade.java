package com.dch.facade;

import com.dch.entity.FutureTarget;
import com.dch.entity.HealthTarget;
import com.dch.entity.TargetLevel;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.HealthTargetVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkqa on 2018/6/14.
 */
@Component
public class HealthTargetFacade extends BaseFacade{

    @Transactional
    public HealthTarget saveHealthTarget(HealthTargetVo healthTargetVo) {
        HealthTarget healthTarget = new HealthTarget();
        healthTarget.setName(healthTargetVo.getName());
        healthTarget.setParentId(healthTargetVo.getParentId());
        healthTarget.setComputeMethod(healthTargetVo.getComputeMethod());
        healthTarget.setDependFile(healthTargetVo.getDependFile());
        healthTarget.setGrade(healthTargetVo.getGrade());
        healthTarget.setOtherCountryStep(healthTargetVo.getOtherCountryStep());
        healthTarget.setOtherCountryValue(healthTargetVo.getOtherCountryValue());
        healthTarget.setStep(healthTargetVo.getStep());
        healthTarget.setSimilarCountry(healthTargetVo.getSimilarCountry());
        healthTarget.setStatus(healthTargetVo.getStatus());
        HealthTarget merge = merge(healthTarget);
        return merge;
    }

    @Transactional
    public void deleteTargetLevel(String id) {
        String hql = "delete from TargetLevel where relatedHealthTarget = '"+id+"'";
        excHql(hql);
    }

    @Transactional
    public void deleteFutureTarget(String id) {
        String hql = "delete from FutureTarget where relatedHealthTarget = '"+id+"'";
        excHql(hql);
    }

    public List<TargetLevel> getTargetLevelList(String id) {
        String hql = " from TargetLevel where relatedHealthTarget = '"+id+"'";
        List<TargetLevel> targetLevelList = createQuery(TargetLevel.class,hql,new ArrayList<Object>()).getResultList();
        return targetLevelList;
    }

    public List<FutureTarget> getFutureTargetList(String id) {
        String hql = " from FutureTarget where relatedHealthTarget = '"+id+"'";
        List<FutureTarget> futureTargetList = createQuery(FutureTarget.class,hql,new ArrayList<Object>()).getResultList();
        return futureTargetList;
    }

    public Page<HealthTarget> getHealthTargetPage(String name,int perPage, int currentPage) {
        String hql = " from HealthTarget where status<>'-1' ";
        if(!StringUtils.isEmptyParam(name)){
            hql += " and name like '%"+name+"%' ";
        }
        hql += " order by createDate desc";
        Page<HealthTarget> healthTargetPage = getPageResult(HealthTarget.class,hql,perPage,currentPage);
        return healthTargetPage;
    }
}
