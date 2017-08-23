package com.dch.facade;

import com.dch.entity.DrugBaseInfo;
import com.dch.entity.DrugClass;
import com.dch.entity.DrugNameDict;
import com.dch.entity.DrugPackageInfo;
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
 * 药物基本信息维护
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugBaseInfoFacade extends BaseFacade{

    @Transactional
    public DrugBaseInfo mergeDrugBaseInfo(DrugBaseInfo drugBaseInfo) throws Exception{
        if(StringUtils.isEmptyParam(drugBaseInfo.getId())){//添加
            String code = getDrugCodeBySequenc(drugBaseInfo.getClassId());//药品类别码+5位序列数
            drugBaseInfo.setDrugCode(code);
            DrugBaseInfo merge = merge(drugBaseInfo);
            //生成DrugDict
            DrugNameDict drugNameDict = new DrugNameDict();
            drugNameDict.setDrugCode(code);
            drugNameDict.setDrugName(drugBaseInfo.getDrugName());
            drugNameDict.setInputCode(PinYin2Abbreviation.cn2py(drugBaseInfo.getDrugName()));
            drugNameDict.setStatus("1");
            merge(drugNameDict);
            //生成DrugPackageInfo
            DrugPackageInfo drugPackageInfo = new DrugPackageInfo();
            drugPackageInfo.setDrugCode(code);
            drugPackageInfo.setDrugId(merge.getId());
            drugPackageInfo.setStatus("1");
            merge(drugPackageInfo);
            return merge;
        }
        if(!StringUtils.isEmptyParam(drugBaseInfo.getId()) && !"-1".equals(drugBaseInfo.getStatus())){//修改
            DrugBaseInfo dbDrugBaseInfo = get(DrugBaseInfo.class,drugBaseInfo.getId());
            DrugNameDict drugNameDict = getDrugNameDictByCodeAndName(dbDrugBaseInfo.getDrugCode(),dbDrugBaseInfo.getDrugName());
            DrugPackageInfo drugPackageInfo = getDrugPackageInfoByDrugId(drugBaseInfo.getId());
            if(drugBaseInfo.getClassId()!=null && !drugBaseInfo.getClassId().equals(dbDrugBaseInfo.getClassId())){
                String code = getDrugCodeBySequenc(drugBaseInfo.getClassId());
                drugBaseInfo.setDrugCode(code);
                //更新字典
                drugNameDict.setDrugCode(code);
                //更新包装信息
                drugPackageInfo.setDrugCode(code);
            }
            if(drugBaseInfo.getDrugName()!=null && !drugBaseInfo.getDrugName().equals(dbDrugBaseInfo.getDrugName())){
                drugNameDict.setDrugName(drugBaseInfo.getDrugName());
                drugNameDict.setInputCode(PinYin2Abbreviation.cn2py(drugBaseInfo.getDrugName()));
            }
            DrugBaseInfo merge = merge(drugBaseInfo);
            merge(drugNameDict);
            merge(drugPackageInfo);
            return merge;
        }
        return merge(drugBaseInfo);
    }

    public DrugPackageInfo getDrugPackageInfoByDrugId(String drugId) throws Exception{
        String hql = "from DrugPackageInfo where status<>'-1' and drugId = '"+drugId+"'";
        List<DrugPackageInfo> drugPackageInfoList = createQuery(DrugPackageInfo.class,hql,new ArrayList<Object>()).getResultList();
        if(drugPackageInfoList!=null && !drugPackageInfoList.isEmpty()){
            return drugPackageInfoList.get(0);
        }else{
            throw new Exception("药品包装不存在");
        }
    }

    public DrugNameDict getDrugNameDictByCodeAndName(String drugCode,String drugName) throws Exception{
        String hql = "from DrugNameDict where drugCode = '"+drugCode+"' and drugName = '"+drugName+"'";
        List<DrugNameDict> drugNameDicts = createQuery(DrugNameDict.class,hql,new ArrayList<Object>()).getResultList();
        if(drugNameDicts!=null && !drugNameDicts.isEmpty()){
            return drugNameDicts.get(0);
        }else{
            throw new Exception("药品字典信不存在");
        }
    }
    /**
     * 根据类别名称id查询数据库已有多少条药品基本信息并根据条数生成随机码
     * @param classId
     * @return
     * @throws Exception
     */
    public String getDrugCodeBySequenc(String classId) throws Exception{
        String hql = "select count(*) from DrugBaseInfo where classId = '"+classId+"'";
        Long count = createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
        DrugClass drugClass = get(DrugClass.class,classId);
        return drugClass.getClassCode()+completingCode(String.valueOf(count+1));
    }
    public String completingCode(String number){
        StringBuffer sb = new StringBuffer("");
        if(number!=null && number.length()<5){
            for(int i=0;i<5-number.length();i++){
                sb.append("0");
            }
        }
        sb.append(number);
        return sb.toString();
    }

    public DrugBaseInfo getDrugBaseInfo(String baseInfoId, String drugCode) throws Exception{
        String hql = "from DrugBaseInfo where status<>'-1' ";
        if(StringUtils.isEmptyParam(baseInfoId) && StringUtils.isEmptyParam(drugCode)){
            throw new Exception("参数信息不能同时为空");
        }
        if(!StringUtils.isEmptyParam(baseInfoId)){
            hql += " and id = '"+baseInfoId+"'";
        }
        if(!StringUtils.isEmptyParam(drugCode)){
            hql += " and drugCode = '"+drugCode+"'";
        }
        List<DrugBaseInfo> drugBaseInfoList = createQuery(DrugBaseInfo.class,hql,new ArrayList<Object>()).getResultList();
        if(drugBaseInfoList!=null && !drugBaseInfoList.isEmpty()){
            return drugBaseInfoList.get(0);
        }else{
            throw new Exception("药品基本信息不存在");
        }
    }

    /**
     * 获取药品基本信息
     * classId:药品类别（选传）perPage:每页显示数量（选传）currentPage：当前页（选传）wherehql:自己拼装的条件（选传）
     * inputCode:不区分大小写的模糊匹配（选传）
     * drugName:模糊匹配（选传）如果和inputCode同时传递，则取并集;另外drugName和可以是商品名和其他任何一种名称
     * @param classId
     * @param perPage
     * @param currentPage
     * @param wherehql
     * @param inputCode
     * @param drugName
     * @return
     */
    public Page<DrugBaseInfo> getDrugBaseInfos(String classId, int perPage, int currentPage, String wherehql, String inputCode, String drugName) {
        String hql = "select distinct base from DrugBaseInfo as base,DrugNameDict as dict where base.status<>'-1' and dict.status<>'-1'" +
                     " and base.drugCode = dict.drugCode ";
        String hqlCount = "select count(distinct base) from DrugBaseInfo as base,DrugNameDict as dict  where base.status<>'-1' and dict.status<>'-1'" +
                          " and base.drugCode = dict.drugCode ";
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += (" and "+ wherehql);
            hqlCount += (" and "+ wherehql);
        }
        if(!StringUtils.isEmptyParam(classId)){
            hql += " and classId = '"+classId+"'";
            hqlCount += " and classId = '"+classId+"'";
        }
        if(!StringUtils.isEmptyParam(inputCode) && !StringUtils.isEmptyParam(drugName)){
            hql += " and (base.drugName like '%"+drugName+"%' or dict.drugName like '%"+drugName+"%'" +
                    " or upper(dict.inputCode) like '%"+inputCode.toUpperCase()+"%')";
            hqlCount += " and (base.drugName like '%"+drugName+"%' or dict.drugName like '%"+drugName+"%'" +
                        " or upper(dict.inputCode) like '%"+inputCode.toUpperCase()+"%')";
        }else if(!StringUtils.isEmptyParam(inputCode)){
            hql += " and upper(dict.inputCode) like '%"+inputCode.toUpperCase()+"%' ";
            hqlCount += " and upper(dict.inputCode) like '%"+inputCode.toUpperCase()+"%' ";
        }else if(!StringUtils.isEmptyParam(drugName)){
            hql += " and (base.drugName like '%"+drugName+"%' or dict.drugName like '%"+drugName+"%')";
            hqlCount += " and (base.drugName like '%"+drugName+"%' or dict.drugName like '%"+drugName+"%')";
        }
        hql += " order by base.createDate desc";
        hqlCount += " order by base.createDate desc";
        TypedQuery<DrugBaseInfo> typedQuery = createQuery(DrugBaseInfo.class,hql,new ArrayList<Object>());
        Page<DrugBaseInfo> drugBaseInfoPage = new Page<>();
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        drugBaseInfoPage.setCounts(counts);
        if(perPage>0){
            typedQuery.setFirstResult(currentPage*perPage) ;
            typedQuery.setMaxResults(perPage);
            drugBaseInfoPage.setPerPage((long) perPage);
        }
        List<DrugBaseInfo> drugBaseInfoList = typedQuery.getResultList();
        drugBaseInfoPage.setData(drugBaseInfoList);
        return drugBaseInfoPage;
    }
}
