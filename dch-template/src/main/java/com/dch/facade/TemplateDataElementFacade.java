package com.dch.facade;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataValue;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplateDataElementFacade extends BaseFacade {

    /**
     * 添加、删除、修改元数据
     * @param templateDataElement
     * @return
     */
    public Response mergeTemplateDataElement(TemplateDataElement templateDataElement) throws Exception{
        if(!"-1".equals(templateDataElement.getStatus())){
            if(ifExistDataCode(templateDataElement)){
                throw new Exception("表单元数据编码已存在,请重新填写");
            }
        }
        TemplateDataElement merge = merge(templateDataElement);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 判断表单元数据编码是否存在重复记录
     * @param templateDataElement
     * @return
     */
    public boolean ifExistDataCode(TemplateDataElement templateDataElement){
        boolean isExist = false;
        String hql = "select dataElementCode from TemplateDataElement where status<>'-1' and id<>'"+templateDataElement.getId()+"' and dataElementCode = '"+templateDataElement.getDataElementCode()+"'";
        List<String> codeList = createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(codeList!=null && !codeList.isEmpty()){
            isExist = true;
        }
        return isExist;
    }
    /**
     * 获取元数据
     * @param groupId
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateDataElement> getTemplateDataElements(String groupId,String dataElementName, String templateId, int perPage, int currentPage) {
        String hql=" from TemplateDataElement where status <> '-1' ";
        String hqlCount="select count(*) from TemplateDataElement where status <> '-1' ";

        if(groupId!=null&&!"".equals(groupId)){
            hql+="and dataGroupId = '"+groupId+"' ";
            hqlCount+="and dataGroupId = '"+groupId+"' ";
        }else{
            if(templateId!=null&&!"".equals(templateId)){
                hql+="and templateId = '"+templateId+"' and dataGroupId is null";
                hqlCount+="and templateId = '"+templateId+"' and dataGroupId is null";
            }
        }

        if(dataElementName!=null && !"".equals(dataElementName)){
            hql+=" and dataElementName like '%"+dataElementName+"%' ";
            hqlCount+=" and dataElementName like '%"+dataElementName+"%' ";
        }
        TypedQuery<TemplateDataElement> query = createQuery(TemplateDataElement.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateDataElement> templateDataElementList = query.getResultList();
        page.setCounts(counts);
        page.setData(templateDataElementList);
        return page;
    }

    /**
     * 添加、修改、删除元数据值域
     * @param templateDataValue
     * @return
     */
    public Response mergeTemplateDataValue(TemplateDataValue templateDataValue) {
        TemplateDataValue merge = merge(templateDataValue);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取元数据的值域
     * @param elementId
     * @return
     */
    public List<TemplateDataValue> getTemplateDataValues(String elementId) {
        String hql=" from TemplateDataValue where status <> '-1' and dataElementId = '"+elementId+"'";
        return createQuery(TemplateDataValue.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据传入的元数据值域集合进行保存
     * @param templateDataValues
     * @return
     */
    @Transactional
    public Response mergeTemplateDataValues(List<TemplateDataValue> templateDataValues) throws Exception{
        List<TemplateDataValue> templateDataValueList = new ArrayList<TemplateDataValue>();
        if(templateDataValues!=null && !templateDataValues.isEmpty()){
            String hql = "delete from TemplateDataValue where dataElementId = '"+templateDataValues.get(0).getDataElementId()+"'";
            excHql(hql);
        }
        Map<String,String> nameMap = new HashMap<String,String>();
        for(TemplateDataValue templateDataValue:templateDataValues){
            if(nameMap.containsKey(templateDataValue.getDataValueName())){
                throw new Exception("元数据值域名称"+templateDataValue.getDataValueName()+"不允许重复");
            }else {
                nameMap.put(templateDataValue.getDataValueName(),templateDataValue.getDataValueName());
            }
        }
        for(TemplateDataValue templateDataValue:templateDataValues){
            templateDataValueList.add(merge(templateDataValue));
        }
        return Response.status(Response.Status.OK).entity(templateDataValueList).build();
    }
}
