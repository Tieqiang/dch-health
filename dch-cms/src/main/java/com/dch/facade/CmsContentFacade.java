package com.dch.facade;

import com.dch.entity.CmsCategory;
import com.dch.entity.CmsContent;
import com.dch.entity.CmsContentLabel;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Component
public class CmsContentFacade extends BaseFacade{

    /**
     * 获取新闻列表
     * @param perPage         每页显示条数
     * @param currentPage     当前页
     * @param whereHql        前端传递的其他条件
     * @param title           标题，进行模糊匹配
     * @param categoryId      分类
     * @param startTime       时间范围创建时间大于等于这个时间
     * @param stopTime        时间范围创建时间小于等于这个时间
     * @param pubStatus
     * @param pubFilterFlag
     * @return
     */
    public Page<CmsContent> getContents(int perPage, int currentPage, String whereHql,
                                        String title, String categoryId,
                                        Timestamp startTime, Timestamp stopTime, String pubStatus, String pubFilterFlag) {


        String hql = "from CmsContent as c where c.status<>'-1' " ;
        String hqlCount = "select count(*) from CmsContent as c where c.status<>'-1'" ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if(!"0".equals(pubFilterFlag)){
            //表示不根据发布时间进行过滤
            hql+=" and c.pubStatus='1' and c.pubTime<=:nowDate" ;
            hqlCount+=" and c.pubStatus='1' and c.pubTime<=:nowDate";
        }

        if(!strIsNull(whereHql)){
            hql+=whereHql;
            hqlCount+=whereHql;
        }

        if(!strIsNull(title)){
            hql+=" and c.title like '"+title+"'" ;
            hqlCount+= " and c.title like '"+title+"'" ;
        }

        if (!strIsNull(categoryId)){
            hql+=" and c.categoryId='"+categoryId+"'";
            hqlCount+=" and c.categoryId='"+categoryId+"'";
        }

        if(startTime!=null){
            hql+=" and c.createDate >=:startDate";
            hqlCount+=" and c.createDate >=:startDate";
        }
        if(stopTime!=null){
            hql+=" and c.createDate <=:stopDate";
            hqlCount+=" and c.createDate <=:stopDate";
        }
        if(!strIsNull(pubStatus)){
            hql+=" and c.pubStatus="+pubStatus;
            hqlCount+=" and c.pubStatus="+pubStatus;
        }

        hql+=" order by c.pubTime desc" ;
        hqlCount+=" order by c.pubTime desc" ;
        Page<CmsContent> cmsContentPage = new Page<>() ;
        TypedQuery<CmsContent> cms = createQuery(CmsContent.class, hql, new ArrayList<Object>());
        TypedQuery<Long> longTypedQuery = createQuery(Long.class, hqlCount, new ArrayList<Object>());
        if(!"0".equals(pubFilterFlag)){
            //不过滤
            cms.setParameter("nowDate",new Timestamp(new Date().getTime()),TemporalType.TIMESTAMP);
            longTypedQuery.setParameter("nowDate",new Timestamp(new Date().getTime()),TemporalType.TIMESTAMP);
        }

        if(startTime!=null){
            cms.setParameter("startDate",startTime,TemporalType.TIMESTAMP);
            longTypedQuery.setParameter("startDate",startTime,TemporalType.TIMESTAMP);
        }
        if(stopTime!=null){
            cms.setParameter("stopDate",stopTime,TemporalType.TIMESTAMP);
            longTypedQuery.setParameter("stopDate",stopTime,TemporalType.TIMESTAMP);
        }

        Long al = longTypedQuery.getSingleResult();
        cmsContentPage.setCounts(al);
        cmsContentPage.setPerPage((long) perPage);
        if(perPage<=0){
            cmsContentPage.setData(cms.getResultList());
        }

        if(perPage>0){
            int max = perPage*(currentPage-1) ;
            cms.setFirstResult(max);
            cms.setMaxResults(perPage);
            cmsContentPage.setData(cms.getResultList());
        }

        return cmsContentPage;
    }

