package com.dch.facade;

import com.dch.entity.CmsCategory;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

@Component
public class CmsCategoryFacade extends BaseFacade {



    public List<CmsCategory> getCategorys() {
        String hql = "from CmsCategory as c where c.status<>'-1'" ;
        return createQuery(CmsCategory.class,hql,new ArrayList<Object>()).getResultList();
    }

    public List<CmsCategory> getChildrenCategorys(String parentCategoryId, String wherehql) {
        String hql = "from CmsCategory as c where c.status<> '-1'" ;
        if(parentCategoryId!=null&&!"".equals(parentCategoryId)){
            hql+=" and c.prentCategoryId='"+parentCategoryId+"'" ;
        }
        if(wherehql!=null&&!"".equals(wherehql)){
            hql+=wherehql ;
        }
        return createQuery(CmsCategory.class,hql,new ArrayList<Object>()).getResultList();
    }

    public CmsCategory getCategory(String categoryId) throws Exception {
        if(null==categoryId||"".equals(categoryId)){
            throw new Exception("参数不能为空");
        }
        CmsCategory cmsCategory = get(CmsCategory.class, categoryId);
        if(cmsCategory!=null){
            if("-1".equals(cmsCategory.getStatus())){
                throw new Exception("分类已经被删除");
            }
        }
        return cmsCategory;
    }
}
