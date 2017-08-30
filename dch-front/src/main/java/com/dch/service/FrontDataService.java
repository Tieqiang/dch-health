package com.dch.service;

import com.dch.entity.FrontSearchCategory;
import com.dch.entity.FrontSearchCategoryField;
import com.dch.facade.FrontDataFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("front/data")
@Produces("application/json")
@Controller
public class FrontDataService {

    @Autowired
    private FrontDataFacade frontDataFacade;

    /**
     * 根据id获取检索分类详细信息
     * @param Id
     * @return
     * @throws Exception
     */
    @Path("get-data-detail")
    @GET
    public FrontSearchCategory getDataDetail(@QueryParam("Id") String Id) throws Exception {
        return frontDataFacade.getDataDetail(Id);
    }

    /**
     * 根据类别id获取分类类型字段信息
     * @param categoryId
     * @return
     */
    @Path("get-data-fields")
    @GET
    public List<FrontSearchCategoryField> getDataFields(@QueryParam("categoryId") String categoryId){
        return frontDataFacade.getDataFields(categoryId);
    }

}