    /**
     * 判读是否为空
     * @param whereHql
     * @return
     */
    private boolean strIsNull(String whereHql) {
        return "".equals(whereHql)||whereHql==null;
    }

    /**
     * 获取具体的文章
     * @param contentId
     * @return
     */
    public CmsContent getContent(String contentId) {
        String hql = "from CmsContent as c where c.status<> '-1' and c.id='"+contentId+"'" ;
        CmsContent cmsContent = createQuery(CmsContent.class, hql, new ArrayList<Object>()).getSingleResult();
        return cmsContent;
    }

    /**
     *  设置内容标签
     * @param contentId
     * @param labelNames
     * @return
     */
    @Transactional
    public List<CmsContentLabel> setContentLabels(String contentId, List<String> labelNames) throws Exception {

        if(strIsNull(contentId)){
            throw new Exception("内容标识不能为空");
        }

        String delHql = "delete from CmsContentLabel where contentId='"+contentId+"'" ;
        excHql(delHql);
        List<CmsContentLabel> cmsContentLabels = new ArrayList<>() ;

        for(String labelName:labelNames){
            CmsContentLabel label = new CmsContentLabel();
            label.setContentId(contentId);
            label.setLabelName(labelName);
            cmsContentLabels.add(merge(label));
        }



        return cmsContentLabels;
    }

    public Page<CmsContent> getContentByLabel(String labelName, int perPage, int currentPage, String categoryId) {
        String hql = "select cc from CmsContent as cc ,CmsContentLabel ccl where cc.id=ccl.contentId and cc.status<>'-1' and " +
                "ccl.labelName='"+labelName+"'" ;
        String hqlCount = "select count(cc) from CmsContent as cc ,CmsContentLabel ccl where cc.id=ccl.contentId and cc.status<>'-1' and " +
                "ccl.labelName='"+labelName+"'" ;

        if(categoryId!=null&&!"".equals(categoryId)){
            hql+=" and cc.categoryId='"+categoryId+"'";
            hql+=" and cc.categoryId='"+categoryId+"'";
        }
        TypedQuery<CmsContent> cmsContentTypedQuery = createQuery(CmsContent.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page<CmsContent> page = new Page<>();
        if(perPage>0){
            cmsContentTypedQuery.setFirstResult(perPage*(currentPage-1));
            cmsContentTypedQuery.setMaxResults(perPage);
            page.setData(cmsContentTypedQuery.getResultList());
        }else{
            page.setData(cmsContentTypedQuery.getResultList());
        }
        return page;
    }

    public List<CmsContentLabel> getContentLabels(String contentId) {
        String hql = "from CmsContentLabel where contentId='"+contentId+"'";
        return createQuery(CmsContentLabel.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *
     * @param categoryId 所属分类
     * @param preFlag    1，表示下一条新闻,0表示上一条新闻
     * @param currentId  当前新闻
     * @return
     */
    public CmsContent getContentByFlag(String categoryId, String currentId, String preFlag) throws Exception {

        CmsContent curr = get(CmsContent.class,currentId) ;
        if(curr==null){
            throw new Exception("获取当前新闻信息失败");
        }

        String hql = "from CmsContent as cc where cc.status<> '-1'" ;

        if("1".equals(preFlag)){
            hql+=" and cc.createDate>:theTime order by cc.createDate asc" ;
        }else if("0".equals(preFlag)){
            hql+=" and cc.createDate<:theTime order by cc.createDate desc" ;
        }else{
            throw new Exception("preFlag 参数有误");
        }

        TypedQuery<CmsContent> cmsContentTypedQuery = createQuery(CmsContent.class, hql, new ArrayList<Object>());
        cmsContentTypedQuery.setParameter("theTime",curr.getCreateDate(),TemporalType.TIMESTAMP);

        List<CmsContent> resultList = cmsContentTypedQuery.getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            return null;
        }

    }
}
