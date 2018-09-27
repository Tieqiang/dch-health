package com.dch.vo;

import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 创建临时表结构对象
 */
public class UserCustomTableVO implements Serializable{

    private TableConfig tableConfig ;
    private List<TableColConfig> tableColConfigs;


    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }

    public List<TableColConfig> getTableColConfigs() {
        return tableColConfigs;
    }

    public void setTableColConfigs(List<TableColConfig> tableColConfigs) {
        this.tableColConfigs = tableColConfigs;
    }
}
