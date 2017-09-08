package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.DrugAdVo;
import com.dch.vo.DrugBaseInfoVo;
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
     *
     * @param type
     * @param categoryId
     * @param keyWords
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    public <T> Page<T> getFrontCategorysByKeyWords(String type, String categoryId, String keyWords, int perPage, int currentPage) throws Exception {
        if(StringUtils.isEmptyParam(type)) "1".equals(type);
        String param = "";
        Page<T> voPage =null;
        if(type == "1" ||"1".equals(type)) {


            if (StringUtils.isEmptyParam(categoryId) && StringUtils.isEmptyParam(keyWords)) {
                throw new Exception("参数为空！");
            }
            if (categoryId != null && !"".equals(categoryId)) {
                param += "baseInfoKeywords:" + categoryId + "AND";
            }

            if (keyWords != null && !"".equals(keyWords)) {
                param += "baseInfoKeywords:" + keyWords;
            }
            String hl = "drugName,drugCode,className,toxi";
            voPage = (Page<T>) baseSolrFacade.getSolrObjectByParamAndPageParm(param, hl, perPage, currentPage, DrugBaseInfoVo.class);

        }
        if(type == "2" || "2".equals(type)) {
            if (StringUtils.isEmptyParam(categoryId) && StringUtils.isEmptyParam(keyWords)) {
                throw new Exception("参数为空！");
            }
            if (categoryId != null && !"".equals(categoryId)) {
                param += "drugAdKeywords:" + categoryId + "AND";
            }

            if (keyWords != null && !"".equals(keyWords)) {
                param += "drugAdKeywords:" + keyWords;
            }
            String hl = "drugName,drugCode";
            voPage = (Page<T>) baseSolrFacade.getSolrObjectByParamAndPageParm(param, hl, perPage, currentPage, DrugAdVo.class);

        }
        return voPage;
    }
}
