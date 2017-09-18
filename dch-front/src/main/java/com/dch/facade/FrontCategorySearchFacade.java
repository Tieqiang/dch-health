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
     * @param categoryCode
     * @param keyWords
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    public Page<SolrVo> getFrontCategorysByKeyWords(String categoryCode, String keyWords, int perPage, int currentPage) throws Exception {
        Page<SolrVo> solrVoPage = null;
        try {
            String param = "";
            if (StringUtils.isEmptyParam(categoryCode) && StringUtils.isEmptyParam(keyWords)) {
                throw new Exception("参数为空！");
            }
            if (categoryCode != null && !"".equals(categoryCode)) {
                param += "categoryCode:" + categoryCode ;
            }

            keyWords = StringUtils.remeveHtmlLabel(keyWords);
            if (keyWords != null && !"".equals(keyWords)) {
                param += " AND categorykeywords:" + keyWords;
            }
            String hl = "title,desc,label";
            solrVoPage = baseSolrFacade.getSolrObjectByParamAndPageParm(param, hl, perPage, currentPage, SolrVo.class);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return solrVoPage;
    }


    public List<FrontSearchCategory> getFrontCategorys() {

        String hql = "from FrontSearchCategory a where a.status<> '-1'" ;
        List<FrontSearchCategory> list = createQuery(FrontSearchCategory.class, hql, new ArrayList<Object>()).getResultList();
        return list;
    }
}
