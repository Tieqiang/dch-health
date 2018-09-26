package com.dch.service;


import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.vo.TableColVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.List;

@Controller
@Produces("application/json")
@Path("data-analysis")
public class DataAnalysisService {



    @Autowired
    private TableFacade tableFacade;

    @GET
    @Path("create-tables")
    public List<TableConfig> createTableConfig(@QueryParam("templateId")String templateId) throws Exception {

        return tableFacade.createTableConfig(templateId);

    }


    /**
     * 获取所有的表格
     * @param templateId
     * @return
     */
    @GET
    @Path("get-tables")
    public List<TableConfig> getTableConfig(@QueryParam("templateId") String templateId){

        return tableFacade.getTableConfig(templateId) ;
    }


    /**
     * 获取某一个表以及表中的数据
     * @param tableId
     * @return
     */
    @GET
    @Path("get-table-data")
    public TableColVO getTableColVO(@QueryParam("tableId")String tableId){
        return tableFacade.getTableColVO(tableId) ;
    }



}
