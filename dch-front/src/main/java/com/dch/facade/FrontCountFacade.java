package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.vo.DrugCountryVo;
import com.dch.vo.DrugPackageVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
@Component
public class FrontCountFacade extends BaseFacade {

    /**
     * 根据传入的药品生产厂商id查询该厂商生产的药品信息
     * @param firmId
     * @return
     */
    public List<DrugBaseInfo> getDrugBaseInfosByFirmId(String firmId){
        String hql = "select DISTINCT f from DrugPackageInfo as p,DrugBaseInfo as f where p.drugId = f.id and p.status <>'-1'" +
                " and f.status <> '-1' and p.firmId = '"+firmId+"'";
        List<DrugBaseInfo> drugBaseInfoList = createQuery(DrugBaseInfo.class,hql,new ArrayList<Object>()).getResultList();
        return drugBaseInfoList;
    }

    /**
     * 根据生产厂商获取药品包材信息表
     * @param firmId
     * @return
     */
    public List<DrugPackageVo> getDrugPackageVosByFirmId(String firmId) {
        String hql = "select new com.dch.vo.DrugPackageVo(p.id,b.drugName,f.firmName,p.drugSpec,p.approvalNo,p.approvalNoEndDate) from DrugPackageInfo as p," +
                "DrugBaseInfo as b,DrugFirm as f where p.drugId = b.id and p.firmId = f.id and p.status = '1' and b.status = '1' and f.status = '1' " +
                "and p.firmId = '"+firmId+"'";

        List<DrugPackageVo> drugPackageVoList = createQuery(DrugPackageVo.class,hql,new ArrayList<Object>()).getResultList();
        return drugPackageVoList;
    }
    /**
     * 根据生产厂商获得药品广告信息
     * @param firmId
     * @return
     */
    public List<DrugAd> getDrugAdsByFirmId(String firmId) {
        String hql = "select distinct d from DrugAd as d,DrugPackageInfo as b where d.drugId = b.drugId and d.status='1' and b.status = '1' " +
                " and b.firmId = '"+firmId+"'";
        List<DrugAd> drugAdList = createQuery(DrugAd.class,hql,new ArrayList<Object>()).getResultList();
        return drugAdList;
    }
    /**
     * 根据生产厂商获得药品专利信息
     * @param firmId
     * @return
     */
    public List<DrugPatent> getDrugPatentsByFirmId(String firmId) {
        String hql = "select p from DrugPatent as p,DrugFirm as f where p.applyer = f.firmName and p.status = '1' and f.status = '1'" +
                " and f.id = '"+firmId+"'";
        List<DrugPatent> drugPatentList = createQuery(DrugPatent.class,hql,new ArrayList<Object>()).getResultList();
        return drugPatentList;
    }

    /**
     * 根据药品id查询药品别名信息
     * @param drugId
     * @return
     */
    public List<DrugNameDict> getDrugNameDictsByDrugId(String drugId) {
        String hql = "from DrugNameDict where status <>'-1' and drugId = '"+drugId+"'";
        List<DrugNameDict> drugNameDictList = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
        return drugNameDictList;
    }

    /**
     * 根据药品id获得药品的生产厂商
     * @param drugId
     * @return
     */
    public List<DrugFirm> getDrugFirmsByDrugId(String drugId) {
        String hql = "select distinct f from DrugPackageInfo as p,DrugFirm as f where p.status = '1' and f.status = '1'" +
                " and p.firmId = f.id and p.drugId = '"+drugId+"'";
        List<DrugFirm> drugFirmList = createQuery(DrugFirm.class,hql,new ArrayList<Object>()).getResultList();
        return drugFirmList;
    }

    /**
     * 根据药品查询此药进行广告的厂家列表
     * @param drugId
     * @return
     */
    public List<DrugFirm> getDrugAdFirmsByDrugId(String drugId) {
        String hql = "select distinct f from DrugPackageInfo as p,DrugFirm as f,DrugAd as d where p.status = '1' and f.status = '1'" +
                " and p.firmId = f.id and p.drugId = d.drugId and p.drugId = '"+drugId+"'";
        List<DrugFirm> drugFirmList = createQuery(DrugFirm.class,hql,new ArrayList<Object>()).getResultList();
        return drugFirmList;
    }

    /**
     * 获取所有厂商信息
     * @return
     */
    public List<DrugFirm> getAllDrugFirms(){
        String hql = " from DrugFirm where status <>'-1'";
        List<DrugFirm> drugFirmList = createQuery(DrugFirm.class,hql,new ArrayList<Object>()).getResultList();
        return drugFirmList;
    }

    /**
     * 获取不同国家所有药品信息
     * @return
     */
    public List<DrugCountryVo> getDrugCountryVos(){
        String hql = "select new com.dch.vo.DrugCountryVo(b.id,b.drugName,b.drugCode,b.className,b.spec,b.toxi,b.drugCategory," +
                "b.rxFlag,f.addressProvince) from DrugBaseInfo as b,DrugPackageInfo as p,DrugFirm as f where b.id = p.drugId" +
                " and p.firmId = f.id and  b.status = '1' and p.status = '1' and f.status = '1'" +
                " group by b.id";
        List<DrugCountryVo> drugCountryVoList = createQuery(DrugCountryVo.class,hql,new ArrayList<Object>()).getResultList();
        return drugCountryVoList;
    }
}
