package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.entity.PolicyResourcesDetail;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.LogHome;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FrontCategorySearchFacade extends BaseFacade {

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 查询一级分类信息
     * @return
     */
    public List<FrontSearchCategory> getFrontFirstCategorys() throws Exception {
        String hql=" from FrontSearchCategory where status <> '-1' and parentId is null";
        return createQuery(FrontSearchCategory.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     *根据分类id查询分类下的子分类
     * @param categoryId
     * @return
     */
    public List<FrontSearchCategory> getFrontChildCategorys(String categoryId) throws Exception {
        String hql=" from FrontSearchCategory where status <> '-1' and parentId ='"+categoryId+"'";
        return createQuery(FrontSearchCategory.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 根据关键字进行分类信息查询
     *
     * @param categoryCode
     * @param keyWords
     * @param exact 是否精确查询 0:否 1:是
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    public Page<SolrVo> getFrontCategorysByKeyWords(String categoryCode, String keyWords,String exact,int perPage, int currentPage) throws Exception {
        Page<SolrVo> solrVoPage = null;
        try {
            String param = "";
            keyWords = keyWords.trim();
            if (StringUtils.isEmptyParam(categoryCode) && StringUtils.isEmptyParam(keyWords)) {
                throw new Exception("参数为空！");
            }
            if (categoryCode != null && !"".equals(categoryCode)) {
                param += "categoryCode:" + categoryCode ;
            }
            keyWords = StringUtils.remeveHtmlLabel(keyWords);
            String hl = "title,desc,label";
            if(StringUtils.isEmptyParam(exact)){//精确查询  如果为空位精确查询 暂定 之前是定义为exact=1
                LogHome.getLog().info("精确查询"+exact);
                String filterStr = "";
                if (categoryCode != null && !"".equals(categoryCode)) {
                    filterStr = "categoryCode:" + categoryCode ;
                }
                solrVoPage = baseSolrFacade.getExactSolrVoByParamAndPageParm(keyWords,filterStr,hl,perPage, currentPage, SolrVo.class);
            }else{//ik分词 智能查询
                LogHome.getLog().info("智能查询"+exact);
                if (keyWords != null && !"".equals(keyWords)) {
                    if(keyWords.indexOf(" ")!=-1){
                        keyWords = "("+keyWords+")";
                    }
                    param += " AND categorykeywords:" + keyWords;
                }
                solrVoPage = baseSolrFacade.getSolrObjectByParamAndPageParm(param,hl,perPage, currentPage, SolrVo.class);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        LogHome.getLog().info("关键字:"+keyWords);
        return solrVoPage;
    }

    /**
     * 根据索引id索引数据信息
     * @param id
     * @return
     * @throws Exception
     */
    public SolrPageVo getSolrVoById(String id) throws Exception{
        if (StringUtils.isEmptyParam(id)) {
            throw new Exception("索引主键不能为空！");
        }
        SolrVo solrVo = baseSolrFacade.getSolrObjectById(id,SolrVo.class);
        SolrPageVo solrPageVo = new SolrPageVo();
        if(solrVo!=null){
            solrPageVo.setId(solrVo.getId());
            solrPageVo.setCategory(solrVo.getCategory());
            solrPageVo.setCategoryCode(solrVo.getCategoryCode());
            solrPageVo.setDesc(solrVo.getDesc());
            solrPageVo.setTitle(solrVo.getTitle());
            solrPageVo.setLabel(solrVo.getLabel());
            if(StringUtils.isEmptyParam(solrVo.getDesc())){//说明详情有分页
                String hql = " from PolicyResourcesDetail where status='1' and relatedPolicyId = '"+solrVo.getId()+"'";
                List<PolicyResourcesDetail> policyResourcesDetails = createQuery(PolicyResourcesDetail.class,hql,new ArrayList<Object>()).getResultList();
                solrPageVo.setDetailList(policyResourcesDetails);
            }
        }
        return solrPageVo;
    }
    public List<FrontSearchCategory> getFrontCategorys() {

        String hql = "from FrontSearchCategory a where a.status<> '-1' order by createDate asc" ;
        List<FrontSearchCategory> list = createQuery(FrontSearchCategory.class, hql, new ArrayList<Object>()).getResultList();
        return list;
    }

    public Page<SolrVo> geRelatedDrugsByKeyWord(String keyWords,int perPage,int currentPage) throws Exception{
        Page<SolrVo> solrVoPage = null;
        try {
            String param = "";
            if(!StringUtils.isEmptyParam(keyWords)){
                keyWords = keyWords.trim();
                keyWords= escapeQueryChars(keyWords);
            }else{
                //return new Page<SolrVo>();
            }
            param += "categoryCode:" + "ywjbxx001" ;
            //keyWords = StringUtils.remeveHtmlLabel(keyWords);
            String hl = "title,desc,label";
            //ik分词 智能查询
            if (keyWords != null && !"".equals(keyWords)) {
                if(keyWords.indexOf(" ")!=-1){
                    keyWords = "("+keyWords+")";
                }
                param += " AND title:" + keyWords;
            }
            solrVoPage = baseSolrFacade.getSolrObjectByParamAndPageParm(param,hl,perPage, currentPage, SolrVo.class);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        LogHome.getLog().info("相关药品搜索关键字:"+keyWords);
        return solrVoPage;
    }
    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!'  || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
                    || c == '*' || c == '?' || c == '|' || c == '&'  || c == ';' || c == '/'
                    || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }
    /**
     * 查询药品分类信息层级信息
     * @param code 分类编码
     * @return
     */
    public Map<String,List<String>> getCategoryNameAndCode(String code){
        Map<String,List<String>> resultMap = new HashMap<>();
        StringBuffer sb = new StringBuffer("select f1.category_name,f1.category_code,f2.category_name as sname,f2.category_code as scode from ");
        sb.append("front_search_category f1,front_search_category f2 where f1.id = f2.parent_id and f1.status = 1");
        if(!StringUtils.isEmptyParam(code)){
            sb.append(" and f1.category_code = '").append(code).append("'");
        }
        sb.append(" order by f1.category_name ");
        List list = createNativeQuery(sb.toString()).getResultList();
        for(Object obj:list){
            Object[] obArray = (Object[])obj;
            String categoryName = (String)obArray[0];
            if(StringUtils.isEmptyParam(code)){
                if(resultMap.containsKey(categoryName)){
                    resultMap.get(categoryName).add((String)obArray[3]);
                }else{
                    resultMap.put(categoryName, Lists.newArrayList((String)obArray[3]));
                }
            }else{
                String categoryName2 = (String)obArray[2];
                if(resultMap.containsKey(categoryName2)){
                    resultMap.get(categoryName2).add((String)obArray[3]);
                }else{
                    resultMap.put(categoryName2, Lists.newArrayList((String)obArray[3]));
                }
            }
        }
        return resultMap;
    }

    public Long getDataCountByCode(List<String> codes,List<DataGroupVo> dataGroupVos){
        if(codes==null || codes.isEmpty()|| dataGroupVos==null || dataGroupVos.isEmpty()){
            return 0L;
        }
        long ct = 0L;
        for(DataGroupVo dataGroupVo:dataGroupVos){
            for(String code:codes){
                if(code.equals(dataGroupVo.getCode())){
                    ct += dataGroupVo.getCount();
                }
            }
        }
        return ct;
    }

    /**
     * 根据查询的分类信息组装返回结果
     * @param resultMap
     * @param dataGroupVos
     * @return
     */
    public List<DrugInfoCountVo> getDrugInfoCountVoList(Map<String,List<String>> resultMap,List<DataGroupVo> dataGroupVos){
        List<DrugInfoCountVo> drugCountryVos = new ArrayList<>();
        for(String key:resultMap.keySet()){
            DrugInfoCountVo dataGroupVo = new DrugInfoCountVo();
            dataGroupVo.setName(key);
            dataGroupVo.setCount(getDataCountByCode(resultMap.get(key),dataGroupVos));
            drugCountryVos.add(dataGroupVo);
        }
        return drugCountryVos;
    }
    /**
     * 查询药物循证，病例库，疾病知识，政策资源，临床实验数据统计数
     * @return
     */
    public List<DrugInfoCountVo> getAllDrugCount() {
        List<DrugInfoCountVo> drugCountryVos = null;
        try {
            List<DataGroupVo> dataGroupVos = baseSolrFacade.getSolrTypeGroupData();
            Map<String,List<String>> resultMap = getCategoryNameAndCode("");
            drugCountryVos = getDrugInfoCountVoList(resultMap,dataGroupVos);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return drugCountryVos;
    }

    /**
     * 查询药物循证下的分类统计数（药物基本信息，药品厂商，药品广告）
     * @return
     */
    public List<DrugInfoCountVo> getDrugEvidenceCount() {
        List<DrugInfoCountVo> drugCountryVos = null;
        try {
            List<DataGroupVo> dataGroupVos = baseSolrFacade.getSolrTypeGroupData();
            Map<String,List<String>> resultMap = getCategoryNameAndCode("ywxz");
            drugCountryVos = getDrugInfoCountVoList(resultMap,dataGroupVos);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return drugCountryVos;
    }
}
