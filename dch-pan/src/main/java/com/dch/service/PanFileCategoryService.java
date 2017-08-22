package com.dch.service;

import com.dch.entity.PanFileCategory;
import com.dch.facade.PanFileCategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
@Produces("application/json")
@Path("pan/file-category")
@Controller
public class PanFileCategoryService {

    @Autowired
    private PanFileCategoryFacade panFileCategoryFacade ;

    /**
     * 添加、删除、修改文件分类 删除为逻辑删除 修改status状态为-1
     * 如果被删除的分类拥有子类，则不允许删除，报异常提示先删除子类
     * @param panFileCategory
     * @return
     */
    @POST
    @Path("merge-file-category")
    @Transactional
    public Response mergePanFileCategory(PanFileCategory panFileCategory) throws Exception{
        return panFileCategoryFacade.mergePanFileCategory(panFileCategory);
    }

    /**
     * 根据分类父类ID获取子分类信息
     * 如果parentId为空则返回所有的一级分类，如果不为空，则返回该分类下的子分类
     * @param parentId
     * @return
     */
    @GET
    @Path("get-categorys-by-parent-id")
    public List<PanFileCategory> getPanFileCategoryByParentId(@QueryParam("parentId") String parentId){
        return panFileCategoryFacade.getPanFileCategoryByParentId(parentId);
    }

    /**
     * 获取所有没有删除的分类
     * @return
     */
    @GET
    @Path("get-categorys")
    public List<PanFileCategory> getPanFileCategorys(){
        return panFileCategoryFacade.getPanFileCategorys();
    }

    /**
     * 获取具体的分类
     * @param id
     * @return
     */
    @GET
    @Path("get-category")
    public PanFileCategory getPanFileCategory(@QueryParam("id") String id) throws Exception{
        return panFileCategoryFacade.getPanFileCategory(id);
    }
}
