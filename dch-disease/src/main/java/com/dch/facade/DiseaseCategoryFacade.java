package com.dch.facade;

import com.dch.entity.DiseaseCategoryDict;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/9/13.
 */
@Service
public class DiseaseCategoryFacade extends BaseFacade {

    /**
     * 查询分类列表
     * @param categoryName 分类名称，进行模糊匹配
     * @param parentId     父分类ID，根据此字段查询子分类
     * @return
     */
    public List<DiseaseCategoryDict> getDiseaseCategorys(String categoryName, String parentId) {

        String hql = "from DiseaseCategoryDict as d where d.status<> '-1' " ;
        if (!StringUtils.isEmptyParam(parentId)){
            hql +=" and d.parentId='"+parentId+"'" ;
        }

        if(!StringUtils.isEmptyParam(categoryName)){
            hql +=" and d.categoryName like '%"+categoryName+"%'" ;
        }
        List<DiseaseCategoryDict> diseaseCategoryDicts = createQuery(DiseaseCategoryDict.class, hql, new ArrayList<Object>()).getResultList();
        return diseaseCategoryDicts;
    }

    /**
     * 添加、删除、修改分类
     * @param diseaseCategoryDict
     * @return
     */
    @Transactional
    public DiseaseCategoryDict mergeDiseaseCategory(DiseaseCategoryDict diseaseCategoryDict) {
        DiseaseCategoryDict merge = merge(diseaseCategoryDict);
        return merge;
    }



}
