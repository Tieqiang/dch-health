package com.dch.facade;

import com.dch.entity.ResourceDict;
import com.dch.entity.ResourceOperation;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
@Component
public class ResourceFacade extends BaseFacade {
    /**
     * 获取所有模块的操作
     * @param moduleId
     * @return
     */
    public List<ResourceDict> getResources(String moduleId){
        String hql = "from ResourceDict as n where n.status<>'-1' and n.moduleId='"+moduleId+"'" ;
        return createQuery(ResourceDict.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取某个资源的所有操作
     * @param functionId
     * @return
     */
    public List<ResourceOperation> getResourceOperations(String functionId){
        String hql = "from ResourceOperation as f where f.status<>'-1' and f.resourceDictId='"+functionId+"'" ;
        return createQuery(ResourceOperation.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据模块id获取模块下的操作权限
     * @param moduleId
     * @return
     * @throws Exception
     */
    public List<ResourceOperation> getResourceOperationsByModuleId(String moduleId) throws Exception{
        String hql = " select ro from ResourceDict as rd,ResourceOperation as ro where rd.id = ro.resourceDictId and rd.status<> '-1' " +
                "and ro.status<> '-1' and rd.moduleId = '"+moduleId+"'";
        return createQuery(ResourceOperation.class,hql,new ArrayList<Object>()).getResultList();
    }
}
