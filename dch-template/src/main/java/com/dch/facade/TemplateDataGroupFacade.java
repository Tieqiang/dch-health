package com.dch.facade;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataGroup;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Component
public class TemplateDataGroupFacade extends BaseFacade{
    /**
     *添加、删除、修改表单元数据组
     * @param templateDataGroup
     * @return
     */
    @Transactional
    public Response mergeTemplateDataGroup(TemplateDataGroup templateDataGroup) throws Exception{
        if("-1".equals(templateDataGroup.getStatus())){//删除元数据组 检查表单元数据是否关联有元数据组
            List<TemplateDataElement> templateDataElementList = getTemplateDataElementsByGroupId(templateDataGroup.getId());
            if(templateDataElementList!=null && !templateDataElementList.isEmpty()){
                throw new Exception("元数据组下有关联的表单元数据，请先删除表单元数据");
            }
        }
        TemplateDataGroup merge = merge(templateDataGroup);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据元数据组id获取表单元数据
     * @param groupId
     * @return
     */
    public List<TemplateDataElement> getTemplateDataElementsByGroupId(String groupId){
        String hql = " from TemplateDataElement where status<>'-1' and dataGroupId = '"+groupId+"'";
        return createQuery(TemplateDataElement.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *根据表单模板id查询元数据组
     * @param templateId
     * @return
     */
    public List<TemplateDataGroup> getTemplateGroups(String templateId) {
        String hql = " from TemplateDataGroup where status<>'-1' and templateId = '"+templateId+"'";
        return createQuery(TemplateDataGroup.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *获取表单元具体的元数据组
     * @param groupId
     * @return
     * @throws Exception
     */
    public TemplateDataGroup getTemplateGroup(String groupId) throws Exception{
        String hql = " from TemplateDataGroup where status<>'-1' and id = '"+groupId+"'";
        List<TemplateDataGroup> templateDataGroupList = createQuery(TemplateDataGroup.class,hql,new ArrayList<Object>()).getResultList();
        if(templateDataGroupList!=null && !templateDataGroupList.isEmpty()){
            return templateDataGroupList.get(0);
        }else{
            throw new Exception("该元数据组信息不存在");
        }
    }
}
