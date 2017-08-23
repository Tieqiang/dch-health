package com.dch.facade;

import com.dch.entity.DrugBaseInfo;
import com.dch.entity.DrugPackageInfo;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugPackageInfoFacade extends BaseFacade{

    @Transactional
    public DrugPackageInfo mergeDrugPackage(DrugPackageInfo drugPackageInfo) throws Exception{
        if(!"-1".equals(drugPackageInfo.getStatus())){
            String hql = " from DrugPackageInfo where status<>'-1' and drugId = '"+drugPackageInfo.getDrugId()+"'" +
                         " and packageSpec = '"+drugPackageInfo.getPackageSpec()+"' and id<>'"+drugPackageInfo.getId()+"'";
            List<DrugPackageInfo> drugPackageInfoList = createQuery(DrugPackageInfo.class,hql,new ArrayList<Object>()).getResultList();
            if(drugPackageInfoList!=null && !drugPackageInfoList.isEmpty()){
                throw new Exception("该包装规格信息已存在");
            }
            if(!StringUtils.isEmptyParam(drugPackageInfo.getId())){//修改包装规格信息
                DrugPackageInfo dbDrugPackageInfo = get(DrugPackageInfo.class,drugPackageInfo.getId());
                if(dbDrugPackageInfo.getPackageSpec()!=null && !dbDrugPackageInfo.getPackageSpec().equals(drugPackageInfo.getPackageSpec())){
                    String baseHql = " from DrugBaseInfo where status<>'-1' and id = '"+drugPackageInfo.getDrugId()+"' ";
                    List<DrugBaseInfo> drugBaseInfoList = createQuery(DrugBaseInfo.class,baseHql,new ArrayList<Object>()).getResultList();
                    if(drugBaseInfoList!=null && !drugBaseInfoList.isEmpty()){
                        DrugBaseInfo drugBaseInfo = drugBaseInfoList.get(0);
                        if(drugBaseInfo.getSpec().equals(dbDrugPackageInfo.getPackageSpec())){
                            drugBaseInfo.setSpec(drugPackageInfo.getPackageSpec());
                            merge(drugBaseInfo);
                        }
                    }
                }
            }
            return merge(drugPackageInfo);
        }
        return merge(drugPackageInfo);
    }

    public List<DrugPackageInfo> getDrugPackInfos(String drugId) {
        String hql = "from DrugPackageInfo where status<>'-1'";
        if(!StringUtils.isEmptyParam(drugId)){
            hql += " and drugId = '"+drugId+"'";
        }
        return createQuery(DrugPackageInfo.class,hql,new ArrayList<Object>()).getResultList();
    }

    public DrugPackageInfo getDrugPackageInfo(String packageInfoId) throws Exception{
        String hql = "from DrugPackageInfo where status<>'-1' and id = '"+packageInfoId+"'";
        List<DrugPackageInfo> drugPackageInfoList = createQuery(DrugPackageInfo.class,hql,new ArrayList<Object>()).getResultList();
        if(drugPackageInfoList!=null && !drugPackageInfoList.isEmpty()){
            return drugPackageInfoList.get(0);
        }else{
            throw new Exception("药品规格信息不存在");
        }
    }
}
