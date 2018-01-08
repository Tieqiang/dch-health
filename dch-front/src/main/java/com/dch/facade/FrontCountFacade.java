package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
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
    public List<DrugCountryVo> getAllDrugFirms(String flag){
        List<DrugCountryVo> drugCountryVoList = new ArrayList<>();
        String sql = "select count(*),address_province from drug_firm where status <>'-1' ";
        if(!StringUtils.isEmptyParam(flag)){
            sql += " and foreign_flag = '"+flag+"' ";
        }
        sql += " group by address_province";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                DrugCountryVo drugCountryVo = new DrugCountryVo();
                Object[] params = (Object[])list.get(i);
                drugCountryVo.setCount(Integer.valueOf(params[0].toString()));
                drugCountryVo.setCountry((String) params[1]);
                drugCountryVoList.add(drugCountryVo);
            }
        }
        return drugCountryVoList;
    }

    /**
     * 获取不同国家所有药品信息
     * @return
     */
    public List<DrugCountryVo> getDrugCountryVos(){
        List<DrugCountryVo> drugCountryVoList = new ArrayList<>();
        String sql = "select count(distinct p.drug_Id),f.address_province" +
                " from drug_package_info as p,drug_firm as f where " +
                " p.firm_id = f.id and p.status = '1' and f.status = '1'" +
                " group by f.address_province";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                DrugCountryVo drugCountryVo = new DrugCountryVo();
                Object[] params = (Object[])list.get(i);
                drugCountryVo.setCount(Integer.valueOf(params[0].toString()));
                drugCountryVo.setCountry((String) params[1]);
                drugCountryVoList.add(drugCountryVo);
            }
        }
        return drugCountryVoList;
    }
}
