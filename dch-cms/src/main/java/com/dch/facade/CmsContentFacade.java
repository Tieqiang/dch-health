package com.dch.facade;

import com.dch.entity.CmsCategory;
import com.dch.entity.CmsContent;
import com.dch.entity.CmsContentLabel;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
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
     * @return
     */
    public Page<CmsContent> getContents(int perPage, int currentPage, String whereHql,
                                        String title, String categoryId,
                                        Timestamp startTime, Timestamp stopTime) {

        String hql = "from CmsContent as c where c.status<>'-1'" ;
        String hqlCount = "select count(*) from CmsContent as c where c.status<>'-1'" ;

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
            hql+=" and c.startTime >="+startTime;
            hqlCount+=" and c.startTime >="+startTime;
        }
        if(stopTime!=null){
            hql+=" and c.stopTime <="+stopTime;
            hqlCount+=" and c.stopTime <="+stopTime;
        }

        hql+=" order by c.createTime desc" ;
        hqlCount+=" order by c.createTime desc" ;
        Page<CmsContent> cmsContentPage = new Page<>() ;
        TypedQuery<CmsContent> cms = createQuery(CmsContent.class, hql, new ArrayList<Object>());
        Long al = createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        cmsContentPage.setCounts(al);
        cmsContentPage.setPerPage((long) perPage);
        if(perPage<=0){
            cmsContentPage.setData(cms.getResultList());
        }

        if(perPage>0){
            int max = perPage*(currentPage-1) ;
            cms.setFirstResult(max);
            cms.setMaxResults(perPage*currentPage);
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
        String hql = "from CategoryContent as c where c.status<> '-1'" ;
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

    public Page<CmsContent> getContentByLabel(String labelName, int perPage, int currentPage) {
        String hql = "select cc from CmsContent as cc ,CmsContentLabel ccl where cc.id=ccl.contentId and " +
                "ccl.labelName='"+labelName+"'" ;
        String hqlCount = "select cc from CmsContent as cc ,CmsContentLabel ccl where cc.id=ccl.contentId and " +
                "ccl.labelName='"+labelName+"'" ;
        TypedQuery<CmsContent> cmsContentTypedQuery = createQuery(CmsContent.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
        Page<CmsContent> page = new Page<>();
        if(perPage>0){
            cmsContentTypedQuery.setFirstResult(perPage*(currentPage-1));
            cmsContentTypedQuery.setMaxResults(perPage*currentPage);
            page.setData(cmsContentTypedQuery.getResultList());
        }else{
            page.setData(cmsContentTypedQuery.getResultList());
        }
        return page;
    }
}
