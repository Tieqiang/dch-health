package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DataRealmVo;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/8.
 */
@Component
public class DataElementFacade extends BaseFacade{

    /**
     * 根据ID获取元数据
     * @param dataElementId
     * @return
     * @throws Exception
     */
    public DataElement getDataElement(String dataElementId)throws Exception{
        String hql="from DataElement where status<> '-1'and id='"+dataElementId+"'";
        List<DataElement> dataElementList = createQuery(DataElement.class,hql,new ArrayList<Object>()).getResultList();
        if(dataElementList!=null && !dataElementList.isEmpty()){
            return dataElementList.get(0);
        }else{
            throw new Exception("该元数据不存在");
        }
    }

    /**
     * 获取值域和值内容信息
     * @param dataRealmId
     * @return
     */
    public DataRealmVo getDataRealm(String dataRealmId)throws Exception{
        DataRealmVo vo = new DataRealmVo();
        DataRealm dataRealm = get(DataRealm.class, dataRealmId);
        vo.setDataRealm(dataRealm);

        String hql = " from DataRealmValue as v where v.dataRealmId= '"+dataRealmId+"'" ;
        List<DataRealmValue> resultList = createQuery(DataRealmValue.class, hql, new ArrayList<Object>()).getResultList();
        vo.setDataRealmValues(resultList);
        return vo ;
    }
    /**
     * 单独获取值域信息
     * @param dataRealmId
     * @return
     * @throws Exception
     */
    public DataRealm getOnlyDataRealm(String dataRealmId)throws Exception{
        String hql="from DataRealm where status<> '-1'and id='"+dataRealmId+"'";
        List<DataRealm> list = createQuery(DataRealm.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }else{
            throw new Exception("该值域和值内容不存在");
        }
    }
    /**
     * 获取某个值域的之内容
     * @param dataRealmId
     * @return
     * @throws Exception
     */
    public List<DataRealmValue> getDataValues(String dataRealmId){
        String hql="from DataRealmValue where status<> '-1'and id='"+dataRealmId+"'";
        return createQuery(DataRealmValue.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取数据集
     * @param dataSetName
     * @return
     */
    public Page<DataSet> getDataSets(String dataSetName,int perPage,int currentPage){
        String hql="from DataSet where status<> '-1'";
        String hqlCount = "select count(f) from DataSet f where status<> '-1'";

        if(dataSetName!=null&&!"".equals(dataSetName)){
            hql+=" and dataSetName like '%"+dataSetName+"%'";
            hqlCount+=" and dataSetName like '%"+dataSetName+"%'";
        }

        TypedQuery<DataSet> query = createQuery(DataSet.class, hql, new ArrayList<Object>());

        Long count = createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();

        Page<DataSet> dataSetPage = new Page<>();
        dataSetPage.setCounts(count);

        if(perPage>0){
            query.setFirstResult(currentPage*perPage);
            query.setMaxResults((currentPage+1)*perPage);
        }
        dataSetPage.setData(query.getResultList());
        return dataSetPage;
    }

    /**
     * 根据ID获取某个数据集信息
     * @param dataSetId
     * @return
     * @throws Exception
     */
    public DataSet getDataSet(String dataSetId)throws Exception{
        String hql="from DataSet where status<> '-1' and id='"+dataSetId+"'";
        return  createQuery(DataSet.class,hql,new ArrayList<Object>()).getSingleResult();
    }

    /**
     * 获取数据集包含的数据元信息
     * @param dataSetId
     * @return
     */
    public List<DataElement> getDataElementBySetId(String dataSetId) {
        String hql="select element from DataSet d,DataSetVsElement e,DataElement as element where d.status<> '-1' and " +
                "d.id=e.dataSetId and e.dataSetId='"+dataSetId+"'" +
                " and e.dataElementId = element.id" +
                " and element.status <> '-1'";
        List<DataElement> dataElements = createQuery(DataElement.class, hql, new ArrayList<Object>()).getResultList();
        return dataElements;
    }

    /**
     * 添加、删除、修改数据集包含的元数据
     * @param dataSetId
     * @param dataElementId
     * @return
     */
    @Transactional
    public int deleteDataSetElment(String dataSetId, String dataElementId) {
        String hql="Delete from DataSetVsElement where dataSetId='"+dataSetId+"' and dataElementId='"+dataElementId+"'";
        return excHql(hql);
    }




    /**
     * 删除、数据集下面的数据元
     * @param dataSetId
     * @param dataElements
     */
    @Transactional
    public void mergeDataSetElement(String dataSetId, List<DataElement> dataElements) {

        for(DataElement element:dataElements){
            DataSetVsElement dvs = getDataSetVsElement(dataSetId,element.getId());
            if(dvs==null){
                DataSetVsElement dataSetVsElement = new DataSetVsElement();
                dataSetVsElement.setDataSetId(dataSetId);
                dataSetVsElement.setDataElementId(element.getId());
                merge(dataSetVsElement);
            }
        }
    }

    /**
     * 获取对照信息
     * @param dataSetId
     * @param id
     * @return
     */
    private DataSetVsElement getDataSetVsElement(String dataSetId, String id) {

        String hql = "from DataSetVsElement as dv where dv.dataSetId='"+dataSetId+"' and dv.dataElementId='"+id+"'" ;
        List<DataSetVsElement> resultList = createQuery(DataSetVsElement.class, hql, new ArrayList<Object>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }

        return null;
    }

    public Page<DataRealmVo> getDataRealms(String categoryId, String inputCode, int perPage, int currentPage, String dataRealmName) {
        String hql = "select distinct r from DataRealm as r ,DataElementCategory as c where r.categoryId =c.id and r.status<>'-1' ";
        String hqlCount = "select count( distinct r) from DataRealm as r ,DataElementCategory as c where r.categoryId =c.id and r.status<>'-1' ";
        if (!"".equals(categoryId) && null != categoryId) {
            hql += " and r.categoryId = '" + categoryId + "'";
            hqlCount += " and r.categoryId = '" + categoryId + "'";
        }
        if (!"".equals(inputCode) && null != inputCode) {
            hql += " and upper(r.inputCode) like upper('%" + inputCode + "%')";
            hqlCount += " and upper(r.inputCode) like upper('%" + inputCode + "%')";

            if(!"".equals(dataRealmName)&&null!=dataRealmName){
                hql += " or r.dataRealmName like '%" + dataRealmName + "%'";
                hqlCount += " or r.dataRealmName like '%" + dataRealmName + "%'";
            }

        }else{
            if(!"".equals(dataRealmName)&&null!=dataRealmName){
                hql += " and r.dataRealmName like '%" + dataRealmName + "%'";
                hqlCount += " and r.dataRealmName ='%" + dataRealmName + "%'";
            }
        }
        hql += " order by r.createDate desc";
        hqlCount += " order by r.createDate desc";
        TypedQuery<DataRealm> query = createQuery(DataRealm.class, hql, new ArrayList<Object>());
        Long count = createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        Page<DataRealmVo> patientPage = new Page<>();
        patientPage.setCounts(count);
        if (perPage > 0) {
            query.setFirstResult(currentPage * perPage);
            query.setMaxResults(perPage);
        }
        List<DataRealm> dataRealms = query.getResultList();
        Page<DataRealmVo> dataRealmVoPage = new Page<>();

        List<DataRealmVo> data = dataRealmVoPage.getData();
        for(DataRealm dataRealm:dataRealms){
            DataRealmVo dataRealmVo = new DataRealmVo();

            dataRealmVo.setDataRealm(dataRealm);
            String dataValuesHql = "from DataRealmValue v where v.dataRealmId='"+dataRealm.getId()+"' and v.status<>'-1'" ;
            List<DataRealmValue> resultList = createQuery(DataRealmValue.class, dataValuesHql, new ArrayList<Object>()).getResultList();
            dataRealmVo.setDataRealmValues(resultList);
            data.add(dataRealmVo);

        }
        return dataRealmVoPage;
    }

    /**
     * 一次性保存值域和值
     * @param dataRealmVo
     * @return
     */
    @Transactional
    public DataRealmVo mergeDataRealmAndValue(DataRealmVo dataRealmVo) {
        DataRealm dataRealm = dataRealmVo.getDataRealm() ;
        List<DataRealmValue> dataRealmValues = dataRealmVo.getDataRealmValues();
        DataRealmVo vo = new DataRealmVo();
        String id = dataRealm.getId();
        if(id!=null&&!"".equals(id)){
            String hql = "delete from DataRealmValue as v where v.dataRealmId='"+dataRealm.getId()+"'" ;
            excHql(hql);
        }

        DataRealm merge = merge(dataRealm);
        vo.setDataRealm(merge);
        for (DataRealmValue value:dataRealmValues){
            value.setDataRealmId(merge.getId());
            DataRealmValue merge1 = merge(value);
            vo.getDataRealmValues().add(merge1);
        }

        return vo ;
    }
}
