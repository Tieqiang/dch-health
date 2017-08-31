package com.dch.facade;

import com.dch.entity.DrugFirm;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import javassist.expr.NewArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * 药品厂商维护
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugFirmFacade extends BaseFacade{

    /**
     * 添加、删除、修改药品厂商 删除为逻辑删除status为-1
     * @param drugFirm
     * @return
     */
    @Transactional
    public DrugFirm mergeDrugFirm(DrugFirm drugFirm) {
        if(StringUtils.isEmptyParam(drugFirm.getId())){
            drugFirm.setInputCode(PinYin2Abbreviation.cn2py(drugFirm.getFirmName()));
        }
        return merge(drugFirm);
    }

    /**
     * 查询药品厂商
     * @param inputCode 拼音码
     * @param firmName 厂商名称
     * @return
     */
    public Page<DrugFirm> getDrugFirms(String inputCode, String firmName,int perPage,int currentPage) {
        Page<DrugFirm> drugFirmPage = new Page<>();
        String hql = "from DrugFirm where status<>'-1'";
        String hqlCount = "select count(*) from DrugFirm where status<>'-1'";
        if(!StringUtils.isEmptyParam(inputCode) && !StringUtils.isEmptyParam(firmName)){
            hql += " and (firmName like '%"+firmName+"%' or upper(inputCode) like '%"+inputCode.toUpperCase()+"%')";
            hqlCount += " and (firmName like '%"+firmName+"%' or upper(inputCode) like '%"+inputCode.toUpperCase()+"%')";
        }else if(!StringUtils.isEmptyParam(inputCode)){
            hql += " and  upper(inputCode) like '%"+inputCode.toUpperCase()+"%'";
            hqlCount += " and  upper(inputCode) like '%"+inputCode.toUpperCase()+"%'";
        }else if(!StringUtils.isEmptyParam(firmName)){
            hql += " and firmName like '%"+firmName+"%'";
            hqlCount += " and firmName like '%"+firmName+"%'";
        }
        TypedQuery<DrugFirm> typedQuery = createQuery(DrugFirm.class,hql,new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        drugFirmPage.setCounts(counts);
        if(perPage<=0){
            perPage = 100;
        }
        if(perPage>0){
            if(currentPage<=0){
                currentPage = 1;
            }
            typedQuery.setFirstResult((currentPage-1)*perPage) ;
            typedQuery.setMaxResults(perPage*currentPage);
            drugFirmPage.setPerPage((long)perPage);
        }
        List<DrugFirm> drugFirmList = typedQuery.getResultList();
        drugFirmPage.setData(drugFirmList);
        return drugFirmPage;
    }

    /**
     * 根据ID获取单独的药品厂商
     * @param firmId
     * @return
     */
    public DrugFirm getDrugFirm(String firmId) throws Exception{
        String hql = " from DrugFirm where status<>'-1' and id = '"+firmId+"'";
        List<DrugFirm> drugFirmList = createQuery(DrugFirm.class,hql,new ArrayList<Object>()).getResultList();
        if(drugFirmList!=null && !drugFirmList.isEmpty()){
            return drugFirmList.get(0);
        }else{
            throw new Exception("厂商信息不存在");
        }
    }
}
