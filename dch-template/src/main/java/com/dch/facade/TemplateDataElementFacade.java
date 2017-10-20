package com.dch.facade;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataValue;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateDataElementVo;
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
        if(StringUtils.isEmptyParam(templateDataElement.getId())){
            templateDataElement.setDataElementCode(getRealCode(templateDataElement.getParentDataId(),"",templateDataElement.getId(),templateDataElement.getDataElementName()));
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
    public Page<TemplateDataElementVo> getTemplateDataElements(String groupId,String dataElementName, String templateId, int perPage, int currentPage) {
        String hql="select new com.dch.vo.TemplateDataElementVo(t.id,t.dataElementName," +
                "t.dataElementCode,t.parentDataId,t.dataStandRefId,(select dataElementName from DataElement where id = t.dataStandRefId) as dataStandRefName" +
                ",t.dataElementType,t.dataUnion,t.childrenFlag,t.dataGroupId,t.hasGroupId,t.templateId," +
                "t.createDate,t.modifyDate,t.createBy,t.modifyBy,t.status) from TemplateDataElement as t where t.status <> '-1' ";
        String hqlCount="select count(*) from TemplateDataElement t where t.status <> '-1' ";

        if(groupId!=null&&!"".equals(groupId)){
            hql+="and t.dataGroupId = '"+groupId+"' ";
            hqlCount+="and t.dataGroupId = '"+groupId+"' ";
        }else{
            if(templateId!=null&&!"".equals(templateId)){
                hql+="and t.templateId = '"+templateId+"' and t.dataGroupId is null";
                hqlCount+="and t.templateId = '"+templateId+"' and t.dataGroupId is null";
            }
        }

        if(dataElementName!=null && !"".equals(dataElementName)){
            hql+=" and t.dataElementName like '%"+dataElementName+"%' ";
            hqlCount+=" and t.dataElementName like '%"+dataElementName+"%' ";
        }
        TypedQuery<TemplateDataElementVo> query = createQuery(TemplateDataElementVo.class, hql, new ArrayList<Object>());
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
        List<TemplateDataElementVo> templateDataElementVoList = query.getResultList();
        page.setCounts(counts);
        page.setData(templateDataElementVoList);
        StringBuffer templateIds = new StringBuffer("");
        if(templateDataElementVoList!=null && !templateDataElementVoList.isEmpty()){
            for(TemplateDataElementVo templateDataElementVo:templateDataElementVoList){
                templateIds.append("'").append(templateDataElementVo.getId()).append("',");
            }
        }
        String ids = templateIds.toString();
        if(!StringUtils.isEmptyParam(ids)){
            ids = ids.substring(0,ids.length()-1);
            Map<String,List<TemplateDataValue>> templateDataValueMap = getTemplateDataValuesByIds(ids);
            for(TemplateDataElementVo templateDataElementVo:templateDataElementVoList){
                if(templateDataValueMap.get(templateDataElementVo.getId())!=null){
                    templateDataElementVo.setTemplateDataValueList(templateDataValueMap.get(templateDataElementVo.getId()));
                }
            }
        }
        return page;
    }

    public Map<String,List<TemplateDataValue>> getTemplateDataValuesByIds(String ids){
        Map<String,List<TemplateDataValue>> dataValueMap = new HashMap<String, List<TemplateDataValue>>();
        String hql = " from TemplateDataValue where status<>'-1' and dataElementId in ("+ids+")";
        List<TemplateDataValue> templateDataValueList = createQuery(TemplateDataValue.class,hql,new ArrayList<Object>()).getResultList();
        if(templateDataValueList!=null && !templateDataValueList.isEmpty()){
            for(TemplateDataValue templateDataValue:templateDataValueList){
                if(dataValueMap.containsKey(templateDataValue.getDataElementId())){
                    List<TemplateDataValue> innerList = dataValueMap.get(templateDataValue.getDataElementId());
                    innerList.add(templateDataValue);
                }else{
                    List<TemplateDataValue> innerList = new ArrayList<TemplateDataValue>();
                    innerList.add(templateDataValue);
                    dataValueMap.put(templateDataValue.getDataElementId(),innerList);
                }
            }
        }
        return dataValueMap;
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

    /**
     * 根据元数据名称和元数据id生成元数据编码
     * @param dataElementId
     * @param dataName
     * @return
     */
    public String getRealCode(String parentDataId,String parentCode,String dataElementId, String dataName) {
        String realCode = "";
        String endCode = "";
        String codeNum = "";
        String hql = "select max(dataElementCode) from TemplateDataElement where status<>'-1' and dataElementName = '"+dataName+"' and id<>'"+dataElementId+"'";
        if(!StringUtils.isEmptyParam(parentDataId)){
            hql += " and parentDataId = '"+parentDataId+"'";
        }
        List<String> list = createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty() && list.get(0) != null){
            endCode = list.get(0);
            if(endCode.contains(".")){
                String[] dataCodeArray = endCode.split("\\.");
                endCode = dataCodeArray[dataCodeArray.length-1];
            }
            if(endCode.contains("_")){
                String[] codeArray = endCode.split("_");
                if(codeArray.length>1){
                    codeNum = codeArray[codeArray.length-1];
                }
            }
            if(!StringUtils.isEmptyParam(codeNum)){
                Integer codeNumber = Integer.valueOf(codeNum)+1;
                codeNum = codeNumber+"";
            }else{
                codeNum = "01";
            }
            if(codeNum.length()<2){
                codeNum = "0"+codeNum;
            }
            realCode= PinYin2Abbreviation.cn2py(dataName)+"_"+codeNum;
        }else{
            realCode = PinYin2Abbreviation.cn2py(dataName);
        }
        if(!StringUtils.isEmptyParam(parentCode)){
            realCode = parentCode+"."+realCode;
        }else{
            String parHql = "select dataElementCode from TemplateDataElement where status<>'-1' and id = '"+parentDataId+"'";
            List<String> parList = createQuery(String.class,parHql,new ArrayList<Object>()).getResultList();
            if(parList!=null && !parList.isEmpty()){
                String parCode = parList.get(0);
                realCode = parCode+"."+realCode;
            }
        }
        return realCode;
    }
}
