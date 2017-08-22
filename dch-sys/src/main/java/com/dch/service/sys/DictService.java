package com.dch.service.sys;

import com.dch.entity.DictType;
import com.dch.entity.DictValue;
import com.dch.facade.DictFacade;
import com.dch.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
@Controller
@Path("sys/dict")
@Produces("application/json")
public class DictService {
    @Autowired
    private DictFacade dictFacade;

    /**
     * 获取单个字典类型
     * @param dictTypeId
     * @return
     */
    @GET
    @Path("get-dict-type")
    public DictType getDictType(@QueryParam("dictTypeId") String dictTypeId) throws Exception{
        return dictFacade.getDictType(dictTypeId);
    }


    /**
     * 根据字典代码获取字典
     * @param dictCode
     * @return
     */
    @GET
    @Path("get-dict-type-by-code")
    public DictType getDictTypeByCode(@QueryParam("dictCode")String dictCode){
        return dictFacade.getDictTypeByCode(dictCode);
    }


    /**
     * 根据字典值获取字典的值
     * @param dictCode
     * @return
     */
    @GET
    @Path("get-dict-values-by-code")
    public List<DictValue> getDictValueByCode(@QueryParam("dictCode")String dictCode) throws Exception {
        return dictFacade.getDictValueByCode(dictCode);
    }


    /**
     * 获取所有字典
     * @param inputCode
     * @return
     */
    @GET
    @Path("get-dict-types")
    public List<DictType> getDictTypes(@QueryParam("inputCode") String inputCode){
        return dictFacade.getDictTypes(inputCode);
    }

    /**
     * 新增、修改、删除字典类型 删除为逻辑删除 修改status状态为-1
     * @param dictType
     * @return
     */
    @POST
    @Path("merge-dict-type")
    public Response mergeDictType(DictType dictType) throws Exception{
        return dictFacade.mergeDictType(dictType);
    }

    /**
     *获取某个字典类型的字典内容
     * @param dictTypeId
     * @return
     */
    @GET
    @Path("get-dict-values")
    public List<DictValue> getDictValues(@QueryParam("dictTypeId") String dictTypeId){
        return dictFacade.getDictValues(dictTypeId,null);
    }

    /**
     * 获取某个字典内容
     * @param dictId
     * @return
     */
    @GET
    @Path("get-dict-value")
    public DictValue getDictValue(@QueryParam("dictId") String dictId) throws Exception{
        return dictFacade.getDictValue(dictId);
    }

    /**
     * 新增、修改、删除字典内容 删除为逻辑删除 修改status状态为-1
     * @param dictValue
     * @return
     */
    @POST
    @Path("merge-dict-value")
    @Transactional
    public Response mergeDictValue(DictValue dictValue) throws Exception{
        return dictFacade.mergeDictValue(dictValue);
    }

    /**
     * 根据拼音码过滤字典内容
     * @param dictTypeId
     * @param inputCode
     * @return
     */
    @GET
    @Path("get-dict-values-by-inputcode")
    public List<DictValue> getDictValuesByInputcode(@QueryParam("dictTypeId") String dictTypeId,@QueryParam("inputCode") String inputCode){
        return dictFacade.getDictValues(dictTypeId,inputCode);
    }

}
