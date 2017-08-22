package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Xu on 2017/8/1.
 */
@Entity
@Table(name = "dict_type", schema = "dch", catalog = "")
public class DictType extends BaseEntity{
    private String dictName;
    private String dictCode;
    private String inputCode;
    private String dictDesc;



    @Basic
    @Column(name = "dict_name")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Basic
    @Column(name = "dict_code")
    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    @Basic
    @Column(name = "input_code")
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Basic
    @Column(name = "dict_desc")
    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }

}
