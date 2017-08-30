package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.entity.FrontSearchCategoryField;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FrontDataFacade extends BaseFacade {

    /**
     * 根据id获取检索分类详细信息
     * @param id
     * @return
     * @throws Exception
     */
    public FrontSearchCategory getDataDetail(String id) throws Exception {
        String hql=" from FrontSearchCategory where status <> '-1' and id = '" +id+ "'";
        List<FrontSearchCategory> frontSearchCategoryList = createQuery(FrontSearchCategory.class, hql, new ArrayList<>()).getResultList();
        if(frontSearchCategoryList!=null && frontSearchCategoryList.size()>0){
            return frontSearchCategoryList.get(0);
        }else{
            throw new Exception("该搜索对象不存在！");
        }
    }

    /**
     * 根据类别id获取分类类型字段信息
     * @param categoryId
     * @return
     */
    public List<FrontSearchCategoryField> getDataFields(String categoryId) {
        String hql="from FrontSearchCategoryField where status <> '-1' and categoryId = '" +categoryId+ "'";
        return createQuery(FrontSearchCategoryField.class, hql, new ArrayList<>()).getResultList();

    }
}
