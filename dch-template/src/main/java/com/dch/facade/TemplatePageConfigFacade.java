package com.dch.facade;

import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateDataValue;
import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplatePage;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.DataElementVO;
import com.dch.vo.ElementVO;
import com.dch.vo.PageVO;
import com.dch.vo.TemplatePageConfigVo;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */
@Component
public class TemplatePageConfigFacade extends BaseFacade {
    @Transactional
    public Response mergeTemplatePageConfig(TemplatePageConfigVo vo){
        try{
            //1,删除所有与该表页相关的元数据、数据值域
            deleteAllRelatedTemplateDataElement(vo.getPageId(),vo.getElements());
            //2，更新当前页config 和content字段
            if(!StringUtils.isEmptyParam(vo.getPageId())){
                String upHql = "update TemplatePage set config = '"+vo.getConfig()+"',templatePageContent = '"+vo.getDocument()+"',templatePageDataModel=" +
                        "'"+vo.getTemplatePageDataModel()+"' where " +
                        " id = '"+vo.getPageId()+"'";
                excHql(upHql);
            }
            List<TemplateDataElement> templateDataElementList = mergeElementVOs(vo.getPageId(),vo.getElements());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(vo).build();
    }

    public void deleteAllRelatedTemplateDataElement(String pageId,List<ElementVO> elementsVOs){
        if(elementsVOs!=null && !elementsVOs.isEmpty()){
            String hql = "select id from TemplateDataElement where pageId = '"+pageId+"'";
            List<String> templateDataEleIds = createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
            if(templateDataEleIds!=null && !templateDataEleIds.isEmpty()){
                //removeByStringIds(TemplateDataElement.class,templateDataEleIds);//删除相关元数据
                StringBuffer idsBuffer = new StringBuffer("");
                for(String tepmlatedId:templateDataEleIds){
                    idsBuffer.append("'").append(tepmlatedId).append("',");
                }
                String rellatedTemplateDataEleIds = idsBuffer.toString();
                rellatedTemplateDataEleIds = rellatedTemplateDataEleIds.substring(0,rellatedTemplateDataEleIds.length()-1);
                String delDataValueSql = " delete from TemplateDataValue where dataElementId in ("+rellatedTemplateDataEleIds+")";
                excHql(delDataValueSql);
                String delDataElementSql = " delete from TemplateDataElement where id in ("+rellatedTemplateDataEleIds+")";
                excHql(delDataElementSql);
            }
            for(ElementVO elementVO:elementsVOs){
                if(elementVO.getChildren()!=null && !elementVO.getChildren().isEmpty()){
                    deleteAllRelatedTemplateDataElement(pageId,elementVO.getChildren());
                }
                if(elementVO.getOtherElement()!=null && !elementVO.getOtherElement().isEmpty()){
                    deleteAllRelatedTemplateDataElement(pageId,elementVO.getOtherElement());
                }
            }
        }
    }

    public List<TemplateDataElement> mergeElementVOs(String pageId,List<ElementVO> elementsVOs){
        List<TemplateDataElement> elementList = new ArrayList<>();
        for (ElementVO vo:elementsVOs){
            TemplateDataElement templateDataElement = new TemplateDataElement();
            templateDataElement.setDataElementName(vo.getDataElementName());
            templateDataElement.setDataElementCode(vo.getDataElementCode());
            templateDataElement.setDataElementType(vo.getDataElementType());
            templateDataElement.setParentDataId(vo.getParentDataId());
            templateDataElement.setStatus("1");
            templateDataElement.setPageId(pageId);
            //set
            List<ElementVO> otherElement = vo.getOtherElement();
            if(otherElement!=null && !otherElement.isEmpty()){
                List<TemplateDataElement> templateDataElements = mergeElementVOs(pageId,otherElement) ;
                StringBuffer otherElementIdsBuffer = new StringBuffer("");
                for(TemplateDataElement dataElement:templateDataElements){
                    //将所有的dataElement的ID拼接起来，用逗号分开，并设置给templateDataElement新曾的字段other_element_ids
                    otherElementIdsBuffer.append(dataElement.getId()).append(",");
                }
                String otherElementIds = otherElementIdsBuffer.toString();
                otherElementIds = otherElementIds.substring(0,otherElementIds.length()-1);
                templateDataElement.setOtherElementIds(otherElementIds);
            }
            TemplateDataElement merge = merge(templateDataElement);
            if(vo.getDataValues()!=null && !vo.getDataValues().isEmpty()){
                for(TemplateDataValue templateDataValue:vo.getDataValues()){
                    templateDataValue.setDataElementId(merge.getId());
                }
                batchInsert(vo.getDataValues());
            }
            if(vo.getChildren()!=null && !vo.getChildren().isEmpty()){
                for (ElementVO child:vo.getChildren()){
                    child.setParentDataId(merge.getId());
                }
                mergeElementVOs(pageId,vo.getChildren());
            }
            elementList.add(merge) ;
        }
        return elementList;
    }

    public TemplatePageConfigVo getTemplatePageConfigVoById(String pageId) {
        TemplatePage templatePage = get(TemplatePage.class,pageId);
        TemplatePageConfigVo templatePageConfigVo = new TemplatePageConfigVo();
        if(templatePage!=null){
            templatePageConfigVo.setConfig(templatePage.getConfig());
            templatePageConfigVo.setDocument(templatePage.getTemplatePageContent());
            templatePageConfigVo.setPageId(pageId);
            templatePageConfigVo.setTemplatePageDataModel(templatePage.getTemplatePageDataModel());
            templatePageConfigVo.setTemplateId(templatePage.getTemplateId());
            List<ElementVO> elementVOS = getElementVoList(pageId);
            templatePageConfigVo.setElements(elementVOS);
        }
        return templatePageConfigVo;
    }

    public List<ElementVO> getElementVoList(String pageId){
        List<ElementVO> elementVOS = new ArrayList<>();
        String hql = " from TemplateDataElement where status<>'-1' and pageId = '"+pageId+"' and parentDataId is null and dataElementCode not like '%other%'";
        List<TemplateDataElement> templateDataElements = createQuery(TemplateDataElement.class,hql,new ArrayList<Object>()).getResultList();
        if(templateDataElements!=null && !templateDataElements.isEmpty()){
            for(TemplateDataElement templateDataElement:templateDataElements){
                ElementVO vo = new ElementVO();
                vo.setDataElementType(templateDataElement.getDataElementType());
                vo.setDataElementCode(templateDataElement.getDataElementCode());
                vo.setDataElementName(templateDataElement.getDataElementName());
                vo.setDataValues(getTemplateDataValueByElementId(templateDataElement.getId()));
                List<ElementVO> childrens = getChildRenElementVos(templateDataElement.getId());
                vo.setChildren(childrens);
                List<ElementVO> otherElements = getOtherElementVos(templateDataElement.getOtherElementIds());
                vo.setOtherElement(otherElements);
                elementVOS.add(vo);
            }
        }
        return elementVOS;
    }

    public List<ElementVO> getChildRenElementVos(String parentDataId){
        List<ElementVO> elementVOS = new ArrayList<>();
        String hql = "from TemplateDataElement where status<>'-1' and parentDataId = '"+parentDataId+"'";
        List<TemplateDataElement> templateDataElements = createQuery(TemplateDataElement.class,hql,new ArrayList<Object>()).getResultList();
        if(templateDataElements!=null && !templateDataElements.isEmpty()){
            for(TemplateDataElement templateDataElement:templateDataElements){
                ElementVO vo = new ElementVO();
                vo.setDataElementType(templateDataElement.getDataElementType());
                vo.setDataElementCode(templateDataElement.getDataElementCode());
                vo.setDataElementName(templateDataElement.getDataElementName());
                vo.setParentDataId(parentDataId);
                vo.setDataValues(getTemplateDataValueByElementId(templateDataElement.getId()));
                vo.setChildren(getChildRenElementVos(templateDataElement.getId()));
                if(!StringUtils.isEmptyParam(templateDataElement.getOtherElementIds())){
                    List<ElementVO> otherElementVos = getOtherElementVos(templateDataElement.getOtherElementIds());
                    vo.setOtherElement(otherElementVos);
                }
                elementVOS.add(vo);
            }
        }
        return elementVOS;
    }

    public List<ElementVO> getOtherElementVos(String elementIds) {
        List<ElementVO> elementVOS = new ArrayList<>();
        if(!StringUtils.isEmptyParam(elementIds)){
            String[] idsArray = elementIds.split(",");
            StringBuffer sb = new StringBuffer("");
            if (idsArray != null && idsArray.length > 0) {
                for (int i = 0; i < idsArray.length; i++) {
                    if (i != idsArray.length - 1) {
                        sb.append("'").append(idsArray[i]).append("',");
                    } else {
                        sb.append("'").append(idsArray[i]).append("'");
                    }
                }
            }
            String ids = sb.toString();
            String hql = " from TemplateDataElement where status<>'-1' and id in (" + ids + ")";
            List<TemplateDataElement> templateDataElements = createQuery(TemplateDataElement.class, hql, new ArrayList<Object>()).getResultList();
            if (templateDataElements != null && !templateDataElements.isEmpty()) {
                for (TemplateDataElement templateDataElement : templateDataElements) {
                    ElementVO vo = new ElementVO();
                    vo.setDataElementType(templateDataElement.getDataElementType());
                    vo.setDataElementCode(templateDataElement.getDataElementCode());
                    vo.setDataElementName(templateDataElement.getDataElementName());
                    vo.setDataValues(getTemplateDataValueByElementId(templateDataElement.getId()));
                    if (!StringUtils.isEmptyParam(templateDataElement.getOtherElementIds())) {
                        List<ElementVO> otherElementVos = getOtherElementVos(templateDataElement.getOtherElementIds());
                        vo.setOtherElement(otherElementVos);
                    }
                    vo.setChildren(getChildRenElementVos(templateDataElement.getId()));
                    elementVOS.add(vo);
                }
            }
        }
        return elementVOS;
    }
    /**
     * 根据元数据id获取元数据值域信息
     * @param elementId
     * @return
     */
    public List<TemplateDataValue> getTemplateDataValueByElementId(String elementId){
        String hql = " from TemplateDataValue where dataElementId = '"+elementId+"'";
        List<TemplateDataValue> templateDataValueList = createQuery(TemplateDataValue.class,hql,new ArrayList<Object>()).getResultList();
        return templateDataValueList;
    }

    /**
     *根据表单id获取表单下的表单页元数据信息
     * @param templateId
     * @return
     */
    public DataElementVO getDataElementVOByTemplateId(String templateId) {
        DataElementVO dataElementVO = new DataElementVO();
        if(!StringUtils.isEmptyParam(templateId)){
            String hql = "from TemplateMaster where id = '"+templateId+"'";
            List<TemplateMaster> templateMasterList = createQuery(TemplateMaster.class,hql,new ArrayList<Object>()).getResultList();
            if(templateMasterList!=null && !templateMasterList.isEmpty()){
                dataElementVO.setTemplateMaster(templateMasterList.get(0));
            }
            String pageHql = "from TemplatePage where status<>'-1' and templateId = '"+templateId+"'";
            List<TemplatePage> templatePageList = createQuery(TemplatePage.class,pageHql,new ArrayList<Object>()).getResultList();
            List<PageVO> pageVOS = new ArrayList<>();
            if(templatePageList!=null && !templatePageList.isEmpty()){
                for(TemplatePage templatePage:templatePageList){
                    PageVO pageVO = new PageVO();
                    pageVO.setTemplatePage(templatePage);
                    pageVO.setElementVOS(getElementVoList(templatePage.getId()));
                    pageVOS.add(pageVO);
                }
            }
            dataElementVO.setPageVOS(pageVOS);
        }
        return dataElementVO;
    }
}
