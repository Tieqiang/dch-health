package com.dch.facade;

import com.dch.entity.DataVersion;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/7.
 */
@Component
public class DataVersionFacade extends BaseFacade {

    /**
     * 获取某个体系下的元数据版本信息
     * @param dataElementStandardId
     * @return
     * @throws Exception
     */
    public List<DataVersion> getDataVersions(String dataElementStandardId) throws Exception{
        String hql="from DataVersion where status<> '-1'and dataElementStandardId = '"+dataElementStandardId+"' ";
        List<DataVersion> dataVersionList = createQuery(DataVersion.class,hql,new ArrayList<Object>()).getResultList();
//        if(dataVersionList!=null && !dataVersionList.isEmpty()){
//            return dataVersionList.get(0);
//        }else{
//            throw new Exception("版本信息不存在");
//        }
        return dataVersionList ;
    }

    /**
     * 获取某一个元数据版本信息
     * @param dataVersionId
     * @return
     * @throws Exception
     */
    public DataVersion getDataVersion(String dataVersionId)throws Exception{
        String hql="from DataVersion where status<> '-1'and id = '"+dataVersionId+"' ";
        List<DataVersion> dataVersionList = createQuery(DataVersion.class,hql,new ArrayList<Object>()).getResultList();
        if(dataVersionList!=null && !dataVersionList.isEmpty()){
            return dataVersionList.get(0);
        }else{
            throw new Exception("元数据版本不存在");
        }

    }

}
