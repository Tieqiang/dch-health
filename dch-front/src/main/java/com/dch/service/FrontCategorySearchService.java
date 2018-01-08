package com.dch.service;

import com.dch.entity.DrugAd;
import com.dch.entity.DrugBaseInfo;
import com.dch.entity.FrontSearchCategory;
import com.dch.entity.FrontSearchCategoryField;
import com.dch.facade.BaseSolrFacade;
import com.dch.facade.FrontCategorySearchFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.vo.PageParam;
import com.dch.vo.SolrVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("front/category")
@Produces("application/json")
@Controller
public class FrontCategorySearchService {

    @Autowired
    private FrontCategorySearchFacade frontCategorySearchFacade;

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 查询一级分类信息
     * @return
     */
    @GET
    @Path("get-first-categorys")
    public List<FrontSearchCategory> getFrontFirstCategorys() throws Exception {
        return frontCategorySearchFacade.getFrontFirstCategorys();
    }

    @GET
    @Path("get-categorys")
    public List<FrontSearchCategory> getFrontCategorys(){
        return frontCategorySearchFacade.getFrontCategorys();
    }

    /**
     *根据分类id查询分类下的子分类
     * @param categoryId
     * @return
     */
    @Path("get-child-categorys")
    @GET
    public List<FrontSearchCategory> getFrontChildCategorys(@QueryParam("categoryId") String categoryId) throws Exception {
        return frontCategorySearchFacade.getFrontChildCategorys(categoryId);
    }

    /**
     * 根据关键字进行分类信息查询
     * @param pageParam
     * @return
     * @throws Exception
     */
    @POST
    @Path("get-categorys-by-keywords")
    public  Page<SolrVo> getFrontCategorysByKeyWords(PageParam pageParam) throws Exception {
        return frontCategorySearchFacade.getFrontCategorysByKeyWords(pageParam.getCategoryCode(),pageParam.getKeyWords(),pageParam.getExact(),pageParam.getPerPage(),pageParam.getCurrentPage());
    }

    @GET
    @Path("get-categorys-by-keyword")
    public  Page<SolrVo> getFrontCategorysByKeyWord(@QueryParam("code")String code) throws Exception {
        return frontCategorySearchFacade.getFrontCategorysByKeyWord(code);
    }

    /**
     * 初始化solr索引库
     * @return
     *
     */
    @GET
    @Path("init-solr-index")
    public List<String> initSolrIndex(){
        List<String> ids = new ArrayList<>();
        String hql = "from DrugAd where status<>'-1' ";
        List<DrugAd> drugAdList = frontCategorySearchFacade.createQuery(DrugAd.class,hql,new ArrayList<Object>()).getResultList();
        for(int i=0;i<drugAdList.size();i++){
            DrugAd drugAds = drugAdList.get(i);
            SolrVo solrVo = new SolrVo();
            solrVo.setTitle(drugAds.getDrugName());
            solrVo.setDesc(drugAds.getDrugName()+","+drugAds.getAdType()+","+drugAds.getAdNo());
            solrVo.setCategory(PinYin2Abbreviation.cn2py(drugAds.getDrugName()));
            solrVo.setCategoryCode("ypgg003");
            solrVo.setLabel(drugAds.getAdType());
            solrVo.setId(drugAds.getId());
            baseSolrFacade.addObjectMessageToMq(solrVo);
            ids.add(drugAds.getId());
        }
        return ids;
    }
    /**
     * 初始化solr索引库
     * @return
     */
    @GET
    @Path("init-solr-drug-base-infos")
    public List<String> initSolrBaseInfo(){
        List<String> ids = new ArrayList<>();
        String hql = "from DrugBaseInfo where status<>'-1' ";
        List<DrugBaseInfo> drugBaseInfoList = frontCategorySearchFacade.createQuery(DrugBaseInfo.class,hql,new ArrayList<Object>()).getResultList();
        for(int i=0;i<drugBaseInfoList.size();i++){
            DrugBaseInfo baseInfo = drugBaseInfoList.get(i);
            SolrVo solrVo = new SolrVo();
            solrVo.setTitle(baseInfo.getDrugName());
            solrVo.setDesc(baseInfo.getDrugName()+","+baseInfo.getClassName()+","+baseInfo.getToxi());
            solrVo.setCategory(PinYin2Abbreviation.cn2py(baseInfo.getDrugName()));
            solrVo.setId(baseInfo.getId());
            solrVo.setLabel(baseInfo.getClassName());
            solrVo.setCategoryCode("ywjbxx001");
            baseSolrFacade.addObjectMessageToMq(solrVo);
            ids.add(solrVo.getId());
        }
        return ids;
    }


    /**
     * 保存分类信息
     * @param frontSearchCategory
     * @return
     */
    @POST
    @Path("merge-category")
    @Transactional
    public FrontSearchCategory mergeCategorySearch(FrontSearchCategory frontSearchCategory){
        FrontSearchCategory merge = frontCategorySearchFacade.merge(frontSearchCategory);
        return merge;
    }

    /**
     * 保存分类字段
     * @param field
     * @return
     */
    @POST
    @Transactional
    @Path("merge-category-fields")
    public FrontSearchCategoryField mergeCategorySearchFields(FrontSearchCategoryField field){
        FrontSearchCategoryField merge = frontCategorySearchFacade.merge(field);
        return merge;
    }

    /**
     * 根据id查询索引的数据信息
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-solrvo-by-id")
    public  SolrVo getSolrVoById(@QueryParam("id")String id) throws Exception {
        return frontCategorySearchFacade.getSolrVoById(id);
    }
}
