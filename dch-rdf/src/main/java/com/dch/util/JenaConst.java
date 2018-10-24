package com.dch.util;

import java.io.Serializable;

/**
 * Created by sunkqa on 2018/5/9.
 */
public class JenaConst implements Serializable{

    public static final relationType RELATION_TYPE = new relationType();

    public static final dbType DB_TYPE = new dbType();

    public static class relationType extends GenericEnum{
        public static final String SUBCLASS = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
        public static final String PRODUCEBY = "http://www.imicams.ac.cn/administrator/ontologies/2018/4/medical-ontologies#produceBy";
        public static final String PRODUCE = "http://www.imicams.ac.cn/administrator/ontologies/2018/4/medical-ontologies#produce";

        private  relationType() {
            super.putEnum(SUBCLASS, "包含");
            super.putEnum(PRODUCE, "生产");
            super.putEnum(PRODUCEBY, "被生产");
        }
    }

    public static class dbType extends GenericEnum{
        public static final String DRUG_DB = "1";
        public static final String CHINA_HEALTH = "2";

        private  dbType() {
            super.putEnum(DRUG_DB, "drugdb");
            super.putEnum(CHINA_HEALTH, "chinaHealth");
        }
    }
}
