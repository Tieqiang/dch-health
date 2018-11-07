package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.util.*;
import com.dch.vo.*;
import com.dch.vo.OperationEnum;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.annotation.DeclareWarning;
import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.hibernate.annotations.Synchronize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TableFacade extends BaseFacade {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TemplateDataElementFacade templateDataElementFacade;

    @Autowired
    private TemplateMasterFacade templateMasterFacade;

    @Autowired
    private ComboPooledDataSource dataSource;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 生成表单结构
     *
     * @param templateId
     * @return
     */
    @Transactional
    public List<TableConfig> createTableConfig(String templateId) throws Exception {
        List<TableConfig> tableConfigs = new ArrayList<>();
        Connection connection = null;
        try {
            List<TemplateDataElement> templateDataElements = templateDataElementFacade.getTemplateDataElements(templateId);
            //删除之前的表
            String hql = "from TableConfig as t where t.formId='" + templateId + "'";
            List<TableConfig> configs = createQuery(TableConfig.class, hql, new ArrayList<Object>()).getResultList();
            List<String> tableIds = new ArrayList<>();
            for (TableConfig tableConfig : configs) {
                tableIds.add(tableConfig.getId());
                //excHql(delHql);
                remove(tableConfig);
            }
            if(!tableIds.isEmpty()){
                String tableIdIns = StringUtils.getQueryIdsString(tableIds);
                String delHql = "delete TableColConfig where tableId in (" + tableIdIns + ")";
                excHql(delHql);
            }
            //删除用户自定义表
            String upHql = "update ReportGroup set status = '-1' where parentId is not null and templateId = '"+templateId+"'";
            excHql(upHql);
            //删除用户自定义统计分析表
            String temHql = "update TemplateQueryRule set status = '-1' where parentId is not null and templateId = '"+templateId+"'";
            excHql(temHql);

            //创建表结构
            List<TableCreateVo> vos = new ArrayList<>();
            List<TemplateDataElement> firstLevelDataElement = new ArrayList<>();
            List<TableCreateVo> tableCreateVos = null;
            TableCreateVo masterTableCreateVo = null;
            int i = 0;
            for (TemplateDataElement vo : templateDataElements) {
                logger.info("循环1：" + i + ":" + vo.getDataElementName());
                if (StringUtils.isEmptyParam(vo.getParentDataId())) {
//                firstLevelDataElement.add(vo);
                    String dataElementType = vo.getDataElementType();
                    if (dataElementType.contains("table")) {
                        tableCreateVos = createNextTable(vo, templateDataElements, templateId, "master");
                        vos.addAll(tableCreateVos);
                    } else {
                        firstLevelDataElement.add(vo);
                    }

                }
                i++;
            }
            masterTableCreateVo = createMasterTAble(firstLevelDataElement, templateId);
            vos.add(masterTableCreateVo);

            connection = dataSource.getConnection();
            for (TableCreateVo vo : vos) {
                String insertSql = vo.getInsertSql();
//            Statement statement = dataSource.getConnection().createStatement();
//            statement.execute(insertSql);
                String[] sqls = insertSql.split(";");
                for (String sql : sqls) {
                    logger.info("执行sql：" + sql);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.execute();
                    preparedStatement.close();
                }

                TableConfig merge = this.merge(vo.getTableConfig());
                tableConfigs.add(merge);
                for (TableColConfig config : vo.getTableColConfigList()) {
                    config.setTableId(merge.getId());
                    this.merge(config);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if(connection!=null){
                connection.close();
            }
        }
        return tableConfigs;
    }

    private TableCreateVo createMasterTAble(List<TemplateDataElement> firstLevelDataElement, String templateId) throws Exception {

        TemplateMaster templateMaster = templateMasterFacade.getTemplateMaster(templateId);
        String templateName = templateMaster.getTemplateName();
        String tableName = "data_master_" + PinYin2Abbreviation.cn2py(templateName);
        List<TableColConfig> tableColConfigs = new ArrayList<>();
        TableCreateVo tableCreateVo = new TableCreateVo();
        String createSql = "drop table if EXISTS   " + tableName + ";" +
                "create table " + tableName + "(" +
                " id varchar(200) NOT NULL comment '主键' ,";

        TableColConfig tableColConfig = new TableColConfig();
        tableColConfig.setColCode("id");
        tableColConfig.setColName("主键");
        tableColConfig.setColDescription(templateName + "主键");
        tableColConfig.setDataVersion(0);
        tableColConfigs.add(tableColConfig);

        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableName(tableName);
        tableConfig.setTableDesc(templateName + "数据");
        tableConfig.setFormId(templateId);
        tableConfig.setCreateFrom("system");
        tableCreateVo.setTableConfig(tableConfig);


        for (TemplateDataElement data : firstLevelDataElement) {
            TableColConfig tableCol = new TableColConfig();
            tableCol.setColCode(data.getDataElementCode());
            tableCol.setColName(data.getDataElementName());
            tableCol.setColDescription(data.getDataElementName());
            tableCol.setDataVersion(0);
            tableColConfigs.add(tableCol);
            createSql = createSql + "" + data.getDataElementCode() + " varchar(200) comment '" + data.getDataElementName() + "',";
        }

        createSql = createSql + "  PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        tableCreateVo.setInsertSql(createSql);
        tableCreateVo.setTableColConfigList(tableColConfigs);

        return tableCreateVo;

    }


    private List<TableCreateVo> createNextTable(TemplateDataElement vo, List<TemplateDataElement> data, String templateId, String master) {

        logger.info("创建子表：" + vo);
        List<TableCreateVo> vos = new ArrayList<>();
        List<TableColConfig> tableColConfigs = new ArrayList<>();

        String sql = "drop table if EXISTS  ";
        String tableName = vo.getDataElementCode();
        sql += " " + tableName + ";";
        sql = sql + " create table " + tableName + " (" +
                "id varchar(200) NOT NULL comment '主键' ," +
                "" + master + "_id varchar(200) comment '外键与主记录',";
        TableColConfig tableColConfig = new TableColConfig();
        tableColConfig.setColCode("id");
        tableColConfig.setColName("主键");
        tableColConfig.setColDescription(vo.getDataElementName() + "主键");
        tableColConfig.setDataVersion(0);
        tableColConfigs.add(tableColConfig);

        TableColConfig tableColConfigName = new TableColConfig();
        tableColConfigName.setColCode(master + "_id");
        tableColConfigName.setColName("外键");
        tableColConfigName.setColDescription(vo.getDataElementName() + "外键");
        tableColConfigName.setDataVersion(0);
        tableColConfigs.add(tableColConfigName);


        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableName(tableName);
        tableConfig.setTableDesc(vo.getDataElementName());
        tableConfig.setFormId(templateId);
        tableConfig.setCreateFrom("system");

        for (TemplateDataElement elementVo : data) {
            if (vo.getId().equals(elementVo.getParentDataId())) {
                if (elementVo.getDataElementType().contains("table")) {
                    List<TableCreateVo> nextTable = createNextTable(elementVo, data, templateId, vo.getDataElementCode());
                    vos.addAll(nextTable);
                } else {
                    TableColConfig tableCol = new TableColConfig();
                    tableCol.setColCode(elementVo.getDataElementCode());
                    tableCol.setColName(elementVo.getDataElementName());
                    tableCol.setColDescription(elementVo.getDataElementName());
                    tableCol.setDataVersion(0);
                    tableColConfigs.add(tableCol);
                    sql += "" + elementVo.getDataElementCode() + " varchar(1700) comment '" + elementVo.getDataElementName() + "',";
                }
            }
        }
        sql = sql + " data_version int DEFAULT 0  comment '版本',";
        sql = sql + "  PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '" +
                vo.getDataElementName() + "';";

        logger.info("创年SQL：" + sql);
        TableCreateVo tableCreateVo = new TableCreateVo();
        tableCreateVo = new TableCreateVo();
        tableCreateVo.setInsertSql(sql);
        tableCreateVo.setTableConfig(tableConfig);
        tableCreateVo.setTableColConfigList(tableColConfigs);
        vos.add(tableCreateVo);
        return vos;
    }


    public List<TableConfig> getTableConfig(String templateId,String type) {
        String hql = "from TableConfig as t where t.formId='" + templateId + "'";
        if(!StringUtils.isEmptyParam(type)){
            hql += " and t.createFrom = '"+type+"'";
        }
        hql += " order by t.tableName asc";
        return createQuery(TableConfig.class, hql, new ArrayList<Object>()).getResultList();
    }

    public TableColVO getTableColVO(String tableId, int perPage, int currentPage) throws Exception{
        String hql = "from TableColConfig where tableId='" + tableId + "'";
        List<TableColConfig> resultList = createQuery(TableColConfig.class, hql, new ArrayList<Object>()).getResultList();
        if(resultList == null || resultList.isEmpty()){
            throw new Exception("表不存在，获取数据失败");
        }
        TableConfig tableConfig = this.get(TableConfig.class, tableId);
        String tableName = tableConfig.getTableName();
        String sqlCount = "select count(*) ";
        String sql = "select ";
        if(resultList.isEmpty()){
            throw new Exception("表字段为空，获取数据失败");
        }else{
            for (TableColConfig config : resultList) {
                sql += config.getColCode() + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " from " + tableConfig.getTableName();
        sqlCount += " from " + tableConfig.getTableName();
        if (sql.contains("data_version")) {//sql.contains("data_version")
            sql += " where data_version = (select max(data_version) from " + tableName + ")";
            sqlCount += " where data_version = (select max(data_version) from " + tableName + ")";
        }
        perPage = perPage < 1?20:perPage;
        currentPage = currentPage< 1?1:currentPage;
        long limit_start = (currentPage -1) * perPage;
        long limit_end = perPage;
        sql += " limit " + limit_start + ","+limit_end;
        logger.info(sql);

        List<Object[]> datas = null;
        if(resultList.size() < 2){
            datas = new ArrayList<>();
            List list = this.createNativeQuery(sql).getResultList();
            for(Object k:list){
                Object[] obj = new Object[]{k};
                datas.add(obj);
            };
        }else{
            datas = this.createNativeQuery(sql).getResultList();
        }
        BigInteger totalNum = (BigInteger)this.createNativeQuery(sqlCount).getResultList().get(0);
        TableColVO tableColVO = new TableColVO();
        tableColVO.setDatas(datas);
        tableColVO.setTotalNum(totalNum.longValue());
        tableColVO.setTableColConfigs(resultList);
        return tableColVO;
    }

    /***
     * 创建用户临时表
     * @param createTableVO
     * @return
     */
    @Transactional
    public TableConfig createCustomTableConfig(CreateTableVO createTableVO) throws SQLException {

        TableConfig tableConfig = createTableVO.getTableConfig();
        tableConfig.setCreateFrom("user");
        tableConfig.setTableDesc(tableConfig.getTableName());

        Connection connection = this.dataSource.getConnection();
        try {
            String executeSQL = createExecuteSQL(createTableVO.getUserCustomTableVOs(), createTableVO.getOperationConditionVOS());
            List<TableColConfig> tableColConfigs = createTAbleColConfigs(createTableVO.getUserCustomTableVOs(), createTableVO.getOperationConditionVOS());
            String tableName =  createUserDefineTable(tableConfig.getTableName());
            String createTableSql = createCreateCustomTableSQL(tableColConfigs, tableName);

            String[] split = createTableSql.split(";");
            for (String sql : split) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                logger.info("开始执行SQL：" + sql);
                preparedStatement.execute();
                preparedStatement.close();
            }

            tableConfig.setTableName(tableName);
            tableConfig.setExecuteSql(executeSQL);
            tableConfig.setCreateDate(new Timestamp(new Date().getTime()));
            tableConfig.setModifyDate(new Timestamp(new Date().getTime()));
            TableConfig merge = merge(tableConfig);
            logger.info("保存自定义表信息成功！");
            for (TableColConfig config : tableColConfigs) {
                config.setTableId(merge.getId());
                merge(config);
            }
            logger.info("保存自定义字段成功");
            //将初始化用户自定义表数据
            List queryList = createNativeQuery(executeSQL).getResultList();
            saveResultToDb(tableName,0, executeSQL, queryList);
            //添加用户报表到自定义的报表分组下2018-10-19
            ReportGroup reportGroup = new ReportGroup();
            reportGroup.setParentId(createTableVO.getParentId());
            reportGroup.setReportName(merge.getTableDesc());
            reportGroup.setTableId(merge.getId());
            reportGroup.setStatus("1");
            reportGroup.setTemplateId(tableConfig.getFormId());
            merge(reportGroup);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭链接
            connection.close();
        }
        return tableConfig;
    }

    /**
     * 查询表是否存在
     * @param tableName
     * @return
     */
    public List<String> queryTableExist(String tableName){
        String sql = "select table_name from table_config where table_desc = '"+tableName+"'";
        List list = createNativeQuery(sql).getResultList();
        return list;
    }

    /**
     * 生成表名
     * @param tableName
     * @return
     */
    public synchronized String createUserDefineTable(String tableName){
        String table = "USER_CUSTOM_";
        List list = queryTableExist(tableName);
        if(!list.isEmpty()){
            if(list.size()>1){
                String orignTable = table+PinYin2Abbreviation.cn2py(tableName);
                list.remove(orignTable);
                Map<String,Integer> map = new HashMap();
                list.stream().forEach(o -> {
                    String tindex = o.toString().replace(orignTable+"_","");
                    Integer index = Integer.valueOf(tindex);
                    if(map.containsKey(orignTable) && map.get(orignTable)<index){
                        map.put(orignTable,index);
                    }
                    if(map.isEmpty()){
                        map.put(orignTable,index);
                    }
                });
                table += PinYin2Abbreviation.cn2py(tableName) + "_" + (map.get(orignTable)+1);
            }else{
                table += PinYin2Abbreviation.cn2py(tableName)+ "_1";
            }
        }else{
            table += PinYin2Abbreviation.cn2py(tableName);
        }
        return table;
    }
    /**
     * 生成建表语句
     *
     * @param tableColConfigs
     * @param tableName
     * @return
     */
    private String createCreateCustomTableSQL(List<TableColConfig> tableColConfigs, String tableName) {

        String SQL = "drop table if EXISTS " + tableName + ";";
        SQL = SQL + " create table " + tableName + " (";
        for (TableColConfig config : tableColConfigs) {
            if ("id".equals(config.getColCode())) {
                SQL += "" + config.getColCode() + " varchar(64) comment '" + config.getColName() + "',";
            } else {
                SQL += "" + config.getColCode() + " varchar(1600) comment '" + config.getColName() + "',";
            }

        }
//        SQL = SQL + " data_version int DEFAULT 0  comment '版本',";
        SQL = SQL + "  PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        return SQL;
    }

    /**
     * 创建表的列
     *
     * @param userCustomTableVOs
     * @param operationConditionVOS
     * @return
     */
    private List<TableColConfig> createTAbleColConfigs(List<UserCustomTableVO> userCustomTableVOs, List<OperationConditionVO> operationConditionVOS) {

        List<TableColConfig> colConfigs = new ArrayList<>();
        for (UserCustomTableVO customTableVO : userCustomTableVOs) {
            List<TableColConfig> tableColConfigs = customTableVO.getTableColConfigs();
            for (TableColConfig config : tableColConfigs) {
                TableColConfig colConfig = new TableColConfig();
                colConfig.setColName(config.getColName());
                colConfig.setColDescription(config.getColDescription());
                colConfig.setColCode(config.getColCode());
                colConfigs.add(colConfig);
            }
        }

        TableColConfig colConfig = new TableColConfig();
        colConfig.setColCode("id");
        colConfig.setColName("主键");
        colConfig.setColDescription("自定义主键");
        colConfigs.add(colConfig);

        TableColConfig colConfig2 = new TableColConfig();
        colConfig2.setColCode("data_version");
        colConfig2.setColName("版本号");
        colConfig2.setColDescription("版本号");
        colConfigs.add(colConfig2);

        return colConfigs;
    }

    /**
     * 创建表执行的sql
     *
     * @param userCustomTableVOs
     * @param operationConditionVOS
     * @return
     */
    private String createExecuteSQL(List<UserCustomTableVO> userCustomTableVOs, List<OperationConditionVO> operationConditionVOS) {
        String sql = "select ";
        String from = " from ";
        String condition = "  where 1=1 and ";
        Map<String,String> tableInfo = new HashMap<>();
        for (UserCustomTableVO vo : userCustomTableVOs) {
            List<TableColConfig> tableColConfigs = vo.getTableColConfigs();
            TableConfig tableConfig = vo.getTableConfig();
            from += tableConfig.getTableName() + ",";
            for (TableColConfig colConfig : tableColConfigs) {
                sql += " " + tableConfig.getTableName() + "." + colConfig.getColCode() + ",";
            }
            tableInfo.put(tableConfig.getId(),tableConfig.getTableName());
        }
        sql = sql.substring(0, sql.length() - 1);
        from = from.substring(0, from.length() - 1);

        for (OperationConditionVO conditionVO : operationConditionVOS) {
            OperationEnum operationEnum = conditionVO.getOperationEnum();
            TableColConfig firstTableColConfig = conditionVO.getFirstTableColConfig();
            condition += " " + tableInfo.get(firstTableColConfig.getTableId()) + "." + firstTableColConfig.getColCode();
            switch (operationEnum) {
                case IN:
                    condition += " in (";
                    for (String str : conditionVO.getInValues()) {
                        condition += "'" + str + "',";
                    }
                    condition = condition.substring(0, condition.length() - 1);
                    condition += " ) " + conditionVO.getNextOperation();
                    break;
                case EQUAL:
                    condition += this.buildCondition("=", conditionVO, tableInfo);
                    break;
                case GRATER_THAN:
                    condition += this.buildCondition(">", conditionVO, tableInfo);
                    break;
                case LESS_THAN:
                    condition += this.buildCondition("<", conditionVO, tableInfo);
                    break;
                case LESS_OR_EQUAL_THAN:
                    condition += this.buildCondition("<=", conditionVO, tableInfo);
                    break;
                case GRATER_OR_EQUAL_THAN:
                    condition += this.buildCondition(">=", conditionVO, tableInfo);
                    break;
                default:
                    condition += " 1=1 " + conditionVO.getNextOperation();
            }
        }
        condition += " 1=1 ";

        return sql + from + condition;
    }

    /**
     * 创建条件语句
     *
     * @param operation
     * @param conditionVO
     * @return
     */
    private String buildCondition(String operation, OperationConditionVO conditionVO, Map<String,String> tableInfo) {
        String condition = "";
        if (conditionVO.getThanValue() != null) {
            condition += operation + "'" + conditionVO.getThanValue() + "' " + conditionVO.getNextOperation();
        }
        if (conditionVO.getSecondTableColConfig() != null) {
            condition += operation + tableInfo.get(conditionVO.getSecondTableColConfig().getTableId()) + "." + conditionVO.getSecondTableColConfig().getColCode() + " " + conditionVO.getNextOperation();
        }

        return condition;
    }

    /**
     * 解析mongo中的数据并生成sql，保存入库
     *
     * @param templateId
     * @return
     */
    public String fetchMongoToTable(String templateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("templateId").is(templateId));
        try {
            List<Document> result = mongoTemplate.find(query, Document.class, "templateFilling");
            Map<String, List<String>> inserSqlMap = new HashMap<>();
            String masterTable = getMasterTableByTemplateId(templateId);
            Map<String, List<String>> tableColMap = getAllTableColInfo(templateId);
            initInserSql(result, masterTable, inserSqlMap, tableColMap);
            System.out.println("to save db");
            saveToDb(inserSqlMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCESS";
    }

    public Map<String, List<String>> getAllTableColInfo(String templateId) {
        Map<String, List<String>> tableColMap = new HashMap<>();
        String sql = "SELECT COLUMN_NAME ,TABLE_NAME from information_schema.COLUMNS where TABLE_NAME in(select table_name from table_config where create_from = 'system'" +
                " and form_id = '" + templateId + "')";
        List colList = createNativeQuery(sql).getResultList();
        for (int i = 0; i < colList.size(); i++) {
            Object[] innerParams = (Object[]) colList.get(i);
            String colName = innerParams[0].toString();
            String tableName = innerParams[1].toString();
            if (tableColMap.containsKey(tableName)) {
                List<String> columList = tableColMap.get(tableName);
                columList.add(colName);
            } else {
                List<String> columList = new ArrayList<>();
                columList.add(colName);
                tableColMap.put(tableName, columList);
            }
            if (!tableColMap.containsKey("data_version") && !tableName.startsWith("data_master")) {
                Integer version = getTableVersion(tableName);
                List<String> versionList = new ArrayList<>();
                versionList.add(version.toString());
                tableColMap.put("data_version", versionList);
            }
        }
        return tableColMap;
    }

    public String getMasterTableByTemplateId(String templateId) {
        String sql = "select template_name from template_master where id = '" + templateId + "'";
        List<String> colList = createNativeQuery(sql).getResultList();
        String table = "data_master";
        if (colList != null && !colList.isEmpty()) {
            table = table + "_" + PinYin2Abbreviation.cn2py(colList.get(0));
        }
        return table;
    }

    public String getInsertSqlBef(List<String> colList, String tableName) {
        StringBuffer firstSqlBuf = new StringBuffer("insert into ").append(tableName).append("(");
        if (!tableName.startsWith("dch")) {
            for (String key : colList) {
                if ("templateId".equals(key)) {
                    continue;
                }
                if ("masterId".equals(key)) {
                    firstSqlBuf.append("id,");
                } else {
                    firstSqlBuf.append(key).append(",");
                }
            }
        } else {
            for (String key : colList) {
                firstSqlBuf.append(key).append(",");
            }
        }
        String firstSqlStr = firstSqlBuf.toString();
        firstSqlStr = firstSqlStr.substring(0, firstSqlStr.length() - 1) + ") VALUES";
        return firstSqlStr;
    }

    /**
     * 将生成的sql执行保存操作
     *
     * @param inserSqlMap
     * @throws Exception
     */
    public void saveToDb(Map<String, List<String>> inserSqlMap) throws Exception {
        PreparedStatement statement = null;
        Connection connection = dataSource.getConnection();
        try {
            statement = connection.prepareStatement("");
            for (String key : inserSqlMap.keySet()) {
                List<String> insertSqlList = inserSqlMap.get(key);
                String insert_sql = insertSqlList.get(0);
                statement.addBatch(insert_sql);
                // 执行操作
                statement.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }

    public Map<String, List<String>> initInserSql(List<Document> documentList, String tableName, Map<String, List<String>> inserSqlMap, Map<String, List<String>> tableColMap) {
        String insertSqlBef = getInsertSqlBef(tableColMap.get(tableName), tableName);
        StringBuffer valueSqlBuf = new StringBuffer(insertSqlBef);
        List<String> keyList = tableColMap.get(tableName);
        Map<String, String> sqlBefMap = new HashMap<>();
        try {
            for (Document document : documentList) {
                valueSqlBuf.append("(");
                for (int i = 0; i < keyList.size(); i++) {
                    String ckey = keyList.get(i);
                    if (i != keyList.size() - 1) {
                        if ("id".equals(ckey)) {
                            valueSqlBuf.append("'").append(document.get("masterId")).append("',");
                        } else {
                            String valueStr = document.get(ckey) == null ? "" : document.get(ckey).toString();
                            valueStr = valueStr.replace("'", "");
                            if(valueStr.contains(TemplateConst.UPLOAD_FILE_KEY_WORD)){
                                valueStr = (String)(((List<Document>)document.get(ckey)).get(0).get("name"));
                            }
                            valueSqlBuf.append("'").append(valueStr).append("',");
                        }
                    } else {
                        if ("id".equals(ckey)) {
                            valueSqlBuf.append("'").append(document.get("masterId")).append("'),");
                        } else {
                            String valueStr = document.get(ckey) == null ? "" : document.get(ckey).toString();
                            valueStr = valueStr.replace("'", "");
                            if(valueStr.contains(TemplateConst.UPLOAD_FILE_KEY_WORD)){
                                valueStr = (String)(((List<Document>)document.get(ckey)).get(0).get("name"));
                            }
                            valueSqlBuf.append("'").append(valueStr).append("'),");
                        }
                    }
                }
                for (String key : document.keySet()) {
                    Object value = document.get(key);
                    if (value.getClass().isArray()) {
                        System.out.println(key);
                    } else if (value instanceof List) {
                        if (!value.toString().contains("dch")) {
                            System.out.println("value=="+value);
                        } else {
                            dealListToMap((List) value, key, "", "", document.get("masterId").toString(), inserSqlMap, tableColMap, sqlBefMap);
                        }
                    }else if (value instanceof Document) {
                        List<Document> dlist = new ArrayList<>();
                        Document doc = (Document)value;
                        String one_key = doc.keySet().iterator().next();
                        Object one_value = doc.get(one_key);
                        Document indoc = new Document();
                        if(!one_value.toString().contains("dch")){
                            doc.forEach((k,v) -> {indoc.put(k,v);});
                            dlist.add(indoc);
                            dealListToMap(dlist, key, "", "", document.get("masterId").toString(), inserSqlMap, tableColMap, sqlBefMap);
                        }
                    }
                }
            }
            String inserSqlFinal = valueSqlBuf.toString();
            inserSqlFinal = inserSqlFinal.substring(0, inserSqlFinal.length() - 1);
            if (inserSqlMap.containsKey(tableName)) {
                List<String> insertList = inserSqlMap.get(tableName);
                insertList.add(inserSqlFinal);
            } else {
                List<String> insertList = new ArrayList<>();
                insertList.add(inserSqlFinal);
                inserSqlMap.put(tableName, insertList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return inserSqlMap;
    }

    public void dealListToMap(List list, String tableName, String parentKey, String parentId, String masterId, Map<String, List<String>> inserSqlMap,
                              Map<String, List<String>> tableColMap, Map<String, String> sqlBefMap) {
        if (list != null && !list.isEmpty()) {
            Integer version = Integer.valueOf(tableColMap.get("data_version").get(0));
            StringBuffer valueSqlBuf = new StringBuffer();
            if (sqlBefMap.containsKey(tableName)) {
                valueSqlBuf = valueSqlBuf.append(sqlBefMap.get(tableName)).append(",");
            } else {
                String insertSqlBef = getInsertSqlBef(tableColMap.get(tableName), tableName);
                valueSqlBuf.append(insertSqlBef);
            }
            List<String> keyList = tableColMap.get(tableName);
            for (int i = 0; i < list.size(); i++) {
                String uuid = getUID();
                Document document = (Document) list.get(i);
                valueSqlBuf.append("(");
                for (int k = 0; k < keyList.size(); k++) {
                    String ckey = keyList.get(k);
                    if (ckey.contains("$")) {
                        ckey = ckey.replace("$", "@");
                    }
                    if (k != keyList.size() - 1) {
                        if ("id".equals(ckey)) {
                            valueSqlBuf.append("'").append(uuid).append("',");
                        } else if ("data_version".equals(ckey)) {
                            valueSqlBuf.append(version).append(",");
                        } else if ("master_id".equals(ckey)) {
                            valueSqlBuf.append("'").append(masterId).append("',");
                        } else if (parentKey.equals(ckey)) {
                            valueSqlBuf.append("'").append(parentId).append("',");
                        } else {
                            String valueStr = document.get(ckey) == null ? "" : document.get(ckey).toString();
                            if(valueStr.length()>1600){
                                valueStr = valueStr.substring(0,1600);
                            }
                            valueStr = valueStr.replace("'", "");
                            valueSqlBuf.append("'").append(valueStr).append("',");
                        }
                    } else {
                        if ("id".equals(ckey)) {
                            valueSqlBuf.append("'").append(uuid).append("'),");
                        } else if ("data_version".equals(ckey)) {
                            valueSqlBuf.append(version).append("),");
                        } else if ("master_id".equals(ckey)) {
                            valueSqlBuf.append("'").append(masterId).append("'),");
                        } else if (parentKey.equals(ckey)) {
                            valueSqlBuf.append("'").append(parentId).append("'),");
                        } else {
                            String valueStr = document.get(ckey) == null ? "" : document.get(ckey).toString();
                            valueStr = valueStr.replace("'", "");
                            valueSqlBuf.append("'").append(valueStr).append("'),");
                        }
                    }
                }
                for (String key : document.keySet()) {
                    Object value = document.get(key);
                    if (value.getClass().isArray()) {

                    } else if (value instanceof List) {
                        if (!value.toString().contains("dch")) {

                        } else {
                            //System.out.println(key);
                            String parentCol = tableName + "_id";
                            dealListToMap((List) value, key, parentCol, uuid, "", inserSqlMap, tableColMap, sqlBefMap);
                        }
                    }
                }
            }
            String valueSqlStr = valueSqlBuf.toString();
            if (valueSqlStr.contains("@")) {
                valueSqlStr = valueSqlStr.replace("@", "$");
            }
            String inserSql = valueSqlStr.substring(0, valueSqlStr.length() - 1);
            if (inserSqlMap.containsKey(tableName)) {
                List<String> insertList = inserSqlMap.get(tableName);
                insertList.remove(0);
                insertList.add(0, inserSql);
                sqlBefMap.put(tableName, inserSql);
            } else {
                List<String> insertList = new ArrayList<>();
                insertList.add(inserSql);
                inserSqlMap.put(tableName, insertList);
                sqlBefMap.put(tableName, inserSql);
            }
        }
    }

    public Integer getTableVersion(String tableName) {
        Integer version = null;
        String sql = "select max(data_version) from " + tableName + " where 1=1";
        List colList = createNativeQuery(sql).getResultList();
        if (colList != null && !colList.isEmpty()) {
            version = colList.get(0)==null?0:Integer.valueOf(colList.get(0).toString());
        }else{
            version = 0;
        }
        return version;
    }

    public String getUID() {
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        return uid;
    }

    /**
     * 获取数据并插入到表中
     *
     * @param tableId
     * @return
     */
    public TableColVO fetchTableFromMongo(String tableId) throws Exception {
        String sql = "select table_name,form_id,execute_sql from table_config where id = '" + tableId + "'";
        List resultList = createNativeQuery(sql).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            Object[] innerParams = (Object[]) resultList.get(0);
            String tableName = innerParams[0].toString();
            String execute_sql = innerParams[2].toString();
            List queryList = createNativeQuery(execute_sql).getResultList();
            Integer version = getTableVersion(tableName);
            saveResultToDb(tableName,version, execute_sql, queryList);
        }
        return getTableColVO(tableId, 20, 1);
    }

    public void saveResultToDb(String tableName,Integer dataVersion, String sql, List queryList){
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            if(queryList==null || queryList.isEmpty()){
                return;
            }
            StringBuffer inserSqlBef = new StringBuffer("insert into ").append(tableName).append("(id,data_version,");
            List<String> keyList = getTableColum(sql,tableName);
            for (String key : keyList) {
                inserSqlBef.append(key).append(",");
            }
            String insertSqlBefStr = inserSqlBef.toString();
            insertSqlBefStr = insertSqlBefStr.substring(0, insertSqlBefStr.length() - 1) + ") VALUES";
            StringBuffer valueSqlBuf = new StringBuffer(insertSqlBefStr);
            for (int i = 0; i < queryList.size(); i++) {
                String uid = getUID();
                valueSqlBuf.append("('").append(uid).append("',").append(dataVersion).append(",");
                Object[] innerParams = (Object[]) queryList.get(i);
                for (int k = 0; k < innerParams.length; k++) {
                    if (k != innerParams.length - 1) {
                        valueSqlBuf.append("'").append(innerParams[k]).append("',");
                    } else {
                        valueSqlBuf.append("'").append(innerParams[k]).append("'),");
                    }
                }
            }
            String valueSqlStr = valueSqlBuf.toString();
            valueSqlStr = valueSqlStr.substring(0, valueSqlStr.length() - 1);
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("");
            statement.addBatch(valueSqlStr);
            // 执行操作
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getTableColum(String sql,String tableName) {
        List<String> list = new ArrayList<>();
        if(!StringUtils.isEmptyParam(sql)){
            String sqlUpper = sql.toUpperCase();
            int table_index = sqlUpper.indexOf("FROM");
            int select_indxe = sqlUpper.indexOf("SELECT");
            String tableColums = sqlUpper.substring(select_indxe + "SELECT".length(), table_index);
            String[] colums = tableColums.split(",");
            for (String colum : colums) {
                int lindex = colum.lastIndexOf(".");
                if (lindex > 0) {
                    list.add(colum.substring(lindex + 1));
                } else {
                    list.add(colum);
                }
            }
        }else{
            String querySql = "SELECT COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME = '"+tableName+"'" +
                    " and COLUMN_NAME NOT IN ('id','data_version')";
            list = createNativeQuery(querySql).getResultList();
        }
        return list;
    }

    /**
     * 获取统计结果
     * @param reportQueryParam
     * @param reportData
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> getReportStatistics(ReportQueryParam reportQueryParam, ReportData reportData) throws Exception{
        List<T> reportList = new ArrayList<>();
        String tableName = reportQueryParam.getTableName();
        String x_field = reportQueryParam.getXaxis();
        String y_field = reportQueryParam.getYaxis();
        String sort = reportQueryParam.getSortType();
        String type = reportQueryParam.getType();
        String table_name = getTableNameById(tableName);
        StringBuffer sqlBuffer = new StringBuffer("SELECT ");
        try {
            //表格类型查询，查询字段表统并赋值，柱状图 也赋予字段值
            setReportFieldValue(reportQueryParam);
            //如果图像为表格类型则直接查询
            if("table".equals(reportQueryParam.getChart())){
                if(StringUtils.isEmptyParam(table_name)){
                    return new ArrayList<>();
                }
                List<FieldChange> fieldChangeList = reportQueryParam.getTableResults();
                fieldChangeList.stream().forEach(x -> sqlBuffer.append(x.getTitle()).append(","));
                String sqlStr = sqlBuffer.toString().substring(0,sqlBuffer.length()-1);
                StringBuffer dvBuffer = new StringBuffer(" FROM ").append(table_name);
                if(!table_name.startsWith("data_master")){
                    dvBuffer.append(" where data_version = (select max(data_version) from ").append(table_name).append(")");
                }
                sqlStr += dvBuffer.toString();
                int perPage = reportData ==null?20:reportData.getPerPage();
                int currentPage = reportData ==null?1:reportData.getCurrentPage();
                long limit_start = (currentPage -1) * perPage;
                long limit_end = perPage;
                sqlStr += " limit " + limit_start + ","+limit_end;
                List resultList = createNativeQuery(sqlStr).getResultList();
                return resultList;
            }
            if(StringUtils.isEmptyParam(table_name)){
                return reportList;
            }
            if("2".equals(reportQueryParam.getIfDuplicate())){//不统计的话则查询x轴y轴
                String isDouble = "2";
                if(!StringUtils.isEmptyParam(x_field) && !StringUtils.isEmptyParam(y_field)){
                    sqlBuffer.append(x_field).append(",").append(y_field);
                }else if(!StringUtils.isEmptyParam(x_field)){
                    sqlBuffer.append(x_field);
                    isDouble = "1";
                }else{
                    sqlBuffer.append(y_field);
                    isDouble = "0";
                }
                sqlBuffer.append(" from ").append(table_name);
                if(!table_name.startsWith("data_master")){
                    sqlBuffer.append(" where data_version = (select max(data_version) from ")
                            .append(table_name).append(")");
                }
//                int perPage = reportData ==null?20:reportData.getPerPage();
//                int currentPage = reportData ==null?1:reportData.getCurrentPage();
//                long limit_start = (currentPage -1) * perPage;
//                long limit_end = perPage;
//                sqlBuffer.append(" limit ").append(limit_start).append(",").append(limit_end);
                final String isDb = isDouble;
                List resultList = createNativeQuery(sqlBuffer.toString()).getResultList();
                resultList.stream().forEach(rt->{
                    Object[] innerParams = (Object[])rt;
                    if(isDb.equals("2")){
                        String xvalue = innerParams[0].toString();
                        String yvalue = innerParams[1].toString();
                        UnitFunds unitFunds = new UnitFunds();
                        unitFunds.setUnit(xvalue);
                        unitFunds.setFunds(Double.valueOf(yvalue));
                        reportList.add((T)unitFunds);
                    }else if(isDb.equals("1")){
                        String xvalue = innerParams[0].toString();
                        UnitFunds unitFunds = new UnitFunds();
                        unitFunds.setUnit(xvalue);
                        reportList.add((T)unitFunds);
                    }else if(isDb.equals("0")){
                        String yvalue = innerParams[0].toString();
                        UnitFunds unitFunds = new UnitFunds();
                        unitFunds.setFunds(Double.valueOf(yvalue));
                        reportList.add((T)unitFunds);
                    }
                });
                return reportList;
            }
            //如果所选的字段一致，则为统计计数
            if(x_field.equals(y_field) || StringUtils.isEmptyParam(y_field)){
                sqlBuffer.append(" count(*),").append(x_field).append(" FROM ").append(table_name);
                if(!table_name.startsWith("data_master")){
                    sqlBuffer.append(" WHERE data_version = (select max(data_version) from ").append(table_name).append(")");
                }
                sqlBuffer.append(" GROUP BY ").append(x_field);
                if("0".equals(sort)){//降序
                    sqlBuffer.append(" ORDER BY ").append(x_field).append(" DESC");
                }else{
                    sqlBuffer.append(" ORDER BY ").append(x_field).append(" ASC");
                }
                List resultList = createNativeQuery(sqlBuffer.toString()).getResultList();
                for(int i=0;i<resultList.size();i++) {
                    UnitFunds unitFunds = new UnitFunds();
                    Object[] innerParams = (Object[]) resultList.get(i);
                    Integer countNum = Integer.valueOf(innerParams[0].toString());
                    String fieldValue = innerParams[1].toString();
                    unitFunds.setUnit(fieldValue);
                    unitFunds.setFunds(countNum.doubleValue());
                    reportList.add((T)unitFunds);
                }
                return reportList;
            }else{
                if(!StringUtils.isEmptyParam(x_field)){
                    sqlBuffer.append(x_field).append(",");
                }else{
                    sqlBuffer.append("1").append(",");
                }
                sqlBuffer.append(y_field).append(" FROM ").append(table_name);
                if(!table_name.startsWith("data_master")){
                    sqlBuffer.append(" where data_version = (select max(data_version) from ").append(table_name).append(")");
                }
                List resultList = createNativeQuery(sqlBuffer.toString()).getResultList();
                Map<String,List<UnitFunds>> resultMap = new HashMap<>();
                for(int i=0;i<resultList.size();i++){
                    Object[] innerParams = (Object[]) resultList.get(i);
                    String xValue = innerParams[0].toString();
                    String yValue = getRealYvalue(innerParams[1].toString());
                    Double yValueNum = Double.valueOf(yValue);
                    if(yValueNum>0){
                        if(resultMap.containsKey(xValue)){
                            List<UnitFunds> unitFundsList = resultMap.get(xValue);
                            UnitFunds unitFunds = new UnitFunds();
                            unitFunds.setUnit(y_field);
                            unitFunds.setFunds(yValueNum);
                            unitFundsList.add(unitFunds);
                        }else{
                            List<UnitFunds> unitFundsList = new ArrayList<>();
                            UnitFunds unitFunds = new UnitFunds();
                            unitFunds.setUnit(y_field);
                            unitFunds.setFunds(yValueNum);
                            unitFundsList.add(unitFunds);
                            resultMap.put(xValue,unitFundsList);
                        }
                    }
                }
                for(String key:resultMap.keySet()){
                    UnitFunds unitFunds = new UnitFunds();
                    Double statValue = getStatisticsValue(resultMap.get(key),type);
                    unitFunds.setUnit(key);
                    unitFunds.setFunds(statValue);
                    reportList.add((T)unitFunds);
                }
            }
        }catch (NumberFormatException e){
            throw new Exception("所选y轴字段非数字类型，无法进行统计");
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportList;
    }

    public String getTableNameById(String tableId){
        String sql = "select table_name from table_config where id = '"+tableId+"'";
        List<String> list = createNativeQuery(sql).getResultList();
        return list.isEmpty()?"":list.get(0);
    }
    public Double getStatisticsValue(List<UnitFunds> unitFundsList,String type){
        Double result = 0D;
        if(unitFundsList!=null && !unitFundsList.isEmpty()){
            if("1".equals(type)){//求和
                return unitFundsList.stream().mapToDouble(UnitFunds::getFunds).sum();
            }else if("2".equals(type)){
                return unitFundsList.stream().mapToDouble(UnitFunds::getFunds).average().getAsDouble();
            }
        }
        return result;
    }
    public static String getRealYvalue(String value){
        String numStr = value;
        numStr = numStr.replace(" ","");
        numStr = numStr.replace(",","");
        numStr = numStr.replace("/","");
        numStr = numStr.replace("-","");
        numStr = numStr.replace("无","");
        if("".equals(numStr)){
            numStr = "0";
        }
        Pattern pattern = Pattern.compile(".*?(\\d+[.]\\d+)[.].*");
        Matcher matcher = pattern.matcher(numStr);
        if (matcher.find()){
            return matcher.group(1);
        }
        return numStr;
    }

    @Transactional
    public ReturnInfo delCustomerDefineTable(String tableId) {
        ReturnInfo returnInfo = null;
        try {
            //删除table_config中定义的表
            TableConfig tableConfig = get(TableConfig.class,tableId);
            if(tableConfig!=null){
                remove(tableConfig);
                //删除table_col_config中的表字段
                String hql = "delete from TableColConfig where tableId = '"+tableId+"'";
                excHql(hql);
                //删除创建的表
                String tableName = tableConfig.getTableName();
                String sql = "DROP TABLE if EXISTS "+ tableName + ";";
                createNativeQuery(sql).executeUpdate();
            }
            returnInfo = new ReturnInfo("true","操作成功");
        }catch (Exception e){
            e.printStackTrace();
            returnInfo = new ReturnInfo("false",e.getMessage());
            throw e;
        }
        return returnInfo;
    }

    public static void main(String args[]){
        List<MongoResultVo> list = new ArrayList<>();
        MongoResultVo mongoResultVo = new MongoResultVo();
        mongoResultVo.setName("1");
        mongoResultVo.setValue(2);
        list.add(mongoResultVo);
        MongoResultVo mongoResultVo2 = new MongoResultVo();
        mongoResultVo2.setName("1");
        mongoResultVo2.setValue(2);
        list.add(mongoResultVo2);
        System.out.println(list.stream().mapToDouble(MongoResultVo::getValue).average().getAsDouble());
        System.out.println(list.stream().mapToDouble(MongoResultVo::getValue).sum());

        List<UnitFunds> unitFundsList = new ArrayList<>();
        UnitFunds unitFunds = new UnitFunds();
        unitFunds.setUnit("1");
        unitFunds.setFunds(2.0);
        UnitFunds unitFunds1 = new UnitFunds();
        unitFunds1.setUnit("1");
        unitFunds1.setFunds(2.0);
        unitFundsList.add(unitFunds);
        if(!unitFundsList.contains(unitFunds1)){
            unitFundsList.add(unitFunds1);
        }
        System.out.println(unitFundsList.stream().mapToDouble(UnitFunds::getFunds).sum());
        ReportQueryParam reportQueryParam = new ReportQueryParam();
        List<FieldChange> fieldChangeList = new ArrayList<>();
        FieldChange fieldChange = new FieldChange();
        fieldChange.setChangeTitle("姓名");
        fieldChange.setTitle("name");
        FieldChange fieldChange1 = new FieldChange();
        fieldChange1.setChangeTitle("年龄");
        fieldChange1.setTitle("age");
        fieldChangeList.add(fieldChange);
        fieldChangeList.add(fieldChange1);
        reportQueryParam.setTableResults(fieldChangeList);
        try {
            System.out.println(JSONUtil.objectToJson(reportQueryParam));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多报表查询接口
     * @param reportParamList
     * @return
     */
    public Object getManyReportStatistics(List<ReportParam> reportParamList) {
        try {
            for(ReportParam reportParam:reportParamList){
                ReportQueryParam reportQueryParam = reportParam.getConfig();
                if(reportQueryParam==null){
                    continue;
                }
                if(StringUtils.isEmptyParam(reportQueryParam.getChart())){
                    reportQueryParam.setChart(reportParam.getChart());
                }
                ReportData reportData = reportParam.getReportData();
                if(reportData==null){
                    reportData = new ReportData();
                }
                reportData.setResult(getReportStatistics(reportQueryParam,reportData));
                if("table".equals(reportQueryParam.getChart())){
                    String table_name = getTableNameById(reportQueryParam.getTableName());
                    BigInteger totalCount = getQueryResultFromTable(table_name);
                    reportData.setTotalCount(totalCount.longValue());
                    reportData.setCurrentPage(reportData.getCurrentPage());
                    reportData.setPerPage(reportData.getPerPage());
                }
                reportParam.setReportData(reportData);
            }
//            reportParamList.stream().parallel().forEach(reportParam -> {
//                ReportQueryParam reportQueryParam = reportParam.getConfig();
//                if(reportQueryParam!=null){
//                    if(StringUtils.isEmptyParam(reportQueryParam.getChart())){
//                        reportQueryParam.setChart(reportParam.getChart());
//                    }
//                    ReportData reportData = new ReportData();
//                    reportData.setResult(getReportStatistics(reportQueryParam));
//                    reportParam.setReportData(reportData);
//                }
//            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportParamList;
    }

    /**
     * 根据前端传入参数设置字段表头中文名称
     * @param reportQueryParam
     */
    public void setReportFieldValue(ReportQueryParam reportQueryParam){
        List<String> fieldList = new ArrayList<>();
        if("table".equals(reportQueryParam.getChart())){
            reportQueryParam.getTableResults().stream().forEach(fc -> fieldList.add(fc.getTitle()));
        }else{
            if(!StringUtils.isEmptyParam(reportQueryParam.getXaxis()))
                fieldList.add(reportQueryParam.getXaxis());
            if(!StringUtils.isEmptyParam(reportQueryParam.getYaxis()))
                fieldList.add(reportQueryParam.getYaxis());
        }
        String colCodes = StringUtils.getQueryIdsString(fieldList);
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append("COL_NAME,COL_CODE FROM table_col_config WHERE TABLE_ID = '").append(reportQueryParam.getTableName()).append("' AND COL_CODE IN (")
                .append(colCodes).append(")");
        String query_sql = sb.toString();
        List list = createNativeQuery(query_sql).getResultList();
        if("table".equals(reportQueryParam.getChart())){
            List<FieldChange> fieldChangeList = reportQueryParam.getTableResults();
            Map<String,String> reMap = new HashMap<>();
            for(int i=0;i<list.size();i++) {
                Object[] innerParams = (Object[]) list.get(i);
                String colName = innerParams[0].toString();
                String colCode = innerParams[1].toString();
                reMap.put(colCode,colName);
            }
            fieldChangeList.stream().forEach(fieldChange -> {
                if(StringUtils.isEmptyParam(fieldChange.getChangeTitle())){
                    fieldChange.setChangeTitle(reMap.get(fieldChange.getTitle()));
                }
            });
            reportQueryParam.setTableResults(fieldChangeList);
        }else{
            List<FieldChange> fieldChangeList = new ArrayList<>();
            for(int i=0;i<list.size();i++) {
                Object[] innerParams = (Object[]) list.get(i);
                String colName = innerParams[0].toString();
                String colCode = innerParams[1].toString();
                FieldChange fieldChange = new FieldChange();
                fieldChange.setTitle(colCode);
                fieldChange.setChangeTitle(colName);
                fieldChangeList.add(fieldChange);
            }
            reportQueryParam.setTableResults(fieldChangeList);
        }
    }

    public <T> T getQueryResultFromTable(String tableName){
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM ").append(tableName);
        BigInteger total = (BigInteger)createNativeQuery(sb.toString()).getResultList().get(0);
        return (T)total;
    }

    /**
     * 新建，修改，删除用户自定义报表分组
     * @param reportGroup
     * @return
     */
    @Transactional
    public Response mergeCustomerReportGroup(ReportGroup reportGroup) {
        try {
            ReportGroup merge = merge(reportGroup);
            if("-1".equals(reportGroup.getStatus()) && !StringUtils.isEmptyParam(reportGroup.getParentId())){
                delCustomerDefineTable(reportGroup.getTableId());
                //删除自定义表所关联的数据分析表
                String hql = "update TemplateQueryRule set status = '-1' where content like '%"+reportGroup.getTableId()+"%'";
                excHql(hql);
            }
            return Response.status(Response.Status.OK).entity(merge).build();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取用户自定义报表分组
     * @param reportName
     * @return
     */
    public List<ReportGroupVo> getReportGroupVoList(String templateId,String reportName) {
        List<ReportGroupVo> reportGroupVos = new ArrayList<>();
        try{
            String userId = UserUtils.getCurrentUser().getId();
            String hql = " from ReportGroup as r where r.status <> '-1' and r.createBy = '" +userId+"' and r.templateId = '"+templateId+"'";
            if(!StringUtils.isEmptyParam(reportName)){
                hql += " and r.reportName like '%"+reportName+"%' or exists(select 1 from ReportGroup where parentId = r.id" +
                        " and status<>'-1') ";
            }
            hql += " order by createDate desc";
            List<ReportGroup> reportGroups = createQuery(ReportGroup.class,hql,new ArrayList<>()).getResultList();
            Map<String,List<ReportGroup>> secondGroups = new HashMap<>();
            reportGroups.stream().forEach(reportGroup -> {
                if(StringUtils.isEmptyParam(reportGroup.getParentId())){
                    ReportGroupVo reportGroupVo = new ReportGroupVo(reportGroup.getId(),reportGroup.getTableId(),reportGroup.getReportName());
                    reportGroupVos.add(reportGroupVo);
                }else{
                    if(secondGroups.containsKey(reportGroup.getParentId())){
                        List<ReportGroup> innerList = secondGroups.get(reportGroup.getParentId());
                        innerList.add(reportGroup);
                    }else{
                        List<ReportGroup> innerList = new ArrayList<>();
                        innerList.add(reportGroup);
                        secondGroups.put(reportGroup.getParentId(),innerList);
                    }
                }
            });
            reportGroupVos.stream().parallel().forEach(reportGroupVo -> {
                if(secondGroups.containsKey(reportGroupVo.getId())){
                    reportGroupVo.setReportGroupList(secondGroups.get(reportGroupVo.getId()));
                }else{
                    reportGroupVo.setReportGroupList(new ArrayList<ReportGroup>());
                }
            });
            //查询系统初始化报表
            ReportGroupVo reportGroupVo = new ReportGroupVo();
            reportGroupVo.setTableId("");
            reportGroupVo.setReportName("系统初始化报表");
            List<ReportGroup> systemReportList = new ArrayList<>();
            List<TableConfig> tableConfigs = getTableConfig(templateId,"system");
            tableConfigs.stream().forEach(tableConfig -> {
                ReportGroup sysReport = new ReportGroup();
                sysReport.setTableId(tableConfig.getId());
                sysReport.setReportName(tableConfig.getTableDesc());
                if(!StringUtils.isEmptyParam(reportName)){
                    if(tableConfig.getTableDesc().contains(reportName))
                        systemReportList.add(sysReport);
                }else{
                    systemReportList.add(sysReport);
                }
            });
            reportGroupVo.setReportGroupList(systemReportList);
            reportGroupVos.add(reportGroupVo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportGroupVos;
    }

    /**
     * 清洗表数据，版本号+1
     * @param tableUponFieldVo
     * @return
     */
    @Transactional
    public TableUponFieldVo cleanDataByTableField(TableUponFieldVo tableUponFieldVo) {
        try {
            //后续优化
            String tableName = getTableNameById(tableUponFieldVo.getTableId());
            String templateId = tableUponFieldVo.getTemplateId();
            TableUpon tableUponDb = getTableUponDb(tableName,templateId);
            if(!"-1".equals(tableUponFieldVo.getStatus())){
                Integer dataVersion = tableUponDb==null?0:tableUponDb.getDataVersion();
                List<String> columnList = getTableColum("",tableName);
                List<FieldUponValue> fieldUponValueList = tableUponFieldVo.getFieldUponValueList();
                Map<Integer,Map<String,Object>> fieldValueMapTo = new HashMap<>();
                for(FieldUponValue fieldUponValue:fieldUponValueList){
                    int index = columnList.indexOf(fieldUponValue.getField());
                    Map<String,Object> innerMap = new HashMap<>();
                    fieldUponValue.getUponValueList().stream().forEach(uponValue -> {
                        uponValue.getNormalList().stream().forEach(x ->{
                            innerMap.put(x==null?"":x.toString(),uponValue.getUponValue());
                        });
                    });
                    if(fieldValueMapTo.containsKey(index)){
                        fieldValueMapTo.get(index).putAll(innerMap);
                    }else {
                        fieldValueMapTo.put(index, innerMap);
                    }
                }
                StringBuffer sbSql = new StringBuffer("select ");
                columnList.stream().forEach(f ->  sbSql.append(f).append(","));
                String sql = sbSql.toString().substring(0,sbSql.toString().length()-1) + " from "+ tableName + " where data_version = '"+dataVersion+"'";
                List<Object> queryList = createNativeQuery(sql).getResultList();
                queryList.stream().parallel().forEach(Object ->{
                    Object[] innerParams = (Object[])Object;
                    fieldValueMapTo.forEach((k,v) -> {
                        String normalValue = innerParams[k]==null?"":innerParams[k].toString();
                        if(v.containsKey(normalValue)){
                            innerParams[k] = v.get(normalValue);
                        }
                    });
                });
                saveResultToDb(tableName,dataVersion+1, "", queryList);
                //保存 清洗记录
                TableUpon tableUpon = tableUponDb==null?(new TableUpon()):tableUponDb;
                tableUpon.setTableName(tableName);
                tableUpon.setTemplateId(templateId);
                tableUpon.setDataVersion(dataVersion+1);
                tableUpon.setStatus("1");
                TableUpon merge = merge(tableUpon);
                List<FieldUponInfo> fieldUponInfos = new ArrayList<>();
                tableUponFieldVo.getFieldUponValueList().stream().forEach(fuv ->{
                    fuv.getUponValueList().stream().parallel().forEach(uponValue -> {
                        FieldUponInfo fieldUponInfo = new FieldUponInfo();
                        fieldUponInfo.setDataVersion(dataVersion+1);
                        fieldUponInfo.setFieldEn(fuv.getField());
                        fieldUponInfo.setFieldZn(fuv.getFieldZn());
                        fieldUponInfo.setOriginalValue(uponValue.getNormalList().toString());
                        fieldUponInfo.setUponValue(uponValue.getUponValue());
                        fieldUponInfo.setRelatedTableUponId(merge.getId());
                        fieldUponInfo.setStatus("1");
                        fieldUponInfos.add(fieldUponInfo);
                    });
                });
                batchInsert(fieldUponInfos);
            }else{
                //数据回退
                if(tableUponDb!=null && tableUponDb.getDataVersion()> 0){
                    Integer dataVersion = tableUponDb.getDataVersion();
                    //删除表中的数据
                    String delTableSql = " delete from " + tableName + " where data_version = '"+dataVersion+"'";
                    createNativeQuery(delTableSql).executeUpdate();
                    //删除映射的字段信息
                    String delFieldHql = " delete from FieldUponInfo where relatedTableUponId = '"+tableUponDb.getId()+"' and dataVersion = "+ dataVersion;
                    excHql(delFieldHql);
                    //原始表判断dataVersion是否大于0大于则更新，否则删除
                    if(dataVersion>1){
                        tableUponDb.setDataVersion(dataVersion-1);
                        merge(tableUponDb);
                    }else{
                        remove(tableUponDb);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return tableUponFieldVo;
    }

    public TableUpon getTableUponDb(String tableName,String templateId){
        String hql = " from TableUpon where tableName = '" + tableName + "' and templateId = '"+templateId+"'";
        List<TableUpon> tableUponList = createQuery(TableUpon.class,hql,new ArrayList<>()).getResultList();
        return tableUponList.isEmpty()?null:tableUponList.get(0);
    }

    /**
     *根据tableId查询表所有的字段
     * @param tableId
     * @return
     */
    public List<TableColConfig> getTableColList(String tableId) {
        String hql = " from TableColConfig where tableId = '"+tableId+"'";
        List<TableColConfig> tableColConfigs = createQuery(TableColConfig.class,hql,new ArrayList<>()).getResultList();
        List<TableColConfig> tableColConfigList = new ArrayList<>();
        tableColConfigs.stream().parallel().forEach(tf ->{
            if(!"id".equals(tf.getColCode()) && !"data_version".equals(tf.getColCode())){tableColConfigList.add(tf);}
        });
        return tableColConfigList;
    }

    /**
     * 查询字段去重后的值
     * @param fieldName
     * @param tableId
     * @return
     */
    public List getFieldValueList(String fieldName, String tableId) {
        String tableName = getTableNameById(tableId);
        StringBuffer sqlBf = new StringBuffer("select distinct ").append(fieldName).append(" from ").append(tableName);
        sqlBf.append(" where data_version = ").append("(select max(data_version) from ").append(tableName).append(" )");
        List list = createNativeQuery(sqlBf.toString()).getResultList();
        if(list.contains(null)){
            list.remove(null);
            list.add("");
        }
        return list;
    }

    /**
     * 根据templateId判断用户是否填写表单数据
     * @param templateId
     * @return
     */
    public List<String> getTemplateMasterFillInfo(String templateId) {
        List<String> info = new ArrayList<>();
        String sql = "select id from template_result_master where status <>'-1' and template_id = '"+templateId+"'";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            info.add("true");
        }else{
            info.add("false");
        }
        return info;
    }

    public List<String> getTemplateResultMasterText(String templateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("templateId").is(templateId));
        try {
            List<Document> result = mongoTemplate.find(query, Document.class, "templateFilling");
            String jsonStr = JSONUtil.objectToJson(result.get(0)).toString();
            String sql = "select data_element_code,data_element_name from template_data_element where " +
                    " page_id in (select id from template_page where status<>'-1' and template_id = '"+templateId+"') order by data_element_code desc";
            List list = createNativeQuery(sql).getResultList();
            Map<String,String> codeMap = new HashMap<>();
            list.stream().forEach(cd->{
                Object[] params = (Object[])cd;
                String code = params[0].toString();
                code = code.replace("$","@");
                codeMap.put(code,params[1].toString());
            });
            for(String key:codeMap.keySet()){
                jsonStr = jsonStr.replace(key,codeMap.get(key));
            }
            System.out.println(jsonStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
