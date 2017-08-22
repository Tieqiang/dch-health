package com.dch.facade;

import com.dch.entity.PanFileCategory;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
@Component
public class PanFileCategoryFacade extends BaseFacade {

    /**
     * 添加、删除、修改文件分类 删除为逻辑删除 修改status状态为-1
     * 如果被删除的分类拥有子类，则不允许删除，报异常提示先删除子类
     * @param panFileCategory
     * @return
     */
    public Response mergePanFileCategory(PanFileCategory panFileCategory) throws Exception{
        if ("-1".equals(panFileCategory.getStatus())){
            String hql = "from PanFileCategory where status <> '-1' and parentCategoryId = '" + panFileCategory.getId() + "'";
            List<PanFileCategory> panFileCategoryList = createQuery(PanFileCategory.class,hql,new ArrayList<Object>()).getResultList();
            if (panFileCategoryList != null&&!panFileCategoryList.isEmpty()){
                throw new Exception("请先删除子类");
            }else{
                PanFileCategory merge = merge(panFileCategory);
                return Response.status(Response.Status.OK).entity(merge).build();
            }
        }else{
            PanFileCategory merge = merge(panFileCategory);
            return Response.status(Response.Status.OK).entity(merge).build();
        }
    }


    /**
     * 根据分类父类ID获取子分类信息
     * 如果parentId为空则返回所有的一级分类，如果不为空，则返回该分类下的子分类
     * @param parentId
     * @return
     */
    public List<PanFileCategory> getPanFileCategoryByParentId(String parentId){
        String hql = "from PanFileCategory where status <> '-1'";
        if(parentId != null&&!"".equals(parentId)){
            hql = hql + " and parentCategoryId = '" + parentId + "'";
        }else{
            hql = hql + " and parentCategoryId is null";
        }
        List<PanFileCategory> panFileCategoryList = createQuery(PanFileCategory.class,hql,new ArrayList<Object>()).getResultList();
        return panFileCategoryList;
    }

    /**
     * 获取所有没有删除的分类
     * @return
     */
    public List<PanFileCategory> getPanFileCategorys(){
        String hql = "from PanFileCategory where status <> '-1'";
        List<PanFileCategory> panFileCategoryList = createQuery(PanFileCategory.class,hql,new ArrayList<Object>()).getResultList();
        return panFileCategoryList;
    }

    /**
     * 获取具体的分类
     * @param id
     * @return
     */
    public PanFileCategory getPanFileCategory(String id) throws Exception{
        if (id == null||"".equals(id)){
            throw new Exception("请输入id");
        }
        String hql = "from PanFileCategory where status <> '-1' and id = '" + id + "'";

        List<PanFileCategory> panFileCategoryList = createQuery(PanFileCategory.class,hql,new ArrayList<Object>()).getResultList();
        if(panFileCategoryList != null&&!panFileCategoryList.isEmpty()){
            return panFileCategoryList.get(0);
        }else{
            throw new Exception("文件分类不存在");
        }
    }
}
