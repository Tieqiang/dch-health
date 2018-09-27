package com.dch.vo;

import com.dch.entity.TableConfig;

import java.util.List;

public class CreateTableVO {

    private List<UserCustomTableVO> userCustomTableVOs;
    private List<OperationConditionVO> operationConditionVOS;
    private TableConfig tableConfig ;


    public List<UserCustomTableVO> getUserCustomTableVOs() {
        return userCustomTableVOs;
    }

    public void setUserCustomTableVOs(List<UserCustomTableVO> userCustomTableVOs) {
        this.userCustomTableVOs = userCustomTableVOs;
    }

    public List<OperationConditionVO> getOperationConditionVOS() {
        return operationConditionVOS;
    }

    public void setOperationConditionVOS(List<OperationConditionVO> operationConditionVOS) {
        this.operationConditionVOS = operationConditionVOS;
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }
}
