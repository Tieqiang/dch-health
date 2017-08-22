package com.dch.service;

import com.dch.entity.*;
import com.dch.facade.DataElementFacade;
import com.dch.facade.common.VO.Page;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.util.PinYin2Abbreviation;
import com.dch.vo.DataRealmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/8.
 */
@Controller
@Path("doc/data-element")
@Produces("application/json")
public class DataElementService {

    @Autowired
    private DataElementFacade dataElementFacade;

    /**
     * 获取某个目录下的元数据列表
     *
     * @param categoryId
     * @param inputCode
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-data-elements")
    public Page<DataElement> getDataElements(@QueryParam("categoryId") String categoryId,
                                             @QueryParam("inputCode") String inputCode,
                                             @QueryParam("perPage") int perPage,
                                             @QueryParam("dataElementName") String dataElementName,
                                             @QueryParam("currentPage") int currentPage) {
        String hql = "select distinct e from DataElement as e ,DataElementCategory as c where e.categoryId =c.id  and e.status<>'-1' ";
        String hqlCount = "select count( distinct e) from DataElement as e ,DataElementCategory as c where e.categoryId =c.id  and e.status<>'-1'";
        if (!"".equals(categoryId) && null != categoryId) {
            hql += " and e.categoryId = '" + categoryId + "'";
            hqlCount += " and e.categoryId = '" + categoryId + "'";
        }
        if (!"".equals(inputCode) && null != inputCode) {
            hql += " and upper(e.inputCode) like  upper('%" + inputCode + "%')";
            hqlCount += " and upper(e.inputCode) like upper('%" + inputCode + "%')";

            if (!"".equals(dataElementName) && null != dataElementName) {
                hql += " or upper(e.dataElementName) like  upper('%" + dataElementName + "%')";
                hqlCount += " or upper(e.dataElementName) like upper('%" + dataElementName + "%')";
            }
        }else{
            if (!"".equals(dataElementName) && null != dataElementName) {
                hql += " and upper(e.dataElementName) like  upper('%" + dataElementName + "%')";
                hqlCount += " and upper(e.dataElementName) like upper('%" + dataElementName + "%')";
            }
        }

        hql += " order by e.createDate desc";
        hqlCount += " order by e.createDate desc";
        TypedQuery<DataElement> query = dataElementFacade.createQuery(DataElement.class, hql, new ArrayList<Object>());
        Long count = dataElementFacade.createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        Page<DataElement> patientPage = new Page<>();
        patientPage.setCounts(count);
        if (perPage > 0) {
            query.setFirstResult(currentPage * perPage);
            query.setMaxResults(perPage);
        }
        List<DataElement> yunPatients = query.getResultList();
        patientPage.setData(yunPatients);
        return patientPage;
    }

    /**
     * 添加、删除、修改元数据
     *
     * @param dataElement
     * @return
     */
    @POST
    @Path("merge-data-element")
    @Transactional
    public Response mergeDataElement(DataElement dataElement) throws Exception {

        if (("-1").equals(dataElement.getStatus())) {
            String dataId = dataElement.getId();
            String hql = "from DataSetVsElement where dataElementId='" + dataId + "'";
            List<DataSetVsElement> List = dataElementFacade.createQuery(DataSetVsElement.class, hql, new ArrayList<Object>()).getResultList();
            if (List != null && !List.isEmpty()) {
                throw new Exception("数据已经被引用，不可删除");
            } else {
                DataElement merge = dataElementFacade.merge(dataElement);
                return Response.status(Response.Status.OK).entity(merge).build();
            }
        }
        DataElement merge = dataElementFacade.merge(dataElement);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据ID获取元数据
     *
     * @param dataElementId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-element")
    public DataElement getDataElement(@QueryParam("dataElementId") String dataElementId) throws Exception {
        return dataElementFacade.getDataElement(dataElementId);
    }

    /**
     * 获取某个目录下的值域列表
     *
     * @param categoryId
     * @param inputCode
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-data-realms")
    public Page<DataRealmVo> getDataRealms(@QueryParam("categoryId") String categoryId,
                                           @QueryParam("inputCode") String inputCode,
                                           @QueryParam("dataRealmName")String dataRealmName,
                                           @QueryParam("perPage") int perPage,
                                           @QueryParam("currentPage") int currentPage) {
        return dataElementFacade.getDataRealms(categoryId,inputCode,perPage,currentPage,dataRealmName);
    }

    /**
     * 获取值域和值内容信息
     *
     * @param dataRealmId
     * @return
     */
    @GET
    @Path("get-data-realm")
    public DataRealmVo getDataRealm(@QueryParam("dataRealmId") String dataRealmId) throws Exception {
        return dataElementFacade.getDataRealm(dataRealmId);
    }

    /**
     * 单独获取值域信息
     *
     * @param dataRealmId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-only-data-realm")
    public DataRealm getOnlyDataRealm(@QueryParam("dataRealmId") String dataRealmId) throws Exception {
        return dataElementFacade.getOnlyDataRealm(dataRealmId);
    }

    /**
     * 获取某个值域的之内容
     *
     * @param dataRealmId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-values")
    public List<DataRealmValue> getDataValues(@QueryParam("dataRealmId") String dataRealmId) throws Exception {
        return dataElementFacade.getDataValues(dataRealmId);
    }

    /**
     * 添加、修改、删除值域
     *
     * @param dataRealm
     * @return
     * @throws Exception
     */
    @POST
    @Path("merge-data-realm")
    @Transactional
    public Response mergeDataRealm(DataRealm dataRealm) throws Exception {
        dataRealm.setInputCode(PinYin2Abbreviation.cn2py(dataRealm.getDataRealmName()));
        if (("-1").equals(dataRealm.getStatus())) {
            String dataRealmId = dataRealm.getId();
            if(dataRealmId==null){
                throw new Exception("无效的删除对象");
            }

            String hql = "from DataElement where dataValueRealmId='" + dataRealmId + "'";
            List<DataElement> List = dataElementFacade.createQuery(DataElement.class, hql, new ArrayList<Object>()).getResultList();
            if (List != null && !List.isEmpty()) {
                throw new Exception("值域已经被引用，不可删除");
            }
        }
        DataRealm merge = dataElementFacade.merge(dataRealm);
        return Response.status(Response.Status.OK).entity(merge).build();
    }


    /**
     * 一次性保存值域和值
     * @param dataRealmVo
     * @return
     */
    @POST
    @Path("merge-data-realm-and-value")
    public Response mergeDataRealmAndValue(DataRealmVo dataRealmVo){
        DataRealmVo vo = dataElementFacade.mergeDataRealmAndValue(dataRealmVo);
        return Response.status(Response.Status.OK).entity(vo).build();
    }

    /**
     * 添加、修改、删除值域内容
     *
     * @param dataRealmValue
     * @return
     * @throws Exception
     */
    @POST
    @Path("merge-data-value")
    @Transactional
    public Response mergeDataRealmValue(DataRealmValue dataRealmValue) throws Exception {

        DataRealmValue merge = dataElementFacade.merge(dataRealmValue);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取数据集
     *
     * @param dataSetName
     * @return
     */
    @GET
    @Path("get-data-sets")
    public Page<DataSet> getDataSets(@QueryParam("dataSetName") String dataSetName, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage) {
        return dataElementFacade.getDataSets(dataSetName, perPage, currentPage);
    }

    /**
     * 根据ID获取某个数据集信息
     *
     * @param dataSetId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-data-set")
    public DataSet getDataSet(@QueryParam("dataSetId") String dataSetId) throws Exception {
        return dataElementFacade.getDataSet(dataSetId);
    }

    /**
     * 获取数据集包含的数据元信息
     *
     * @param dataSetId
     * @return
     */
    @GET
    @Path("get-data-element-by-set-id")
    public List<DataElement> getDataElementBySetId(@QueryParam("dataSetId") String dataSetId) {
        return dataElementFacade.getDataElementBySetId(dataSetId);
    }

    /**
     * 添加、删除、修改数据集
     *
     * @param dataSet
     * @return
     */
    @POST
    @Path("merge-data-set")
    @Transactional
    public Response mergeDataSet(DataSet dataSet) {
        DataSet merge = dataElementFacade.merge(dataSet);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 添加、删除、修改数据集包含的元数据
     *
     * @param dataSetId
     * @param DataElements
     * @return
     */
    @POST
    @Path("merge-data-set-element")
    @Transactional
    public Response mergeDataSetElement(@QueryParam("dataSetId") String dataSetId, List<DataElement> DataElements) {
        dataElementFacade.mergeDataSetElement(dataSetId, DataElements);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true", "保存成功")).build();
    }

    /**
     * 删除、数据集下面的数据元
     *
     * @param dataSetId
     * @param dataElementId
     * @return
     */
    @POST
    @Path("del-data-set-element")
    @Transactional
    public Response deleteDataSetElement(@QueryParam("dataSetId") String dataSetId, @QueryParam("dataElementId") String dataElementId) {
        dataElementFacade.deleteDataSetElment(dataSetId, dataElementId);

        return Response.status(Response.Status.OK).entity(new ReturnInfo("true", "删除成功")).build();
    }






}
