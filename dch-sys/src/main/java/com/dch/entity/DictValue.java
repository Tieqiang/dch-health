package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Xu on 2017/8/1.
 */
@Entity
@Table(name = "dict_value", schema = "dch", catalog = "")
public class DictValue  extends BaseEntity{
    private String dictTypeId;
    private String keyName;
    private String keyValue;
    private String inputCode;



    @Basic
    @Column(name = "dict_type_id")
    public String getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(String dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    @Basic
    @Column(name = "key_name")
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Basic
    @Column(name = "key_value")
    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Basic
    @Column(name = "input_code")
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

}
