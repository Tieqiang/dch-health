package com.dch.service;


import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.vo.CreateTableVO;
import com.dch.vo.TableColVO;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.security.provider.certpath.OCSPResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.bson.Document;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

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


    /**
     * 从MongoDb中获取最新的数据dataVersion = dataVersion +1
     * @param tableId
     * @return
     */
    @POST
    @Path("fetch-data")
    public TableColVO fetchTableFromMongo(@QueryParam("tableId")String tableId) throws Exception{
        return tableFacade.fetchTableFromMongo(tableId) ;
    }


    /***
     * 创建用户自定义表
     * @param createTableVO
     * @return
     */
    @POST
    @Path("create-custom-table")
    public Response createCustomTableConfig(CreateTableVO createTableVO){
        try {
            TableConfig tableConfig =  tableFacade.createCustomTableConfig(createTableVO);
            return Response.status(Response.Status.OK).entity(tableConfig).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    /**
     * 根据传入的模板id初始化数据
     * @param templateId
     * @return
     */
    @GET
    @Path("init-all-tables")
    public List<String> initAllTables(@QueryParam("templateId") String templateId){
        List<String> list = new ArrayList<>();
        try {
            tableFacade.fetchMongoToTable(templateId);
            list.add("初始化成功");
        }catch (Exception e){
            list.add("初始化失败："+ e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
