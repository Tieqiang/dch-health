package com.dch.service;


import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.util.StringUtils;
import com.dch.vo.CreateTableVO;
import com.dch.vo.TableColVO;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.security.provider.certpath.OCSPResponse;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ComboPooledDataSource dataSource;

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
    public TableColVO fetchTableFromMongo(@QueryParam("tableId")String tableId){
        return null ;
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
            fetchMongoToTable(templateId);
            list.add("初始化成功");
        }catch (Exception e){
            list.add("初始化失败："+ e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析mongo中的数据并生成sql，保存入库
     * @param templateId
     * @return
     */
    public String fetchMongoToTable(String templateId){
        Query query = new Query();
        query.addCriteria(Criteria.where("templateId").is(templateId));
        try {
            List<Document> result = mongoTemplate.find(query,Document.class,"templateFilling");
            Map<String,List<String>> inserSqlMap = new HashMap<>();
            for(int i=0;i<result.size();i++) {
                Document document = (Document) result.get(i);
                initInserSql(document,inserSqlMap);
            }
            saveToDb(inserSqlMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "SUCESS";
    }

    /**
     * 将生成的sql执行保存操作
     * @param inserSqlMap
     * @throws Exception
     */
    public void saveToDb(Map<String,List<String>> inserSqlMap) throws Exception{
        PreparedStatement statement = null;
        try{
            statement = dataSource.getConnection().prepareStatement("");
            for(String key:inserSqlMap.keySet()){
                List<String> insertSqlList = inserSqlMap.get(key);
                if(insertSqlList.size()<=1000){
                    for(String sql:insertSqlList){
                        statement.addBatch(sql);
                    }
                    statement.executeBatch();
                }else{
                    int k=0;
                    for(String sql:insertSqlList){
                        k++;
                        if(k!= 0 && k%1000 == 0){
                            statement.addBatch(sql);
                            statement.executeBatch();
                        }else{
                            statement.addBatch(sql);
                            System.out.println(k);
                        }
                    }
                    statement.executeBatch();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(statement!=null){
                statement.close();
            }
        }
    }

    public Map<String,List<String>> initInserSql(Document document,Map<String,List<String>> inserSqlMap){
        String firstTable = "data_master_gyxwsxykyzxsbxt";
        StringBuffer firstSqlBuf = new StringBuffer("insert into ").append(firstTable).append("(");
        StringBuffer valueSqlBuf = new StringBuffer(") VALUES (");
        for(String key:document.keySet()){
            if("templateId".equals(key)){
                continue;
            }
            if("masterId".equals(key)){
                firstSqlBuf.append("id,");
                valueSqlBuf.append("'").append(document.get(key)).append("',");
            }else{
                Object value = document.get(key);
                if(value instanceof String){
                    String valueStr = value.toString();
                    valueStr = valueStr.replace("'","");
                    firstSqlBuf.append(key).append(",");
                    valueSqlBuf.append("'").append(valueStr).append("',");
                }else if(value.getClass().isArray()){
                    //System.out.println(key);
                }else if(value instanceof List){
                    if(!value.toString().contains("dch")){
                        firstSqlBuf.append(key).append(",");
                        valueSqlBuf.append("'").append(value).append("',");
                    }else{
                        //System.out.println(key);
                        dealListToMap((List)value,key,"","",document.get("masterId").toString(),inserSqlMap);
                    }
                }else if(value instanceof Long){
                    firstSqlBuf.append(key).append(",");
                    valueSqlBuf.append("'").append(value).append("',");
                }else if(value instanceof Integer){
                    firstSqlBuf.append(key).append(",");
                    valueSqlBuf.append("'").append(value).append("',");
                }
            }
        }
        String firstSqlStr = firstSqlBuf.toString();
        String valueSqlStr = valueSqlBuf.toString();
        String inserSql = firstSqlStr.substring(0,firstSqlStr.length()-1) + valueSqlStr.substring(0,valueSqlStr.length()-1) + ")";
        if(inserSqlMap.containsKey(firstTable)){
            List<String> insertList = inserSqlMap.get(firstTable);
            insertList.add(inserSql);
        }else{
            List<String> insertList = new ArrayList<>();
            insertList.add(inserSql);
            inserSqlMap.put(firstTable, insertList);
        }
        return inserSqlMap;
    }

    public void dealListToMap(List list,String tableName,String parentKey,String parentId,String masterId,Map<String,List<String>> inserSqlMap){
        if(list!=null && !list.isEmpty()){
            Integer version = getTableVersion(tableName);
            if(version==null){
                version = 0;
            }else{
                version = version + 1;
            }
            for(int i=0;i<list.size();i++){
                String uuid = getUID();
                StringBuffer firstSqlBuf = new StringBuffer("insert into ").append(tableName).append("(id,data_version,");
                StringBuffer valueSqlBuf = new StringBuffer(") VALUES (").append("'").append(uuid).append("',").append(version).append(",");
                if(!StringUtils.isEmptyParam(parentKey)){
                    firstSqlBuf.append(parentKey).append(",");
                    valueSqlBuf.append("'").append(parentId).append("',");
                }else{
                    firstSqlBuf.append("master_id").append(",");
                    valueSqlBuf.append("'").append(masterId).append("',");
                }
                Document document = (Document)list.get(i);
                for(String key:document.keySet()){
                    Object value = document.get(key);
                    if(value instanceof String){
                        String valueStr = value.toString();
                        valueStr = valueStr.replace("'","");
                        firstSqlBuf.append(key).append(",");
                        valueSqlBuf.append("'").append(valueStr).append("',");
                    }else if(value.getClass().isArray()){

                    }else if(value instanceof List){
                        if(!value.toString().contains("dch")){
                            firstSqlBuf.append(key).append(",");
                            valueSqlBuf.append("'").append(value).append("',");
                        }else{
                            //System.out.println(key);
                            String parentCol = tableName+"_id";
                            dealListToMap((List)value,key,parentCol,uuid,"",inserSqlMap);
                        }
                    }else if(value instanceof Long){
                        firstSqlBuf.append(key).append(",");
                        valueSqlBuf.append("'").append(value).append("',");
                    }else if(value instanceof Integer){
                        firstSqlBuf.append(key).append(",");
                        valueSqlBuf.append("'").append(value).append("',");
                    }
                }
                String firstSqlStr = firstSqlBuf.toString();
                if(firstSqlStr.contains("@")){
                    firstSqlStr = firstSqlStr.replace("@","$");
                }
                String valueSqlStr = valueSqlBuf.toString();
                String inserSql = firstSqlStr.substring(0,firstSqlStr.length()-1) + valueSqlStr.substring(0,valueSqlStr.length()-1) + ")";
                if(inserSqlMap.containsKey(tableName)){
                    List<String> insertList = inserSqlMap.get(tableName);
                    insertList.add(inserSql);
                }else{
                    List<String> insertList = new ArrayList<>();
                    insertList.add(inserSql);
                    inserSqlMap.put(tableName, insertList);
                }
            }
        }
    }

    public Integer getTableVersion(String tableName){
        String sql = "select max(data_version) as version from "+ tableName +" where 1=1";
        List<Integer> colList = tableFacade.createNativeQuery(sql).getResultList();
        if(colList!=null && !colList.isEmpty()){
            return colList.get(0);
        }
        return null;
    }

    public String getUID(){
        String uid = UUID.randomUUID().toString().replaceAll("-","");
        return uid;
    }
}
