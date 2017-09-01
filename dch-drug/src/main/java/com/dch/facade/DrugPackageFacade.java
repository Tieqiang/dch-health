package com.dch.facade;

import com.dch.entity.DrugPackage;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
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
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugPackage> getDrugPackages(String packageName, String wherehql, int perPage, int currentPage) {
        String hql = "from DrugPackage where status<>'-1' ";
        String hqlCount = "select count(*) from DrugPackage where status<>'-1' ";
        if(!StringUtils.isEmptyParam(packageName)){
            hql += " and packageName like '%"+packageName+"%'";
            hqlCount += " and packageName like '%"+packageName+"%'";
        }
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += " and "+wherehql;
            hqlCount += " and "+wherehql;
        }
        TypedQuery<DrugPackage> query = createQuery(DrugPackage.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page=new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugPackage> drugPackageList = query.getResultList();
        page.setCounts(counts);
        page.setData(drugPackageList);
        return page;
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
