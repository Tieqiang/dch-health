package com.dch.service;

import com.dch.entity.Feature;
import com.dch.entity.FeatureZone;
import com.dch.facade.FeatureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import java.util.List;

/**
 * 特色栏目维护接口
 * Created by Administrator on 2017/8/7.
 */
@Produces("application/json")
@Path("front/feature")
@Controller
public class FeatureService {

    @Autowired
    private FeatureFacade featureFacade ;

    /**
     * 添加、修改、删除 特色区域
     * @param featureZone
     * @return
     */
    @POST
    @Path("merge-feature-zone")
    @Transactional
    public FeatureZone mergeFeatureZone(FeatureZone featureZone){
        return featureFacade.merge(featureZone) ;
    }


    /**
     * 获取特色区域
     * @param featureZoneName 如果存在则模糊匹配
     * @param zoneNo          根据代码获取
     * @return
     */
    @GET
    @Path("get-feature-zones")
    public List<FeatureZone> getFeatureZones(@QueryParam("featureZoneName") String featureZoneName,
                                             @QueryParam("zoneNo") String zoneNo){
        return featureFacade.getFeatureZones(featureZoneName,zoneNo);
    }

    /**
     * 获取某一个特色区域
     * @param featureZoneId 特色区域ID
     * @return
     */
    @GET
    @Path("get-feature-zone")
    public FeatureZone getFeatureZone(@QueryParam("featureZoneId")String featureZoneId) throws Exception {
        FeatureZone featureZone = featureFacade.get(FeatureZone.class, featureZoneId);
        if ("-1".equals(featureZone.getStatus())){
            throw new Exception("该内容已经被删除。");
        }
        return featureZone;
    }


    /**
     * 保存主题
     * @param feature
     * @return
     */
    @Transactional
    @Path("merge-feature")
    @POST
    public Feature mergeFeature(Feature feature){
        return featureFacade.merge(feature);
    }

    /**
     *
     *获取区域的栏目的内容
     * @param zoneId 区域ID
     * @param type   类型名称，如果不为空则进行模糊匹配
     * @return
     * @return
     */
    @GET
    @Path("get-features")
    public List<Feature> getFeatures(@QueryParam("zoneId") String zoneId,
                                     @QueryParam("type")String type){
        return featureFacade.getFeatures(zoneId,type) ;

    }

    /**
     * 获取具体的栏目内容
     * @param featureId
     * @return
     */
    @GET
    @Path("get-feature")
    public Feature getFeature(@QueryParam("featureId")String featureId) throws Exception {
        Feature feature = featureFacade.get(Feature.class, featureId);
        if ("-1".equals(feature.getStatus())){
            throw new Exception("该内容已经被删除。");
        }
        return feature;
    }

}
