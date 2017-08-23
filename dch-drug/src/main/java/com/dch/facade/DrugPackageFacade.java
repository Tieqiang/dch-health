package com.dch.facade;

import com.dch.entity.DrugPackage;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品包材维护
 * Created by Administrator on 2017/8/23.
 */
@Service
public class DrugPackageFacade extends BaseFacade{

    @Transactional
    public DrugPackage mergeDrugPackage(DrugPackage drugPackage) {
        return merge(drugPackage);
    }

    /**
     * 获取药品包材信息
     * @param packageName
     * @param wherehql
     * @return
     */
    public List<DrugPackage> getDrugPackages(String packageName, String wherehql) {
        String hql = "from DrugPackage where status<>'-1' ";
        if(!StringUtils.isEmptyParam(packageName)){
            hql += " and packageName like '%"+packageName+"%'";
        }
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += " and "+wherehql;
        }
        return createQuery(DrugPackage.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取单一的包材信息
     * @param packageId
     * @return
     */
    public DrugPackage getDrugPackage(String packageId) throws Exception{
        String hql = "from DrugPackage where status<>'-1' and id = '"+packageId+"'";
        List<DrugPackage> drugPackageList = createQuery(DrugPackage.class,hql,new ArrayList<Object>()).getResultList();
        if(drugPackageList!=null && !drugPackageList.isEmpty()){
            return drugPackageList.get(0);
        }else{
            throw new Exception("药品包材信息不存在");
        }
    }
}
