package com.dch.vo;

import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;

import java.io.Serializable;
import java.util.List;

public class TableCreateVo implements Serializable{

    private TableConfig tableConfig ;
    private List<TableColConfig> tableColConfigList ;
    private String insertSql ;

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }

    public List<TableColConfig> getTableColConfigList() {
        return tableColConfigList;
    }

    public void setTableColConfigList(List<TableColConfig> tableColConfigList) {
        this.tableColConfigList = tableColConfigList;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }
}
