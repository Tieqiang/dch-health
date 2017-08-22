package com.dch.facade;

import com.dch.entity.DataElementCategory;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/8.
 */
@Component
public class DataCategoryFacade extends BaseFacade{

    /**
     * 获取某个体系下的元数据或者值域目录
     * @param standardId
     * @param categoryType
     * @return
     * @throws Exception
     */
    public List<DataElementCategory> getDataCategorys(String standardId,String categoryType)throws Exception{

        String hql="from DataElementCategory where status<> '-1'and standardId='"+standardId+"'and categoryType='"+categoryType+"'";
        List<DataElementCategory> dataElementCategoryList = createQuery(DataElementCategory.class,hql,new ArrayList<Object>()).getResultList();
        return dataElementCategoryList;
    }

    /**
     * 根据ID获取元数据或者值域分类
     * @param categoryId
     * @return
     * @throws Exception
     */
    public DataElementCategory getDataCategory(String categoryId)throws Exception{
        String hql="from DataElementCategory where status<> '-1'and categoryType='"+categoryId+"'";
        List<DataElementCategory> dataElementCategoryList = createQuery(DataElementCategory.class,hql,new ArrayList<Object>()).getResultList();
        if(dataElementCategoryList!=null && !dataElementCategoryList.isEmpty()){
            return dataElementCategoryList.get(0);
        }else{
            throw new Exception("元数据或值域目录不存在");
        }
    }

}
