package com.dch.service;

import com.dch.facade.BaseSolrFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@Produces("application/json")
@Path("drug/drug-solr")
@Controller
public class DrugSolrService {

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 根据搜索内容查询信息
     * @param content 查询内容
     * @param perPage 每页条数
     * @param currentPage 当前页码
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-common-infos")
    public Page<DrugCommonVo>  getDrugCommonVos(@QueryParam("content") String content,@QueryParam("perPage")int perPage,
                                                @QueryParam("currentPage") int currentPage) throws Exception{
        try {
            Page<DrugCommonVo> drugCommonVos = baseSolrFacade.searchDrugCommonVos(content,perPage,currentPage);
            return drugCommonVos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据分类id查询分类下的子分类并匹配关键字
     * @param id
     * @param content
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-child-drug-common-infos")
    public Page<DrugCommonVo>  getChildDrugCommonVos(@QueryParam("id") String id,@QueryParam("content") String content,@QueryParam("perPage")int perPage,
                                                @QueryParam("currentPage") int currentPage) throws Exception{
        try {
            Page<DrugCommonVo> drugCommonVos = baseSolrFacade.searchChildDrugCommonVos(id,content,perPage,currentPage);
            return drugCommonVos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
