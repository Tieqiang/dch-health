package com.dch.service;


import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.vo.*;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.security.provider.certpath.OCSPResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.bson.Document;
import javax.ws.rs.*;
import javax.ws.rs.QueryParam;
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
    public TableColVO getTableColVO(@QueryParam("tableId")String tableId, @QueryParam("perPage")int perPage,@QueryParam("currentPage")int currentPage){
        return tableFacade.getTableColVO(tableId, perPage, currentPage);
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

    /**
     * 获取报表统计结果
     * @param reportQueryParam
     * @return
     */
    @POST
    @Path("get-report-statistics")
    public Response getReportStatistics(ReportQueryParam reportQueryParam) throws Exception{
        try {
            List<UnitFunds> mongoResultVoList = tableFacade.getReportStatistics(reportQueryParam);
            return Response.status(Response.Status.OK).entity(mongoResultVoList).build();
        }catch (Exception e){
            List<String> errorList = new ArrayList<>();
            errorList.add(e.getMessage());
            return Response.status(Response.Status.OK).entity(errorList).build();
        }
    }

    /**
     * 根据表id删除用户自定义报表
     * @param tableId
     * @return
     */
    @POST
    @Path("del-customer-table")
    public Response delCustomerDefineTable(@QueryParam("tableId") String tableId){
        ReturnInfo returnInfo = tableFacade.delCustomerDefineTable(tableId);
        return Response.status(Response.Status.OK).entity(returnInfo).build();
    }
}
