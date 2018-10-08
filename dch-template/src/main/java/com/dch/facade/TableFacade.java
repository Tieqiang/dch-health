package com.dch.facade;

import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;
import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateMaster;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        List<TemplateDataElement> templateDataElements = templateDataElementFacade.getTemplateDataElements(templateId);

        //删除之前的表
        String hql = "from TableConfig as t where t.formId='" + templateId + "'";
        List<TableConfig> configs = createQuery(TableConfig.class, hql, new ArrayList<Object>()).getResultList();
        for (TableConfig tableConfig : configs) {
            String delHql = "delete TableColConfig where tableId='" + tableConfig.getId() + "'";
            excHql(delHql);
            remove(tableConfig);
        }

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

        List<TableConfig> tableConfigs = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        for (TableCreateVo vo : vos) {
            String insertSql = vo.getInsertSql();
//            Statement statement = dataSource.getConnection().createStatement();
//            statement.execute(insertSql);
            String[] sqls = insertSql.split(";");
            for (String sql : sqls) {
                logger.info("执行sql：" + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
            }


            TableConfig merge = this.merge(vo.getTableConfig());
            tableConfigs.add(merge);
            for (TableColConfig config : vo.getTableColConfigList()) {
                config.setTableId(merge.getId());
                this.merge(config);
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
                    sql += "" + elementVo.getDataElementCode() + " varchar(1600) comment '" + elementVo.getDataElementName() + "',";
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


    public List<TableConfig> getTableConfig(String templateId) {
        String hql = "from TableConfig as t where t.formId='" + templateId + "' order by t.tableName asc";
        return createQuery(TableConfig.class, hql, new ArrayList<Object>()).getResultList();
    }

    public TableColVO getTableColVO(String tableId) {
        String hql = "from TableColConfig where tableId='" + tableId + "'";
        List<TableColConfig> resultList = createQuery(TableColConfig.class, hql, new ArrayList<Object>()).getResultList();
        TableConfig tableConfig = this.get(TableConfig.class, tableId);
        String tableName = tableConfig.getTableName();
        String sql = "select ";
        for (TableColConfig config : resultList) {
            sql += config.getColCode() + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " from " + tableConfig.getTableName();
        if (sql.contains("data_version")) {//sql.contains("data_version")
            sql += " where data_version = (select max(data_version) from " + tableName + ")";
        }
        logger.info(sql);

        List<Object[]> datas = this.createNativeQuery(sql).getResultList();
        TableColVO tableColVO = new TableColVO();
        tableColVO.setDatas(datas);
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
        String executeSQL = createExecuteSQL(createTableVO.getUserCustomTableVOs(), createTableVO.getOperationConditionVOS());
        List<TableColConfig> tableColConfigs = createTAbleColConfigs(createTableVO.getUserCustomTableVOs(), createTableVO.getOperationConditionVOS());
        String tableName = PinYin2Abbreviation.cn2py("user_custom_" + tableConfig.getTableName());
        String createTableSql = createCreateCustomTableSQL(tableColConfigs, tableName);
        Connection connection = this.dataSource.getConnection();

        String[] split = createTableSql.split(";");
        for (String sql : split) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            logger.info("开始执行SQL：" + sql);
            preparedStatement.execute();
        }

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
        return tableConfig;
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
        colConfig.setColName("版本号");
        colConfig.setColDescription("版本号");
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
        for (UserCustomTableVO vo : userCustomTableVOs) {
            List<TableColConfig> tableColConfigs = vo.getTableColConfigs();
            TableConfig tableConfig = vo.getTableConfig();
            from += tableConfig.getTableName() + ",";
            for (TableColConfig colConfig : tableColConfigs) {
                sql += " " + tableConfig.getTableName() + "." + colConfig.getColCode() + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        from = from.substring(0, from.length() - 1);

        for (OperationConditionVO conditionVO : operationConditionVOS) {
            OperationEnum operationEnum = conditionVO.getOperationEnum();
            TableColConfig firstTableColConfig = conditionVO.getFirstTableColConfig();
            condition += " " + firstTableColConfig.getColCode();
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
                    condition += this.buildCondition("=", conditionVO);
                    break;
                case GRATER_THAN:
                    condition += this.buildCondition(">", conditionVO);
                    break;
                case LESS_THAN:
                    condition += this.buildCondition("<", conditionVO);
                    break;
                case LESS_OR_EQUAL_THAN:
                    condition += this.buildCondition("<=", conditionVO);
                    break;
                case GRATER_OR_EQUAL_THAN:
                    condition += this.buildCondition(">=", conditionVO);
                    break;
                default:
                    condition += " 1=1 " + conditionVO.getNextOperation();
            }
            condition += " 1=1 ";
        }

        return sql + from + condition;
    }

    /**
     * 创建条件语句
     *
     * @param operation
     * @param conditionVO
     * @return
     */
    private String buildCondition(String operation, OperationConditionVO conditionVO) {
        String condition = "";
        if (conditionVO.getThanValue() != null) {
            condition += operation + "'" + conditionVO.getThanValue() + "' " + conditionVO.getNextOperation();
        }
        if (conditionVO.getSecondTableColConfig() != null) {
            condition += operation + conditionVO.getSecondTableColConfig().getColCode() + " " + conditionVO.getNextOperation();
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
        try {
            statement = dataSource.getConnection().prepareStatement("");
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
        }
    }

    public Map<String, List<String>> initInserSql(List<Document> documentList, String tableName, Map<String, List<String>> inserSqlMap, Map<String, List<String>> tableColMap) {
        String insertSqlBef = getInsertSqlBef(tableColMap.get(tableName), tableName);
        StringBuffer valueSqlBuf = new StringBuffer(insertSqlBef);
        List<String> keyList = tableColMap.get(tableName);
        Map<String, String> sqlBefMap = new HashMap<>();
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
                        valueSqlBuf.append("'").append(valueStr).append("',");
                    }
                } else {
                    if ("id".equals(ckey)) {
                        valueSqlBuf.append("'").append(document.get("masterId")).append("'),");
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
                    //System.out.println(key);
                } else if (value instanceof List) {
                    if (!value.toString().contains("dch")) {
                    } else {
                        //System.out.println(key);
                        dealListToMap((List) value, key, "", "", document.get("masterId").toString(), inserSqlMap, tableColMap, sqlBefMap);
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
        String sql = "select max(data_version) as version from " + tableName + " where 1=1";
        List<Integer> colList = createNativeQuery(sql).getResultList();
        if (colList != null && !colList.isEmpty()) {
            version = colList.get(0);
        }
        return version == null ? 0 : (version + 1);
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
            saveResultToDb(tableName, execute_sql, queryList);
        }
        return getTableColVO(tableId);
    }

    public void saveResultToDb(String tableName, String sql, List queryList) throws Exception {
        PreparedStatement statement = null;
        try {
            StringBuffer inserSqlBef = new StringBuffer("insert into ").append(tableName).append("(id,data_version,");
            List<String> keyList = getTableColum(sql);
            for (String key : keyList) {
                inserSqlBef.append(key).append(",");
            }
            String insertSqlBefStr = inserSqlBef.toString();
            insertSqlBefStr = insertSqlBefStr.substring(0, insertSqlBefStr.length() - 1) + ") VALUES";
            StringBuffer valueSqlBuf = new StringBuffer(insertSqlBefStr);
            Integer version = getTableVersion(tableName);
            for (int i = 0; i < queryList.size(); i++) {
                String uid = getUID();
                valueSqlBuf.append("('").append(uid).append("',").append(version).append(",");
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
            statement = dataSource.getConnection().prepareStatement("");
            statement.addBatch(valueSqlStr);
            // 执行操作
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public List<String> getTableColum(String sql) {
        List<String> list = new ArrayList<>();
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
        return list;
    }

    public List<UnitFunds> getReportStatistics(ReportQueryParam reportQueryParam) throws Exception{
        List<UnitFunds> reportList = new ArrayList<>();
        String tableName = reportQueryParam.getTableName();
        String x_field = reportQueryParam.getXaxis();
        String y_field = reportQueryParam.getYaxis();
        String sort = reportQueryParam.getSortType();
        String type = reportQueryParam.getType();
        String table_name = getTableNameById(tableName);
        StringBuffer sqlBuffer = new StringBuffer("SELECT ");
        try {
            if(x_field.equals(y_field)){//如果所选的字段一致，则为统计计数
                sqlBuffer.append(" count(*),").append(x_field).append(" FROM ").append(table_name)
                        .append(" GROUP BY ").append(x_field);
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
                    reportList.add(unitFunds);
                }
                return reportList;
            }else{
                sqlBuffer.append(x_field).append(",").append(y_field).append(" FROM ").append(table_name);
                List resultList = createNativeQuery(sqlBuffer.toString()).getResultList();
                Map<String,List<UnitFunds>> resultMap = new HashMap<>();
                for(int i=0;i<resultList.size();i++){
                    Object[] innerParams = (Object[]) resultList.get(i);
                    String xValue = innerParams[0].toString();
                    System.out.println(innerParams[1]);
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
                    reportList.add(unitFunds);
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
        return list.get(0);
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
    }
}
