package com.dch.facade;

import com.dch.entity.FrontSearchCategory;
import com.dch.entity.PolicyResourcesDetail;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.LogHome;
import com.dch.util.StringUtils;
import com.dch.vo.SolrPageVo;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
}
