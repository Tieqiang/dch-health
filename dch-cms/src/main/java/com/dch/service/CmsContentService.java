package com.dch.service;

import com.dch.entity.CmsCategory;
import com.dch.entity.CmsContent;
import com.dch.entity.CmsContentLabel;
import com.dch.facade.CmsContentFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Path("cms/content")
@Controller
@Produces("application/json")
public class CmsContentService {

    @Autowired
    private CmsContentFacade cmsContentFacade ;


    /**
     * 添加、删除、修改内容信息
     * @param cmsContent
     * @return
     */
    @POST
    @Path("merge-content")
    @Transactional
    public CmsContent mergeContent(CmsContent cmsContent){
        return cmsContentFacade.merge(cmsContent);
    }

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
    @GET
    @Path("get-contents")
    public Page<CmsContent> getContents(@QueryParam("perPage") int perPage,
                                        @QueryParam("currentPage")int currentPage,
                                        @QueryParam("wherehql")String whereHql,
                                        @QueryParam("title")String title,
                                        @QueryParam("categoryId")String categoryId,
                                        @QueryParam("startTime")Timestamp startTime,
                                        @QueryParam("stopTime")Timestamp stopTime){
        return cmsContentFacade.getContents(perPage,currentPage,whereHql,title,categoryId,startTime,stopTime);
    }


    /**
     * 获取单一的内容信息
     * @param contentId
     * @return
     */
    @GET
    @Path("get-content")
    public CmsContent getContent(@QueryParam("contentId")String contentId){
        return cmsContentFacade.getContent(contentId);
    }


    @POST
    @Path("set-content-labels")
    public List<CmsContentLabel> setContentLables(String contentId,List<String> labelNames) throws Exception {
        return cmsContentFacade.setContentLabels(contentId,labelNames);
    }


    /**
     * 根据标签获取
     * @param labelName
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-contents-by-label")
    public Page<CmsContent> getContentsByLabel(@QueryParam("labelName")String labelName,
                                               @QueryParam("perPage")int perPage,
                                               @QueryParam("currentPage")int currentPage){
        return cmsContentFacade.getContentByLabel(labelName,perPage,currentPage);
    }


}
