package com.dch.facade;

import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;
import com.dch.entity.TemplateDataElement;
import com.dch.entity.TemplateMaster;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.TableColVO;
import com.dch.vo.TableCreateVo;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableFacade extends BaseFacade {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TemplateDataElementFacade templateDataElementFacade;

    @Autowired
    private TemplateMasterFacade templateMasterFacade;

    @Autowired
    private ComboPooledDataSource dataSource;
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
        String hql = "from TableConfig as t where t.formId='"+templateId+"'";
        List<TableConfig> configs = createQuery(TableConfig.class,hql,new ArrayList<Object>()).getResultList();
        for(TableConfig tableConfig:configs){
            String delHql = "delete TableColConfig where tableId='"+tableConfig.getId()+"'";
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
            logger.info("循环1："+i+":"+vo.getDataElementName());
            if (StringUtils.isEmptyParam(vo.getParentDataId())) {
//                firstLevelDataElement.add(vo);
                String dataElementType = vo.getDataElementType();
                if (dataElementType.contains("table")) {
                    tableCreateVos = createNextTable(vo, templateDataElements, templateId,"master");
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
        for(TableCreateVo vo :vos){
            String insertSql = vo.getInsertSql();
//            Statement statement = dataSource.getConnection().createStatement();
//            statement.execute(insertSql);
            String[] sqls = insertSql.split(";");
            for(String sql:sqls){
                logger.info("执行sql："+sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
            }


            TableConfig merge = this.merge(vo.getTableConfig());
            tableConfigs.add(merge);
            for(TableColConfig config:vo.getTableColConfigList()){
                config.setTableId(merge.getId());
                this.merge(config);
            }
        }

        return tableConfigs;
    }

    private TableCreateVo createMasterTAble(List<TemplateDataElement> firstLevelDataElement, String templateId) throws Exception {

        TemplateMaster templateMaster = templateMasterFacade.getTemplateMaster(templateId);
        String templateName = templateMaster.getTemplateName();
        String tableName = "data_master_"+ PinYin2Abbreviation.cn2py(templateName);
        List<TableColConfig> tableColConfigs = new ArrayList<>();
        TableCreateVo tableCreateVo = new TableCreateVo();
        String createSql = "drop table if EXISTS   "+tableName+";" +
                "create table "+tableName+"(" +
                " id varchar(200) NOT NULL comment '主键' ,";

        TableColConfig tableColConfig = new TableColConfig();
        tableColConfig.setColCode("id");
        tableColConfig.setColName("主键");
        tableColConfig.setColDescription(templateName + "主键");
        tableColConfig.setDataVersion(0);
        tableColConfigs.add(tableColConfig);

        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableName(tableName);
        tableConfig.setTableDesc(templateName+"数据");
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

        logger.info("创建子表："+vo);
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
        sql= sql+" data_version int DEFAULT 0  comment '版本',";
        sql = sql + "  PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '" +
                vo.getDataElementName() + "';";

        logger.info("创年SQL："+sql);
        TableCreateVo tableCreateVo = new TableCreateVo();
        tableCreateVo = new TableCreateVo();
        tableCreateVo.setInsertSql(sql);
        tableCreateVo.setTableConfig(tableConfig);
        tableCreateVo.setTableColConfigList(tableColConfigs);
        vos.add(tableCreateVo);
        return vos;
    }


    public List<TableConfig> getTableConfig(String templateId) {
        String hql = "from TableConfig as t where t.formId='"+templateId+"' order by t.tableName asc" ;
        return createQuery(TableConfig.class,hql,new ArrayList<Object>()).getResultList();
    }

    public TableColVO getTableColVO(String tableId) {
        String hql = "from TableColConfig where tableId='"+tableId+"'" ;
        List<TableColConfig> resultList = createQuery(TableColConfig.class, hql, new ArrayList<Object>()).getResultList();
        TableConfig tableConfig = this.get(TableConfig.class, tableId);
        String tableName = tableConfig.getTableName();
        String sql = "select ";
        for(TableColConfig config:resultList){
            sql+=config.getColCode()+",";
        }
        sql=sql.substring(0,sql.length()-1);
        sql+=" from "+tableConfig.getTableName();
        if(sql.contains("data_version")){
            sql+=" where data_version = (select max(data_version) from "+tableName+")";
        }
        logger.info(sql);

        List<Object[]> datas = this.createNativeQuery(sql).getResultList();
        TableColVO tableColVO = new TableColVO();
        tableColVO.setDatas(datas);
        tableColVO.setTableColConfigs(resultList);

        return tableColVO;
    }
}
