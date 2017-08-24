package com.dch.facade;

import com.dch.entity.DrugBaseInfo;
import com.dch.entity.DrugNameDict;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            String hql = "from DrugNameDict where status<>'-1' and drugCode = '"+drugNameDict.getDrugCode()+"'" +
                         " and drugName = '"+drugNameDict.getDrugName()+"' and id<> '"+drugNameDict.getId()+"'";
          List<DrugNameDict> drugNameDictList = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
          if(drugNameDictList!=null && !drugNameDictList.isEmpty()){
              throw new Exception("药品名称字典已存在，请修改");
          }
          if(!StringUtils.isEmptyParam(drugNameDict.getId())){
              DrugNameDict dbDrugNameDict = get(DrugNameDict.class,drugNameDict.getId());
              if(drugNameDict.getDrugName()!=null && !drugNameDict.getDrugName().equals(dbDrugNameDict.getDrugName())){
                  String baseHql = " from DrugBaseInfo where status<>'-1' and drugCode='"+drugNameDict.getDrugCode()+"' and drugName = '"+dbDrugNameDict.getDrugName()+"'";
                  List<DrugBaseInfo> drugBaseInfoList = createQuery(DrugBaseInfo.class,baseHql,new ArrayList<Object>()).getResultList();
                  if(drugBaseInfoList!=null && !drugBaseInfoList.isEmpty()){
                      DrugBaseInfo drugBaseInfo = drugBaseInfoList.get(0);
                      drugBaseInfo.setDrugName(drugNameDict.getDrugName());
                      merge(drugBaseInfo);
                  }
              }
          }
          drugNameDict.setInputCode(PinYin2Abbreviation.cn2py(drugNameDict.getDrugName()));
          return merge(drugNameDict);
        }
        return merge(drugNameDict);
    }

    public List<DrugNameDict> getDrugNames(String drugCode, String inputCode) {
        String hql = " from DrugNameDict where status<>'-1' ";
        if(!StringUtils.isEmptyParam(drugCode)){
            hql +=" and drugCode = '"+drugCode+"'";
        }
        if(!StringUtils.isEmptyParam(inputCode)){
            hql += " and upper(inputCode) like '%"+inputCode.toUpperCase()+"%'";
        }
        List<DrugNameDict> drugNameDicts = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
        return drugNameDicts;
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
