package com.dch.facade;

import com.dch.entity.DrugClass;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品类别维护
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugClassFacade extends BaseFacade{

    @Transactional
    public DrugClass mergeDrugclass(DrugClass drugClass) throws Exception{
        if(!"-1".equals(drugClass.getStatus())){
            List<DrugClass> drugClassList = getDrugClassByParams(drugClass.getParentId(),drugClass.getClassName(),drugClass.getId());
            if(drugClassList!=null && !drugClassList.isEmpty()){
                throw new Exception("药品分类名称不能重复，请重新添加");
            }
        }else{
            List<DrugClass> drugClassList = getChildDrugClassList(drugClass.getParentId());
            if(drugClassList!=null && !drugClassList.isEmpty()){
                throw new Exception("药品类别下有子分类，请先删除子分类");
            }
        }
        return merge(drugClass);
    }

    public List<DrugClass> getDrugClassByParams(String parentId, String name,String selfId){
        String hql = " from DrugClass where status<>'-1'";
        if(!StringUtils.isEmptyParam(name)){
            hql += " and className = '"+name+"'";
        }
        if(parentId==null||"".equals(parentId)){
            hql += " and (parentId is null or parentId='')";
        }
        if(!StringUtils.isEmptyParam(selfId)){
            hql += " and id <> '"+selfId+"'";
        }
        return createQuery(DrugClass.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据父id获取药品子类别信息
     * @param parentId
     * @return
     */
    public List<DrugClass> getChildDrugClassList(String parentId){
        String hql = " from DrugClass where status<>'-1'";
        if(parentId==null||"".equals(parentId)){
            hql += " and (parentId is null or parentId='')";
        }else{
            hql += " and parentId = '"+parentId+"'";
        }
        return createQuery(DrugClass.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据类别名称和父id查询药品分类信息
     * @param className
     * @param parentId
     * @return
     */
    public List<DrugClass> getDrugClasses(String className, String parentId) {
        String hql = " from DrugClass where status<>'-1'";
        if(!StringUtils.isEmptyParam(className)){
            hql += " and className like '%"+className+"%'";
        }
        if(parentId==null||"".equals(parentId)){
            hql += " and (parentId is null or parentId='')";
        }else if(!"ALL".equalsIgnoreCase(parentId)){
            hql +=" and parentId = '"+parentId+"'";
        }
        return createQuery(DrugClass.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据ID获取单一药品类别
     * @param classId
     * @return
     * @throws Exception
     */
    public DrugClass getDrugClass(String classId) throws Exception{
        String hql = " from DrugClass where status<>'-1' and id = '"+classId+"'";
        List<DrugClass> drugClassList = createQuery(DrugClass.class,hql,new ArrayList<Object>()).getResultList();
        if(drugClassList!=null && !drugClassList.isEmpty()){
            return drugClassList.get(0);
        }else{
            throw new Exception("药品类别信息不存在");
        }
    }
}
