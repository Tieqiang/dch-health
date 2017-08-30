package com.dch.service;

import com.dch.entity.FrontSearchCategory;
import com.dch.facade.FrontCategorySearchFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("front/category")
@Produces("application/json")
@Controller
public class FrontCategorySearchService {
    @Autowired
    private FrontCategorySearchFacade frontCategorySearchFacade;

    /**
     * 查询一级分类信息
     * @return
     */
    @GET
    @Path("get-first-categorys")
    public List<DrugCommonVo> getFrontFirstCategorys() throws Exception {
        return frontCategorySearchFacade.getFrontFirstCategorys();
    }

    /**
     *根据分类id查询分类下的子分类
     * @param categoryId
     * @return
     */
    @Path("get-child-categorys")
    @GET
    public List<DrugCommonVo> getFrontChildCategorys(@QueryParam("categoryId") String categoryId) throws Exception {

        return frontCategorySearchFacade.getFrontChildCategorys(categoryId);
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
    @Path("get-categorys-by-keywords")
    @GET
    public Page<DrugCommonVo> getFrontCategorysByKeyWords(@QueryParam("categoryId") String categoryId,
                                                          @QueryParam("keyWords") String keyWords,
                                                          @QueryParam("perPage") int perPage,
                                                          @QueryParam("currentPage") int currentPage) throws Exception {
        return frontCategorySearchFacade.getFrontCategorysByKeyWords(categoryId,keyWords,perPage,currentPage);
    }
}
