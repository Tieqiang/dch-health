package com.dch.vo;

import com.dch.entity.TableColConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 获取待分析数据以及表结构数据
 */
public class TableColVO implements Serializable {

    private List<TableColConfig> tableColConfigs ;
    private List<Object[]> datas ;
    private long totalNum;

    public List<TableColConfig> getTableColConfigs() {
        return tableColConfigs;
    }

    public void setTableColConfigs(List<TableColConfig> tableColConfigs) {
        this.tableColConfigs = tableColConfigs;
    }

    public List<Object[]> getDatas() {
        return datas;
    }

    public void setDatas(List<Object[]> datas) {
        this.datas = datas;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }
}
