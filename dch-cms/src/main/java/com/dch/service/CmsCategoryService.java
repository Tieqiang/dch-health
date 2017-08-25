package com.dch.service;

import com.dch.entity.CmsCategory;
import com.dch.facade.CmsCategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Controller
@Path("cms/category")
@Produces("application/json")
public class CmsCategoryService {

    @Autowired
    private CmsCategoryFacade cmsCategoryFacade ;

    /**
     * 添加、删除、修改分类
     * @param cmsCategory
     * @return
     */
    @POST
    @Path("merge-category")
    @Transactional
    public CmsCategory mergeCategory(CmsCategory cmsCategory){
        return cmsCategoryFacade.merge(cmsCategory);
    }

    /**
     * 获取所有的分类
     * @return
     */
    @GET
    @Path("get-all-categorys")
    public List<CmsCategory> getCategorys(){
        return cmsCategoryFacade.getCategorys();
    }

    /**
     * 获取子分类
     * @param parentCategoryId
     * @param wherehql
     * @return
     */
    @GET
    @Path("get-children-categorys")
    public List<CmsCategory> getChildrenCategorys(@QueryParam("parentCategoryId") String parentCategoryId,
                                                  @QueryParam("wherehql") String wherehql){
        return cmsCategoryFacade.getChildrenCategorys(parentCategoryId,wherehql);
    }

    /**
     * 获取具体的分类
     * @param categoryId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-category")
    public CmsCategory getCategory(@QueryParam("categoryId") String categoryId) throws Exception {
        return cmsCategoryFacade.getCategory(categoryId);
    }

}
