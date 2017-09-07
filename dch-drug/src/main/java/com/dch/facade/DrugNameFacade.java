package com.dch.facade;

import com.dch.entity.DrugBaseInfo;
import com.dch.entity.DrugNameDict;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * 药品名称维护
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugNameFacade extends BaseFacade{

    @Transactional
    public DrugNameDict mergeDrugName(DrugNameDict drugNameDict) throws Exception{
        if(!"-1".equals(drugNameDict.getStatus())){
            String hql = "from DrugNameDict where status<>'-1' and drugId = '"+drugNameDict.getDrugId()+"'" +
                    " and drugName = '"+drugNameDict.getDrugName()+"' and id<> '"+drugNameDict.getId()+"'";
            List<DrugNameDict> drugNameDictList = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
            if(drugNameDictList!=null && !drugNameDictList.isEmpty()){
                throw new Exception("药品名称字典已存在，请修改");
            }
            if(!StringUtils.isEmptyParam(drugNameDict.getId())){
                DrugNameDict dbDrugNameDict = get(DrugNameDict.class,drugNameDict.getId());
                if(drugNameDict.getDrugName()!=null && !drugNameDict.getDrugName().equals(dbDrugNameDict.getDrugName())){
                    List<DrugBaseInfo> drugBaseInfoList = getDrugBaseInfos(drugNameDict.getDrugId(),drugNameDict.getDrugName());
                    if(drugBaseInfoList!=null && !drugBaseInfoList.isEmpty()){
                        DrugBaseInfo drugBaseInfo = drugBaseInfoList.get(0);
                        drugBaseInfo.setDrugName(drugNameDict.getDrugName());
                        merge(drugBaseInfo);
                    }
                }
            }
            drugNameDict.setInputCode(PinYin2Abbreviation.cn2py(drugNameDict.getDrugName()));
            return merge(drugNameDict);
        }else{//删除药品字典名称 正名不允许删除
            List<DrugBaseInfo> drugBaseInfoList = getDrugBaseInfos(drugNameDict.getDrugId(),drugNameDict.getDrugName());
            if(drugBaseInfoList!=null && !drugBaseInfoList.isEmpty()){
                throw new Exception("药品正名不允许删除！");
            }
            return merge(drugNameDict);
        }
    }

    /**
     * 根据药品id和药品名称查询药品基本信息
     * @param drugId
     * @param drugName
     * @return
     */
    public List<DrugBaseInfo> getDrugBaseInfos(String drugId,String drugName){
        String baseHql = " from DrugBaseInfo where status<>'-1' and id='"+drugId+"' and drugName = '"+drugName+"'";
        List<DrugBaseInfo> drugBaseInfoList = createQuery(DrugBaseInfo.class,baseHql,new ArrayList<Object>()).getResultList();
        return drugBaseInfoList;
    }
    public Page<DrugNameDict> getDrugNames(String drugCode, String inputCode, int perPage, int currentPage, String drugId) {
        Page<DrugNameDict> drugNameDictPage = new Page<>();
        String hql = " from DrugNameDict where status<>'-1' ";
        String hqlCount = "select count(*) from DrugNameDict where status<>'-1' ";
        if(!StringUtils.isEmptyParam(drugCode)){
            hql +=" and drugCode = '"+drugCode+"'";
            hqlCount += " and drugCode = '"+drugCode+"'";
        }
        if(!StringUtils.isEmptyParam(drugId)){
            hql +=" and drugId = '"+drugId+"'";
            hqlCount += " and drugId = '"+drugId+"'";
        }
        if(!StringUtils.isEmptyParam(inputCode)){
            hql += " and upper(inputCode) like '%"+inputCode.toUpperCase()+"%'";
            hqlCount += " and upper(inputCode) like '%"+inputCode.toUpperCase()+"%'";
        }
        TypedQuery<DrugNameDict> typedQuery = createQuery(DrugNameDict.class,hql,new ArrayList<Object>());
        Long counts =  createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        drugNameDictPage.setCounts(counts);
        if(perPage<=0){
            perPage =100;
        }
        if(currentPage<=0){
            currentPage=1;
        }
        typedQuery.setFirstResult((currentPage-1)*perPage);
        typedQuery.setMaxResults(perPage);
        drugNameDictPage.setPerPage((long)perPage);
        List<DrugNameDict> drugNameDictList = typedQuery.getResultList();
        drugNameDictPage.setData(drugNameDictList);
        return drugNameDictPage;
    }

    public DrugNameDict getDrugName(String nameId) throws Exception{
        String hql = " from DrugNameDict where status<>'-1' and id = '"+nameId+"'";
        List<DrugNameDict> drugNameDicts = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
        if(drugNameDicts!=null && !drugNameDicts.isEmpty()){
            return drugNameDicts.get(0);
        }else{
            throw new Exception("药品名称信息不存在");
        }
    }
}
