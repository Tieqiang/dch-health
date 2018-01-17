package com.dch.intercept;

import com.dch.entity.*;
import com.dch.entity.base.BaseEntity;
import com.dch.facade.BaseSolrFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.SolrVo;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
public class MethodSolrInterceptor implements MethodInterceptor {
    @Autowired
    private BaseSolrFacade baseSolrFacade;

    @Autowired
    private BaseFacade baseFacade;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object value = null;
        Object[] arguments = invocation.getArguments();
        boolean isSuccess = false;
        try {
            value = invocation.proceed();
            isSuccess = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        if(isSuccess){
            if(arguments!=null && arguments.length>0){
                Object obj = arguments[0];
                String status = getFieldValue("status",obj)+"";
                if("-1".equals(status)){//删除数据则删除索引库索引
                    String id = getFieldValue("id",obj)+"";
                    baseSolrFacade.deleteById(id);
                }else{//添加和修改索引库数据
                    Response response = (Response)value;
                    Object dbObj = response.getEntity();
                    sendObjToSolrIndex(dbObj);
                }
            }
        }
        return value;
    }

    /**
     * 根据字段名获取字段的值
     * @param fieldName
     * @param obj
     * @return
     * @throws Exception
     */
    public String getFieldValue(String fieldName,Object obj) throws Exception{
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,obj.getClass());
        Method getMethod = pd.getReadMethod();//获得get方法
        String value = getMethod.invoke(obj)+"";
        return value;
    }

    /**
     * 根据药品id获取药品名称
     * @param id
     * @return
     */
    public String getDrugNameById(String id){
        String hql = "select drugName from DrugBaseInfo where id = '"+id+"'";
        List<String> list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }else {
            return "";
        }
    }
    public void sendObjToSolrIndex(Object dbObj) throws Exception{
        SolrVo solrVo = null;
        String status = ((BaseEntity)dbObj).getStatus();
        if("-1".equals(status)){
            String id = ((BaseEntity)dbObj).getId();
            baseSolrFacade.deleteById(id);
        }else{
            if(dbObj instanceof DrugAd){
                DrugAd drugAd = (DrugAd)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugAd.getId());
                solrVo.setTitle(drugAd.getDrugName());
                solrVo.setCategoryCode("ypgg003");
                solrVo.setDesc(drugAd.getDrugName()+","+drugAd.getAdType()+","+drugAd.getAdNo());
                solrVo.setLabel(drugAd.getAdType());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugAd.getDrugName()));
            }else if(dbObj instanceof DrugBaseInfo){
                DrugBaseInfo drugBaseInfo = (DrugBaseInfo)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugBaseInfo.getId());
                solrVo.setTitle(drugBaseInfo.getDrugName());
                solrVo.setCategoryCode("ywjbxx001");
                solrVo.setDesc(drugBaseInfo.getDrugName()+","+drugBaseInfo.getClassName()+","+drugBaseInfo.getToxi());
                solrVo.setLabel(drugBaseInfo.getClassName());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugBaseInfo.getDrugName()));
            }else if(dbObj instanceof DrugFirm){
                DrugFirm drugFirm = (DrugFirm)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugFirm.getId());
                solrVo.setTitle(drugFirm.getFirmName());
                solrVo.setCategoryCode("ypcs002");
                solrVo.setDesc(drugFirm.getFirmName()+","+drugFirm.getAddressProvince()+","+drugFirm.getAddressRegist()+","+drugFirm.getAddressProduce()
                        +","+drugFirm.getProduceRealm());
                solrVo.setLabel(drugFirm.getAddressProvince());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugFirm.getFirmName()));
            }else if(dbObj instanceof DrugPlant){
                DrugPlant drugPlant = (DrugPlant)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugPlant.getId());
                solrVo.setTitle(drugPlant.getNameCn());
                solrVo.setCategoryCode("yyzw004");
                solrVo.setDesc(drugPlant.getNameCn()+","+drugPlant.getBioFamily()+","+drugPlant.getLivePlace()+","+drugPlant.getFunctions()+","+
                        drugPlant.getLiveEnv()+","+drugPlant.getPlantCharacter());
                solrVo.setLabel(drugPlant.getBioFamily());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugPlant.getNameCn()));
            }else if(dbObj instanceof DrugNaturalActive){
                DrugNaturalActive drugNaturalActive = (DrugNaturalActive)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugNaturalActive.getId());
                solrVo.setTitle(drugNaturalActive.getDrugNaturalNameCn());
                solrVo.setCategoryCode("trywhx005");
                solrVo.setDesc(drugNaturalActive.getDrugNaturalNameCn()+","+drugNaturalActive.getBioFamily()+","+drugNaturalActive.getBioGenus()
                        +","+drugNaturalActive.getFunctions()+","+drugNaturalActive.getCollectPlace());
                solrVo.setLabel(drugNaturalActive.getBioFamily());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugNaturalActive.getDrugNaturalNameCn()));
            }else if(dbObj instanceof DrugNaturalChemicalComposition){
                DrugNaturalChemicalComposition drugNaturalChemicalComposition = (DrugNaturalChemicalComposition)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugNaturalChemicalComposition.getId());
                solrVo.setTitle(drugNaturalChemicalComposition.getNameCn());
                solrVo.setCategoryCode("trywhxcf006");
                solrVo.setDesc(drugNaturalChemicalComposition.getNameCn()+","+drugNaturalChemicalComposition.getNameEn()+","+drugNaturalChemicalComposition.getStructuralFormula()
                        +","+drugNaturalChemicalComposition.getMolecularWeight()+","+drugNaturalChemicalComposition.getPlantOrigin()+","+drugNaturalChemicalComposition.getBioactivity());
                solrVo.setLabel(drugNaturalChemicalComposition.getNameEn());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugNaturalChemicalComposition.getNameCn()));
            }else if(dbObj instanceof DrugDiseaseTreatmentGuide){
                DrugDiseaseTreatmentGuide drugDiseaseTreatmentGuide = (DrugDiseaseTreatmentGuide)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugDiseaseTreatmentGuide.getId());
                solrVo.setTitle(drugDiseaseTreatmentGuide.getGuideName());
                solrVo.setCategoryCode("ypzlzn007");
                solrVo.setDesc(drugDiseaseTreatmentGuide.getGuideName()+","+drugDiseaseTreatmentGuide.getPublisher()+","+drugDiseaseTreatmentGuide.getOrigin());
                solrVo.setLabel(drugDiseaseTreatmentGuide.getSpecialty());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugDiseaseTreatmentGuide.getGuideName()));
            }else if(dbObj instanceof DrugNewResearchPolicy){
                DrugNewResearchPolicy drugNewResearchPolicy = (DrugNewResearchPolicy)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugNewResearchPolicy.getId());
                solrVo.setTitle(drugNewResearchPolicy.getPolicyName());
                solrVo.setCategoryCode("xyyfzc008");
                solrVo.setDesc(drugNewResearchPolicy.getPolicyName()+","+drugNewResearchPolicy.getReleaseOrgCn()+","+drugNewResearchPolicy.getKeyWordsCn()
                        +","+drugNewResearchPolicy.getSummaryCn());
                solrVo.setLabel(drugNewResearchPolicy.getKeyWordsCn());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugNewResearchPolicy.getPolicyName()));
            }else if(dbObj instanceof DrugUntowardEffect){
                DrugUntowardEffect drugUntowardEffect = (DrugUntowardEffect)dbObj;
                if(!StringUtils.isEmptyParam(drugUntowardEffect.getDrugId())){
                    String drugName = getDrugNameById(drugUntowardEffect.getDrugId());
                    solrVo = new SolrVo();
                    solrVo.setId(drugUntowardEffect.getId());
                    solrVo.setTitle(drugName);
                    solrVo.setCategoryCode("ypblfy009");
                    solrVo.setDesc(drugUntowardEffect.getSex()+","+drugUntowardEffect.getAge()+","+drugUntowardEffect.getUserDrugReason()
                            +","+drugUntowardEffect.getAdministration()+","+drugUntowardEffect.getQuantity()+",不良反应开始时间:"+drugUntowardEffect.getUntowardEffectStartTime()
                            +",不良反应后果:"+drugUntowardEffect.getUntowardEffectResult()+","+drugUntowardEffect.getSolution()+","+drugUntowardEffect.getTreadResult());
                    solrVo.setLabel(drugUntowardEffect.getUserDrugReason());
                    solrVo.setCategory(PinYin2Abbreviation.cn2py(drugName));
                }
            }else if(dbObj instanceof DrugAdministrationProtect){
                DrugAdministrationProtect drugAdministrationProtect = (DrugAdministrationProtect)dbObj;
                if(!StringUtils.isEmptyParam(drugAdministrationProtect.getDrugId())){
                    String drugName = getDrugNameById(drugAdministrationProtect.getDrugId());
                    solrVo = new SolrVo();
                    solrVo.setId(drugAdministrationProtect.getId());
                    solrVo.setTitle(drugName);
                    solrVo.setCategoryCode("ypxzbh010");
                    solrVo.setDesc(drugAdministrationProtect.getApplyerCountryCn()+","+drugAdministrationProtect.getApplyer()+",授权号:"+drugAdministrationProtect.getAuthorizeNo()
                            +",授权日:"+drugAdministrationProtect.getAuthorizeDate()+",公告号,"+drugAdministrationProtect.getPubNo());
                    solrVo.setLabel(drugAdministrationProtect.getApplyer());
                    solrVo.setCategory(PinYin2Abbreviation.cn2py(drugName));
                }
            }else if(dbObj instanceof DrugInstruction){
                DrugInstruction drugInstruction = (DrugInstruction)dbObj;
                if(!StringUtils.isEmptyParam(drugInstruction.getDrugId())){
                    String drugName = getDrugNameById(drugInstruction.getDrugId());
                    solrVo = new SolrVo();
                    solrVo.setId(drugInstruction.getId());
                    solrVo.setTitle(drugName);
                    solrVo.setCategoryCode("ypsm011");
                    solrVo.setDesc(drugInstruction.getFunctions()+","+drugInstruction.getDose()+","+drugInstruction.getSepc()
                            +","+drugInstruction.getContraindication()+","+drugInstruction.getUntowardEffect()+","+drugInstruction.getAttentionMatters()
                            +","+drugInstruction.getDrugInteraction()+","+drugInstruction.getDrugAction()+","+drugInstruction.getComponent());
                    solrVo.setLabel(drugInstruction.getUseType());
                    solrVo.setCategory(PinYin2Abbreviation.cn2py(drugName));
                }
            }else if(dbObj instanceof DrugPatent){
                DrugPatent drugPatent = (DrugPatent)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugPatent.getId());
                solrVo.setTitle(drugPatent.getPatentName());
                solrVo.setCategoryCode("ypzlwx012");
                solrVo.setDesc("申请专利号:"+drugPatent.getPatentNo()+",申请日期:"+drugPatent.getPatentDate()+","+drugPatent.getApplyer()
                        +","+drugPatent.getInventor()+","+drugPatent.getAddress()+","+drugPatent.getPatentAgency()+","+drugPatent.getAgencyPerson()+","
                        +drugPatent.getPrincipalClaim()+","+drugPatent.getAbstractContent());
                solrVo.setLabel(drugPatent.getPatentNo());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugPatent.getPatentName()));
            }else if(dbObj instanceof DrugAnalysisMethods){
                DrugAnalysisMethods drugAnalysisMethods = (DrugAnalysisMethods)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugAnalysisMethods.getId());
                solrVo.setTitle(drugAnalysisMethods.getMethodName());
                solrVo.setCategoryCode("ywfxff013");
                solrVo.setDesc(drugAnalysisMethods.getUserRealm()+","+drugAnalysisMethods.getMethodPrinciple()+","+drugAnalysisMethods.getEquipment()
                        +","+drugAnalysisMethods.getDemo()+","+drugAnalysisMethods.getOperateStep()+","+drugAnalysisMethods.getReference()+","+drugAnalysisMethods.getMemo());
                solrVo.setLabel(drugAnalysisMethods.getReference());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugAnalysisMethods.getMethodName()));
            }else if(dbObj instanceof DrugExamOrg){
                DrugExamOrg drugExamOrg = (DrugExamOrg)dbObj;
                solrVo = new SolrVo();
                solrVo.setId(drugExamOrg.getId());
                solrVo.setTitle(drugExamOrg.getMedicalOrgName());
                solrVo.setCategoryCode("ywlcsyjg014");
                solrVo.setDesc(drugExamOrg.getMedicalOrgName()+","+drugExamOrg.getMedicalOrgAddress()+","+drugExamOrg.getIdentificationProfession()+",认定日期:"
                        +drugExamOrg.getIdentificationDate());
                solrVo.setLabel(drugExamOrg.getIdentificationProfession());
                solrVo.setCategory(PinYin2Abbreviation.cn2py(drugExamOrg.getMedicalOrgName()));
            }
            if(solrVo!=null){
                baseSolrFacade.addObjectMessageToMq(solrVo);
            }
        }
    }
}
