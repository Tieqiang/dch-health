package com.dch.facade;

import com.dch.facade.common.BaseFacade;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

/**
 * Created by sunkqa on 2018/5/21.
 */
@Component
public class TemplateCountFacade extends BaseFacade {

    public GroupByResults<BasicDBObject> getGroupByResults(MongoTemplate mongoTemplate, Criteria criteria, String collectionName, String groupField) {
        GroupBy groupBy = GroupBy.key(groupField).initialDocument("{value:0}")
                .reduceFunction("function(doc,prev){prev.value+=1;}");
        return mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
    }
}
