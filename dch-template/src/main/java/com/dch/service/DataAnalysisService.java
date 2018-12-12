package com.dch.service;


import com.dch.entity.ReportGroup;
import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.util.JSONUtil;
import com.dch.vo.*;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @param type 报表类型，type为system 为系统初始化的表
     * @return
     */
    @GET
    @Path("get-tables")
    public List<TableConfig> getTableConfig(@QueryParam("templateId") String templateId,@QueryParam("type")String type){
        return tableFacade.getTableConfig(templateId,type) ;
    }

    @GET
    @Path("get-custom-table")
    public TableConfig getTableConfig(@QueryParam("id") String id){

        return tableFacade.getTableConfig(id);
    }


    /**
     * 获取某一个表以及表中的数据
     * @param tableId
     * @return
     */
    @GET
    @Path("get-table-data")
    public TableColVO getTableColVO(@QueryParam("tableId")String tableId, @QueryParam("perPage")int perPage,@QueryParam("currentPage")int currentPage) throws Exception{
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

    /**
     * 添加，修改，删除(逻辑删除，status置为-1)用户自定义报表分组
     * @param reportGroup
     * @return
     */
    @POST
    @Path("merge-report-group")
    public Response mergeCustomerReportGroup(ReportGroup reportGroup) throws Exception{
        return tableFacade.mergeCustomerReportGroup(reportGroup);
    }

    /**
     * 获取用户自定义报表分组
     * @param reportName 报表名称 模糊查询
     * @return
     */
    @GET
    @Path("get-report-group-list")
    public List<ReportGroupVo> getReportGroupVoList(@QueryParam("templateId")String templateId,@QueryParam("reportName")String reportName){
        return tableFacade.getReportGroupVoList(templateId,reportName);
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
            return Response.status(Response.Status.OK).entity(tableFacade.getReportStatistics(reportQueryParam,null)).build();
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

    /**
     * 获取多报表统计结果
     * @param reportParamList
     * @return
     */
    @POST
    @Path("get-many-report-statistics")
    public Response getManyReportStatistics(List<ReportParam> reportParamList) throws Exception{
        try {
            return Response.status(Response.Status.OK).entity(tableFacade.getManyReportStatistics(reportParamList)).build();
        }catch (Exception e){
            List<String> errorList = new ArrayList<>();
            errorList.add(e.getMessage());
            return Response.status(Response.Status.OK).entity(errorList).build();
        }
    }

    /**
     *清洗表数据 按照字段映射值 清洗tableUponFieldVo
     * @param tableUponFieldVo
     * @return
     */
    @POST
    @Path("clean-data-by-table-info")
    public Response cleanDataByTableField(TableUponFieldVo tableUponFieldVo) throws Exception{
        try {
            return Response.status(Response.Status.OK).entity(tableFacade.cleanDataByTableField(tableUponFieldVo)).build();
        }catch (Exception e){
            List<String> errorList = new ArrayList<>();
            errorList.add(e.getMessage());
            return Response.status(Response.Status.OK).entity(errorList).build();
        }
    }

    /**
     * 根据tableName查询表所有的字段（如果系统初始化表结构会发生变化 改用tableName）
     * @param tableId
     * @return
     */
    @GET
    @Path("get-table-col-by-tableId")
    public List<TableColConfig> getTableColList(@QueryParam("tableId")String tableId){
        return tableFacade.getTableColList(tableId);
    }

    /**
     * 查询字段去重后的值
     * @param fieldName
     * @param tableId
     * @return
     */
    @GET
    @Path("get-field-value-list")
    public List getFieldValueList(@QueryParam("fieldName")String fieldName,@QueryParam("tableId")String tableId){
        return tableFacade.getFieldValueList(fieldName,tableId);
    }

    /**
     * 根据templateId判断用户是否填写表单数据
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-fill-info")
    public List<String> getTemplateMasterFillInfo(@QueryParam("templateId")String templateId){
        return tableFacade.getTemplateMasterFillInfo(templateId);
    }

    /**
     * 根据templateId获取用户填报的表单信息
     * @param templateId
     * @return
     */
    @GET
    @Path("get-template-result-text")
    public List<String> getTemplateResultMasterText(@QueryParam("templateId")String templateId){
        return tableFacade.getTemplateResultMasterText(templateId);
    }
}
