package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/5/8.
 */
public class RdfResultVo {
    private String id;
    private String name;;
    private String image;
    private String kgType;
    private String attId;
    private String attName;//关系

    private RdfChildren parent;

    private List<RdfChildren> childrens;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKgType() {
        return kgType;
    }

    public void setKgType(String kgType) {
        this.kgType = kgType;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public RdfChildren getParent() {
        return parent;
    }

    public void setParent(RdfChildren parent) {
        this.parent = parent;
    }

    public List<RdfChildren> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<RdfChildren> childrens) {
        this.childrens = childrens;
    }
}
