package com.dch.facade;

import com.dch.entity.DataElementStandard;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Component
public class DataElementStandardFacade extends BaseFacade{


    /**
     * 获取元数据科研体系
     * @return
     */
    public List<DataElementStandard> getDataElementStandards() {

        String hql = "from DataElementStandard where status<>'-1'" ;
        return createQuery(DataElementStandard.class,hql,new ArrayList<Object>()).getResultList();
    }
}
