package com.dch.facade;

import com.dch.entity.PanFileType;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
@Component
public class PanFileTypeFacade extends BaseFacade {

    /**
     * 添加、修改、删除文件类型
     * @param panFileType
     * @return
     * @throws Exception
     */
    @Transactional
    public Response mergePanFileType(PanFileType panFileType)throws Exception{

       String status  =panFileType.getStatus();
       if(!"-1".equals(status)){
           String Name=panFileType.getTypeName();
           String hql=" from PanFileType where status<> '-1' and typeName='"+Name+"' and id<>'"+panFileType.getId()+"'";
           List<PanFileType> list=createQuery(PanFileType.class,hql,new ArrayList<>()).getResultList();
           if(list!=null&&!list.isEmpty()){
               throw new Exception("文件名称类型已存在！");
           }
           PanFileType merge = merge(panFileType);
           return Response.status(Response.Status.OK).entity(merge).build();
       }
        PanFileType merge =merge(panFileType);
       return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取文件类型
     * @param id
     * @param typeName
     * @return
     * @throws Exception
     */
    public PanFileType getPanFileType(String id,String typeName)throws Exception{
        if(StringUtils.isEmptyParam(id)&&StringUtils.isEmptyParam(typeName)){
            throw new Exception("参数不可为空！");
        }
        String hql="from PanFileType where status<> '-1' ";
        if(null!=id&&!"".equals(id)){
            hql+=" and id='"+id+"' ";
        }
        if(null!=typeName&&!"".equals(typeName)){
            hql+=" and typeName='"+typeName+"' ";
        }
        List<PanFileType> typeList=createQuery(PanFileType.class,hql,new ArrayList<>()).getResultList();
        if(typeList!=null&& !typeList.isEmpty()){
            return typeList.get(0);
        }else{
            throw new Exception("该文件名称类型不存在。");
        }
    }

    /**
     * 获取所有文件类型
     * @param typeName
     * @return
     */
    public List<PanFileType> getPanFileTypes(String typeName){
        String hql="from PanFileType where status<> '-1' ";
        if( null!=typeName&&!"".equals(typeName)){
            hql+="and typeName like '%"+typeName+"%' ";
        }
        return createQuery(PanFileType.class,hql,new ArrayList<>()).getResultList();
    }
}
