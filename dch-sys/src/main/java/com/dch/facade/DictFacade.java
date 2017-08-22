package com.dch.facade;

import com.dch.entity.DictType;
import com.dch.entity.DictValue;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DictFacade extends BaseFacade {

    /**
     * 获取单个字典类型
     * @param dictTypeId
     * @return
     */
    public DictType getDictType(String dictTypeId) throws Exception{
        String hql = "from DictType where status <> '-1' ";
        if(dictTypeId != null&&!"".equals(dictTypeId)){
           hql += "and id = '" + dictTypeId + "'";
        }
        List<DictType> dictTypeList = createQuery(DictType.class,hql,new ArrayList<Object>()).getResultList();
        if (dictTypeList != null&&!dictTypeList.isEmpty()){
            return dictTypeList.get(0);
        }else{
            throw new Exception("字典类型不存在");
        }
    }

    /**
     * 获取所有字典
     * @param inputCode
     * @return
     */
    public List<DictType> getDictTypes(String inputCode){
        String hql = "from DictType where status <> '-1'";
        if(inputCode != null&&!"".equals(inputCode)){
            hql += " and upper(inputCode) like '%" + inputCode.toUpperCase() + "%'";
        }
        return createQuery(DictType.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 新增、修改、删除字典类型 删除为逻辑删除 修改status状态为-1
     * @param dictType
     * @return
     */
    @Transactional
    public Response mergeDictType(DictType dictType) throws Exception{
        String id = dictType.getId();
        if(id==null || "".equals(id)){
            List<DictType> dictTypeList = getDictTypeList(dictType.getDictName(),dictType.getDictCode());
            if(dictTypeList!=null && !dictTypeList.isEmpty()){
                throw new Exception("该字典类型信息已存在，请重试");
            }
        }
        dictType.setInputCode(PinYin2Abbreviation.cn2py(dictType.getDictName()));
        return Response.status(Response.Status.OK).entity(merge(dictType)).build();
    }

    /**
     * 根据字典名称获取字典信息
     * @param dictName 字典类型名称
     * @param dictCode
     * @return
     */
    private List<DictType> getDictTypeList(String dictName,String dictCode) {
        String hql = " from DictType where dictName='"+dictName+"' or dictCode ='"+dictCode+"'";
        return createQuery(DictType.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 新增、修改、删除字典内容 删除为逻辑删除 修改status状态为-1
     * @param dictValue
     * @return
     */
    @Transactional
    public Response mergeDictValue(DictValue dictValue) throws Exception{
        List<DictValue> dictValueList = getDictValueList(dictValue.getKeyName(),dictValue.getId()==null?"":dictValue.getId());
        if(dictValueList!=null && !dictValueList.isEmpty()){
            throw new Exception("该字典内容信息已存在，请重试");
        }
        dictValue.setInputCode(PinYin2Abbreviation.cn2py(dictValue.getKeyName()));
        return Response.status(Response.Status.OK).entity(merge(dictValue)).build();
    }

    /**
     *根据字典值获取字典内容信息
     * @param keyName 字典内容值名称
     * @param id
     * @return
     */
    public List<DictValue> getDictValueList(String keyName,String id){
        String hql = " from DictValue where keyName = '"+keyName+"' and id <> '"+id+"'";
        return createQuery(DictValue.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取某个字典内容
     * @param dictId
     * @return
     */
    public DictValue getDictValue(String dictId) throws Exception{
        String hql = "from DictValue where status <> '-1'";
        if (dictId != null&&!"".equals(dictId)){
            hql += " and id = '" + dictId + "'";
        }
        List<DictValue> dictValueList = createQuery(DictValue.class,hql,new ArrayList<Object>()).getResultList();
        if(dictValueList != null&&!dictValueList.isEmpty()){
            return dictValueList.get(0);
        }else{
            throw new Exception("字典内容不存在");
        }
    }

    /**
     * 根据拼音码过滤字典内容
     * @param dictTypeId
     * @param inputCode
     * @return
     */
    public List<DictValue> getDictValues(String dictTypeId,String inputCode){
        String hql = "select dv from DictValue as dv,DictType as dt where dt.id = dv.dictTypeId " +
                "and dv.status <> '-1'" ;
        if(dictTypeId != null&&!"".equals(dictTypeId)){
            hql +=  " and dt.id = '" + dictTypeId + "'";
        }
        if(inputCode != null&&!"".equals(inputCode)){
            hql += " and upper(dv.inputCode) like '%" + inputCode.toUpperCase() + "%'";
        }
        return createQuery(DictValue.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据字典代码获取字典
     * @param dictCode
     * @return
     */
    public DictType getDictTypeByCode(String dictCode) {
        String hql = "from DictType as t where t.dictCode='"+dictCode+"' and t.status<>'-1'" ;
        DictType dictType = createQuery(DictType.class, hql, new ArrayList<Object>()).getSingleResult();
        return dictType;
    }


    /**
     * 根据字典值获取字典的值
     * @param dictCode
     * @return
     */
    public List<DictValue> getDictValueByCode(String dictCode) throws Exception {
        DictType dictType = this.getDictTypeByCode(dictCode);
        if(dictType!=null){
            String hql = "from DictValue as v where v.dictTypeId='"+dictType.getId()+"' and v.status<> '-1'" ;
            List<DictValue> resultList = createQuery(DictValue.class, hql, new ArrayList<Object>()).getResultList();
            return  resultList;
        }else{
            throw new Exception("没有找到对应的字典");
        }
    }
}
