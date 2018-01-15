package com.dch.vo;

import com.dch.entity.TemplateDataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台传入的表单页面的元数据组
 */
public class ElementVO {
    private String dataElementName;
    private String dataElementCode ;
    private String dataElementType;
    private String parentDataId ;
    private List<TemplateDataValue> dataValues = new ArrayList<TemplateDataValue>();
    private List<ElementVO> children = new ArrayList<ElementVO>();
    private List<ElementVO> otherElement = new ArrayList<ElementVO>();

    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    public String getDataElementCode() {
        return dataElementCode;
    }

    public void setDataElementCode(String dataElementCode) {
        this.dataElementCode = dataElementCode;
    }

    public String getDataElementType() {
        return dataElementType;
    }

    public void setDataElementType(String dataElementType) {
        this.dataElementType = dataElementType;
    }

    public List<TemplateDataValue> getDataValues() {
        return dataValues;
    }

    public void setDataValues(List<TemplateDataValue> dataValues) {
        this.dataValues = dataValues;
    }

    public List<ElementVO> getChildren() {
        return children;
    }

    public void setChildren(List<ElementVO> children) {
        this.children = children;
    }

    public List<ElementVO> getOtherElement() {
        return otherElement;
    }

    public void setOtherElement(List<ElementVO> otherElement) {
        this.otherElement = otherElement;
    }

    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }
}
