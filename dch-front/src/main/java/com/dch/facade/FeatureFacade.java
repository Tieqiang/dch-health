package com.dch.facade;

import com.dch.entity.Feature;
import com.dch.entity.FeatureZone;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 特色栏目维护业务操作
 *
 * Created by Administrator on 2017/8/7.
 */
@Service
public class FeatureFacade extends BaseFacade{


    /**
     *
     * 获取特色区域
     * @param featureZoneName 如果存在则模糊匹配
     * @param zoneNo          根据代码获取
     * @return
     */
    public List<FeatureZone> getFeatureZones(String featureZoneName, String zoneNo) {
        String hql = "from FeatureZone as zone where zone.status<>'-1' " ;

        if(featureZoneName!=null&&!"".equals(featureZoneName)){
            hql+=" and zone.featureZoneName like '%"+featureZoneName+"%'";
        }

        if(zoneNo!=null&&!"".equals(zoneNo)){
            hql+=" and zone.featureZoneNo = '"+zoneNo+"'";
        }
        return createQuery(FeatureZone.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *获取区域的栏目的内容
     * @param zoneId 区域ID
     * @param type   类型名称，如果不为空则进行模糊匹配
     * @return
     */
    public List<Feature> getFeatures(String zoneId, String type) {
        String hql = "from Feature as f where f.status<>'-1' and  f.zoneId='"+zoneId+"'" ;
        if(type!=null&&!"".equals(type)){
            hql+=" and f.type = '"+type+"'";
        }
        return createQuery(Feature.class,hql,new ArrayList<Object>()).getResultList();
    }
}
