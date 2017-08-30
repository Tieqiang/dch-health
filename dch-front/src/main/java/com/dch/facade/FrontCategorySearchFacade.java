package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugCommonVo;
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
    public List<DrugCommonVo> getFrontFirstCategorys() throws Exception {

       return baseSolrFacade.searchDrugCommonVosById("");
    }

    /**
     *根据分类id查询分类下的子分类
     * @param categoryId
     * @return
     */
    public List<DrugCommonVo> getFrontChildCategorys(String categoryId) throws Exception {
        return baseSolrFacade.searchDrugCommonVosById(categoryId);
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
    public Page<DrugCommonVo> getFrontCategorysByKeyWords(String categoryId, String keyWords, int perPage, int currentPage) throws Exception {
       return baseSolrFacade.searchChildDrugCommonVos(categoryId,keyWords,perPage,currentPage);
    }
}
