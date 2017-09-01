package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FrontCategorySearchFacade extends BaseFacade {

    @Autowired
    private BaseSolrFacade baseSolrFacade;


    /**
     * 查询一级分类信息
     * @return
     */
    public List<FrontSearchCategory> getFrontFirstCategorys() throws Exception {
        String hql=" from FrontSearchCategory where status <> '-1' and parentId is null";
        return createQuery(FrontSearchCategory.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     *根据分类id查询分类下的子分类
     * @param categoryId
     * @return
     */
    public List<FrontSearchCategory> getFrontChildCategorys(String categoryId) throws Exception {
        String hql=" from FrontSearchCategory where status <> '-1' and parentId ='"+categoryId+"'";
        return createQuery(FrontSearchCategory.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 根据关键字进行分类信息查询
     * @param categoryId
     * @param keyWords
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    public Page<SolrVo> getFrontCategorysByKeyWords(String categoryId, String keyWords, int perPage, int currentPage) throws Exception {
        if(StringUtils.isEmptyParam(categoryId) && StringUtils.isEmptyParam(keyWords)){
            throw new Exception("参数为空！");
        }
        String param="";
        if(categoryId!=null && !"".equals(categoryId)){
            param +=  "content_keywords:"+categoryId+"AND";
        }

        if(keyWords!=null && !"".equals(keyWords)){
            param +=  "content_keywords:"+keyWords;
        }
        String hl="title,label,desc";
        return baseSolrFacade.getSolrObjectByParamAndPageParm(param,hl,perPage,currentPage,SolrVo.class);
    }
}
