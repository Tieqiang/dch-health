package com.dch.service;

import com.dch.entity.DiseaseContent;
import com.dch.entity.DiseaseNameDict;
import com.dch.facade.BaseSolrFacade;
import com.dch.facade.DiseaseContentFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
@Produces("application/json")
@Path("disease/content")
@Controller
public class DiseaseContentService {

    @Autowired
    private DiseaseContentFacade diseaseContentFacade ;

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 添加 、删除、修改分类
     * @param diseaseContent
     * @return
     */
    @POST
    @Path("merge-content")
    @Transactional
    public Response mergeDiseaseContent(DiseaseContent diseaseContent){
        DiseaseContent content = diseaseContentFacade.merge(diseaseContent);

        SolrVo solrVo = new SolrVo();
        solrVo.setDesc(content.getName()+"\b\r"+content.getContent());
        solrVo.setCategoryCode("jbzs");
        solrVo.setTitle(content.getName());
        solrVo.setCategory(PinYin2Abbreviation.cn2py(content.getName()));
        solrVo.setFirstPy(PinYin2Abbreviation.getFirstPy(solrVo.getCategory()));
        baseSolrFacade.addObjectMessageToMq(solrVo);

        return Response.status(Response.Status.OK).entity(content).build();
    }

    /**
     * 获取疾病知识列表
     * @param categoryId
     * @param name
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-disease-contents")
    public Page<DiseaseContent> getDiseaseContents(@QueryParam("categoryId") String categoryId,
                                                   @QueryParam("name") String name,
                                                   @QueryParam("perPage") int perPage,
                                                   @QueryParam("currentPage") int currentPage,
                                                   @QueryParam("createBy") String createBy){
        return diseaseContentFacade.getDiseaseContents(categoryId,name,perPage,currentPage,createBy);
    }

    /**
     * 根据ID获取具体的疾病知识
     * @param contentId
     * @return
     */
    @GET
    @Path("get-disease-content")
    public DiseaseContent getDiseaseContent(@QueryParam("contentId") String contentId){
        DiseaseContent diseaseContent = diseaseContentFacade.get(DiseaseContent.class, contentId);
        if(diseaseContent!=null&&!"-1".equals(diseaseContent.getStatus())){
            return diseaseContent ;
        }else{
            return null ;
        }
    }

    /**
     * 获取别名
     * @param contentId
     * @return
     */
    @GET
    @Path("get-disease-content-names")
    public List<DiseaseNameDict> getDiseaseContentNames(@QueryParam("contentId") String contentId){
        return diseaseContentFacade.getDiseaseContentNames(contentId);
    }


    /**
     * 添加、删除、修改别名
     * @param diseaseNameDict
     * @return
     */
    @POST
    @Path("merge-disease-content-name")
    @Transactional
    public Response mergeDiseaseContentName(DiseaseNameDict diseaseNameDict){
        DiseaseNameDict merge = diseaseContentFacade.merge(diseaseNameDict);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据项目获取疾病知识
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-disease-content-by-projectId")
    public Page<DiseaseContent> getDiseaseContentsByProjectId(@QueryParam("projectId") String projectId,
                                                              @QueryParam("perPage") int perPage,
                                                              @QueryParam("currentPage") int currentPage){
        return diseaseContentFacade.getDiseaseContentsByProjectId(projectId,perPage,currentPage);
    }

}
