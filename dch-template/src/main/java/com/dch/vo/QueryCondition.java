package com.dch.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/9/10.
 */
public class QueryCondition {
    private List<ConditionParam> conditions;

    public List<ConditionParam> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionParam> conditions) {
        this.conditions = conditions;
    }

    public Map<String,Object> getParamMap(){
        Map<String,Object> map = new HashMap<>();
        if(conditions!=null && !conditions.isEmpty()){
            for(ConditionParam conditionParam:conditions){
                map.put(conditionParam.getParamName(),conditionParam.getParamValue());
            }
        }
        return map;
    }
}
