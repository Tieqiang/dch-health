package com.dch.vo;

import com.dch.entity.DataRealm;
import com.dch.entity.DataRealmValue;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 值域和值内容的显示对象 测试分支
 *
 * Created by Administrator on 2017/8/7.
 */
public class DataRealmVo {

    private DataRealm dataRealm ;

    private List<DataRealmValue> dataRealmValues = new ArrayList<>() ;

    public DataRealm getDataRealm() {
        return dataRealm;
    }

    public void setDataRealm(DataRealm dataRealm) {
        this.dataRealm = dataRealm;
    }

    public List<DataRealmValue> getDataRealmValues() {
        return dataRealmValues;
    }

    public void setDataRealmValues(List<DataRealmValue> dataRealmValues) {
        this.dataRealmValues = dataRealmValues;
    }
}
