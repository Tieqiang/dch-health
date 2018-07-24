package com.dch.vo;

import java.util.List;

/**
 * Created by sunkqa on 2018/5/10.
 */
public class RdfDataVo {
    private String code;
    private List<RdfEntity> rdfEntityList;
    private List<RdfRelation> rdfRelationList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<RdfEntity> getRdfEntityList() {
        return rdfEntityList;
    }

    public void setRdfEntityList(List<RdfEntity> rdfEntityList) {
        this.rdfEntityList = rdfEntityList;
    }

    public List<RdfRelation> getRdfRelationList() {
        return rdfRelationList;
    }

    public void setRdfRelationList(List<RdfRelation> rdfRelationList) {
        this.rdfRelationList = rdfRelationList;
    }
}
