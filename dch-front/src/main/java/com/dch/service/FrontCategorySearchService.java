package com.dch.service;

import com.dch.entity.DrugAd;
import com.dch.entity.DrugBaseInfo;
import com.dch.entity.FrontSearchCategory;
import com.dch.facade.BaseSolrFacade;
import com.dch.facade.FrontCategorySearchFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugAdVo;
import com.dch.vo.DrugBaseInfoVo;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
     * @param categoryCode
     * @param keyWords
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    @Path("get-categorys-by-keywords")
    @GET
    public  Page<SolrVo> getFrontCategorysByKeyWords(@QueryParam("categoryCode") String categoryCode,
                                                    @QueryParam("keyWords") String keyWords,
                                                    @QueryParam("perPage") int perPage,
                                                    @QueryParam("currentPage") int currentPage) throws Exception {
        return frontCategorySearchFacade.getFrontCategorysByKeyWords(categoryCode,keyWords,perPage,currentPage);
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
            solrVo.setCategory(drugAds.getAdType());
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
            solrVo.setCategory(baseInfo.getDrugCategory());
            solrVo.setId(baseInfo.getId());
            solrVo.setLabel(baseInfo.getClassName());
            solrVo.setCategoryCode("ywjbxx001");
            baseSolrFacade.addObjectMessageToMq(solrVo);
            ids.add(solrVo.getId());
        }
        return ids;
    }

}
