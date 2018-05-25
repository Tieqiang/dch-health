package com.dch.service;

import com.dch.facade.TemplateCountFacade;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by sunkqa on 2018/5/21.
 */
@Controller
@Produces("application/json")
@Path("mongo/count")
public class TemplateCountService {

    @Autowired
    private TemplateCountFacade templateCountFacade;

    @Autowired
    private MongoTemplate mongoTemplate ;

    final private static String collectionName = "templateFilling";//对应表单填报集

    private final String specialChar = "@";

    private final String inputSpeChar = "$";
    /**
     *人员职称统计分析
     * @param type 0:申报时 1:目前
     * @return
     */
    @GET
    @Path("get-position-count")
    public List<PositionCountVo> getPositionCount(@QueryParam("templateId")String templateId,@QueryParam("type")String type){
        List<PositionCountVo> positionCountVos = new ArrayList<>();
        Map<String,Map<String,Integer>> map = new HashMap<String,Map<String,Integer>>();
        String groupField = "";
        String firstField = "dch_1523179199291";
        if("0".equals(type)){
            groupField = "dch_1523179199291.dch_1523179560994";
        }else{
            groupField = "dch_1523179199291.dch_1523179581724";
        }
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        try{
            //获取非兼职统计
            Criteria criteria = where("templateId").is(templateId);
            Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                    Aggregation.unwind(firstField),Aggregation.group(groupField).count().as("value"));
            AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
            GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
            List<MongoResultVo> mongoResultVos = getMongoResultVos(groupByResults,groupField);
            for(MongoResultVo mongoResultVo:mongoResultVos){
                String name = mongoResultVo.getName().trim();
                if(map.containsKey(name)){
                    Map<String,Integer> inMap = map.get(name);
                    Integer inValue = inMap.get("0");
                    inMap.put("0",inValue+mongoResultVo.getValue());
                }else{
                    Map<String,Integer> inMap = new HashMap<String,Integer>();
                    inMap.put("0",mongoResultVo.getValue());
                    map.put(name,inMap);
                }
//            PositionCountVo positionCountVo = new PositionCountVo();
//            positionCountVo.setCount(mongoResultVo.getValue());
//            positionCountVo.setIfPart("0");
//            positionCountVo.setPosition(mongoResultVo.getName());
//            positionCountVos.add(positionCountVo);
            }

            groupField = "dch_1523180339648.dch_1523180579756";
            firstField ="dch_1523180339648";
            aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                    Aggregation.unwind(firstField),Aggregation.group(groupField).count().as("value"));
            AggregationResults<BasicDBObject> groupResults1 = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
            GroupByResults<BasicDBObject> groupByResults1 = new GroupByResults(groupResults1.getMappedResults(),groupResults.getRawResults());
            List<MongoResultVo> mongoResultVos1 = getMongoResultVos(groupByResults1,groupField);
            for(MongoResultVo mongoResultVo:mongoResultVos1){
                String name = mongoResultVo.getName().trim();
                if(map.containsKey(name)){
                    Map<String,Integer> inMap = map.get(name);
                    if(inMap.containsKey("1")){
                        Integer inValue = inMap.get("1");
                        inMap.put("1",inValue+mongoResultVo.getValue());
                    }else{
                        inMap.put("1",mongoResultVo.getValue());
                    }
                }else{
                    Map<String,Integer> inMap = new HashMap<String,Integer>();
                    inMap.put("1",mongoResultVo.getValue());
                    map.put(name,inMap);
                }
            }
            for(String key:map.keySet()){
                Map<String,Integer> inMap = map.get(key);
                if(!"".equals(key)){
                    PositionCountVo positionCountVo = new PositionCountVo();
                    Integer count = inMap.get("0")==null?0:inMap.get("0");
                    Integer partCount = inMap.get("1")==null?0:inMap.get("1");
                    positionCountVo.setPosition(key);
                    positionCountVo.setCount(count);
                    positionCountVo.setPartCount(partCount);
                    positionCountVos.add(positionCountVo);
                }
//                for(String inKey:inMap.keySet()){
//                    if(!"".equals(key)){
//                        PositionCountVo positionCountVo = new PositionCountVo();
//                        positionCountVo.setCount(inMap.get(inKey));
//                        positionCountVo.setIfPart(inKey);
//                        positionCountVo.setPosition(key);
//                        positionCountVos.add(positionCountVo);
//                    }
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return positionCountVos;
    }

    /**
     *兼职人员类型统计分析
     * @param templateId 模板id
     * @return
     */
    @GET
    @Path("get-partjob-count")
    public List<MongoResultVo> getPartJobTypeCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523180339648";
        String groupField = "dch_1523180339648.dch_1523180549429";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(groupField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
       return getMongoResultVos(groupByResults,groupField);
    }

    /**
     * 获取申报时课题职称数量统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-topic-position-count")
    public List<MongoResultVo> getTopicPersonPositionCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523179199291";
        String groupField = "dch_1523179199291.dch_1523179560994";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group("dch_1523154179240",groupField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        List<MongoResultVo> mongoResultVos = getTopicPositionCount(groupByResults);
        return mongoResultVos;
    }

    /**
     * 获取课题参与单位数量统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-join-unit-count")
    public List<MongoResultVo> getJoinUnitCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523155723238";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523154179240";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(groupField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        List<MongoResultVo> mongoResultVos = getMongoResultVos(groupByResults,groupField);
        return mongoResultVos;
    }

    /**
     * 获取课题参与单位经费统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-topic-funds-count")
    public List<FundsCountVo> getTopicFundsCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523155723238";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523154179240";
        String group2Field = "dch_1523155723238.dch_1523156003036";
        String group3Field = "dch_1523155723238.dch_1523243831591";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(groupField,group2Field,group3Field).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        return getTopicFundsCount(groupByResults);
    }

    /**
     * 获取学科分布统计
     * @param templateId
     * @param type 1:一级学科,2:二级学科
     * @return
     */
    @GET
    @Path("get-course-part-count")
    public List<MongoResultVo> getCoursePartCount(@QueryParam("templateId")String templateId,@QueryParam("type")String type){
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523260226473";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(groupField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        Map<String,Integer> map = getCourseMapByType(type,groupByResults);
        for(String key:map.keySet()){
            MongoResultVo mongoResultVo = new MongoResultVo();
            mongoResultVo.setName(key);
            mongoResultVo.setValue(map.get(key));
            mongoResultVos.add(mongoResultVo);
        }
        return mongoResultVos;
    }

    /**
     * 课题类别统计分析
     * @param templateId
     * @return
     */
    @GET
    @Path("get-course-type-count")
    public List<MongoResultVo> getCourseTypeCount(@QueryParam("templateId")String templateId){
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523170063538";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(groupField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        mongoResultVos = getMongoResultVos(groupByResults,groupField);
        return mongoResultVos;
    }

    /**
     * 目前国内国外任职情况统计分析
     * @param templateId
     * @return
     */
    @GET
    @Path("get-course-person-job")
    public List<CoursePersonJob> getCoursePersonJobInfo(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523179199291";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523154179240";//课题名称
        String group2Field = "dch_1523179199291.dch_1523179379101";
        String group3Field = "dch_1523179199291.dch_1523180058523";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(groupField,group2Field,group3Field).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        List<CoursePersonJob> coursePersonJobs = getCoursePersonJobByResults(groupByResults,groupField);
        return coursePersonJobs;
    }

    /**
     * 8项目组成员及流动人员行业部委人才计划统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-team-talent-count")
    public List<TeamTalentCountVo> getTeamTalentCount(@QueryParam("templateId")String templateId){
        List<TeamTalentCountVo> teamTalentCountVos = new ArrayList<>();
        String firstField = "dch_1523181081570";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField = "dch_1523154179240";
        String group2Field = "dch_1523181081570.dch_1523181158457";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(groupField,group2Field).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        Map<String,Map<String,Integer>> talentMap = getTalentMap(groupByResults);
        for(String key:talentMap.keySet()){
            Map<String,Integer> inMap = talentMap.get(key);
            if(!inMap.isEmpty()){
                TeamTalentCountVo teamTalentCountVo = new TeamTalentCountVo();
                teamTalentCountVo.setTopic(key);
                List<MongoResultVo> mongoResultVos = new ArrayList<>();
                for(String inKey:inMap.keySet()){
                    MongoResultVo mongoResultVo = new MongoResultVo();
                    mongoResultVo.setName(inKey);
                    mongoResultVo.setValue(inMap.get(inKey));
                    mongoResultVos.add(mongoResultVo);
                }
                teamTalentCountVo.setTalentList(mongoResultVos);
                teamTalentCountVos.add(teamTalentCountVo);
            }
        }
        return teamTalentCountVos;
    }

    /**
     * 所有课题不同行业部委人才计划人数统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-talent-type-count")
    public List<MongoResultVo> getTalentTypeCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523181081570";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String group2Field = "dch_1523181081570.dch_1523181158457";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(group2Field).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        return getMongoResultVos(groupByResults,"dch_1523181158457");
    }

    /**
     * 获取人才培养情况统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-team-train-talent")
    public List<TalentNumberVo> getTeamTranTalentCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523181415097";
        String topicField = "dch_1523154179240";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String group2Field = "dch_1523181415097.dch_1523181415097@0";
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(topicField,firstField).count().as("value"),Aggregation.sort(Sort.Direction.DESC,"dch_1523181415097.dch_1523181415097@0"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        List<TalentNumberVo> teamTalentCountVos = getTeamTalentVoByResults(groupByResults);
        return getTalentNumberVoSort("1",teamTalentCountVos);
    }

    /**
     *10、所有课题研究成果关键字统计分析 & 11、所有课题项目研究成果关键词分析
     * @param templateId
     * @param type 1:研究内容关键字统计 2:研究成果关键字统计
     * @return
     */
    @GET
    @Path("get-topic-keyword-count")
    public List<TopicKeyWordVo> getTopicKeyWordCount(@QueryParam("templateId")String templateId,@QueryParam("type")String type){
        String firstField = "dch_1523235705542";
        if("2".equals(type)){
            firstField = "dch_1523235776670";
        }
        String topicField = "dch_1523154179240";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(topicField,firstField).count().as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        List<TopicKeyWordVo> topicKeyWordVos = getTopicKeyWordVosByResults(groupByResults,type,firstField);
        return topicKeyWordVos;
    }

    /**
     * 13、	所有课题出版专著数目、字数总体分析
     * @param templateId
     * @return
     */
    @GET
    @Path("get-topic-treatise-count")
    public List<TopicTreatiseVo> getTopicTreatiseCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523184137019";
        String topicField = "dch_1523154179240";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(topicField).addToSet("$dch_1523184137019.dch_1523184137019@0").as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        Map<String,Integer> treatiseMap = getTreatiseNumber(groupByResults);

        aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(topicField).sum("$dch_1523184137019.dch_1523184137019@6").as("value"));
        AggregationResults<BasicDBObject> groupResults2 = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults2 = new GroupByResults(groupResults2.getMappedResults(),groupResults2.getRawResults());
        List<TopicTreatiseVo> topicTreatiseVos = getTopicTreatiseVo(groupByResults2,treatiseMap);
        return getTopicTreatiseSort(topicTreatiseVos);
    }

    /**
     * 所有课题不同类型知识产权分析
     * @param templateId
     * @return
     */
    @GET
    @Path("get-topic-intellectual-count")
    public List<TeamTalentCountVo> getTopicIntellectualCount(@QueryParam("templateId")String templateId){
        String firstField = "dch_1523167757808";
        String topicField = "dch_1523154179240";
        String secodeField = "dch_1523244016373";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        String groupField2 = firstField+"."+secodeField;
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(topicField,groupField2).count().as("value"));
        AggregationResults<BasicDBObject> groupResults2 = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults2 = new GroupByResults(groupResults2.getMappedResults(),groupResults2.getRawResults());
        return getTopicIntellectualVos(groupByResults2,secodeField,topicField);
    }

    /**
     * 专利授权数统计
     * @param templateId
     * @return
     */
    @GET
    @Path("get-patent-authorize-count")
    public List<MongoResultVo> getPatentAuthorizeCount(@QueryParam("templateId")String templateId){
        List<MongoResultVo> mongoResultVos  = new ArrayList<>();
        String firstField = "dch_1523167757808";
        String topicField = "dch_1523154179240";
        if(StringUtils.isEmptyParam(templateId)){
            templateId = "5e5d857c628fe2940162a2e82bf000a9";
        }
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.unwind(firstField),Aggregation.group(topicField).addToSet("$dch_1523167757808.dch_1523167999517").as("value"));
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        GroupByResults<BasicDBObject> groupByResults = new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
        Map<String,Integer> treatiseMap = getTreatiseNumber(groupByResults);
        for(String key:treatiseMap.keySet()){
            Integer value = treatiseMap.get(key);
            if(value>0){
                MongoResultVo mongoResultVo = new MongoResultVo();
                mongoResultVo.setName(key);
                mongoResultVo.setValue(value);
                mongoResultVos.add(mongoResultVo);
            }
        }
        return getSortListBySortType("1",mongoResultVos);
    }

    public List<TeamTalentCountVo> getTopicIntellectualVos(GroupByResults groupByResults,String secodeField,String topicField){
        List<TeamTalentCountVo> topicIntellectualVos = new ArrayList<>();
        Map<String,Map<String,Integer>> reMap = new HashMap<>();
        Map codeMap = getMapValue(secodeField);
        if(groupByResults!=null && !groupByResults.getRawResults().isEmpty()) {
            Iterator iterator = groupByResults.iterator();
            while(iterator.hasNext()){
                Document document = (Document)iterator.next();
                String topic = document.get(topicField)+"";
                String type = document.get(secodeField)==null?"":document.get(secodeField).toString();
                type = type.trim();
                if(!"".equals(type)){
                    String typeName = codeMap.get(type)==null?"":codeMap.get(type).toString();
                    String value = document.get("value")==null?"0":document.get("value").toString();
                    if(reMap.containsKey(topic)){
                        Map<String,Integer> inMap = reMap.get(topic);
                        inMap.put(typeName,Integer.valueOf(value));
                    }else{
                        Map<String,Integer> inMap = new HashMap<>();
                        inMap.put(typeName,Integer.valueOf(value));
                        reMap.put(topic,inMap);
                    }
                }
            }
        }
        if(!reMap.isEmpty()){
            for(String key:reMap.keySet()){
                TeamTalentCountVo teamTalentCountVo = new TeamTalentCountVo();
                teamTalentCountVo.setTopic(key);
                Map<String,Integer> inMap = reMap.get(key);
                List<MongoResultVo> mongoResultVos = new ArrayList<>();
                for(String inkey:inMap.keySet()){
                    MongoResultVo mongoResultVo = new MongoResultVo();
                    mongoResultVo.setName(inkey);
                    mongoResultVo.setValue(inMap.get(inkey));
                    mongoResultVos.add(mongoResultVo);
                }
                teamTalentCountVo.setTalentList(mongoResultVos);
                topicIntellectualVos.add(teamTalentCountVo);
            }
        }
        return topicIntellectualVos;
    }

    public List<TopicTreatiseVo> getTopicTreatiseVo(GroupByResults groupByResults,Map<String,Integer> treatiseMap){
        List<TopicTreatiseVo> topicTreatiseVos = new ArrayList<>();
        if(groupByResults!=null && !groupByResults.getRawResults().isEmpty()) {
            Iterator iterator = groupByResults.iterator();
            while(iterator.hasNext()){
                Document document = (Document)iterator.next();
                String topic = document.get("_id")+"";
                String wordNum = document.get("value")==null?"":document.get("value").toString();
                wordNum = wordNum.equals("")?"0":wordNum;
                Double wordNumber = m2(Double.valueOf(wordNum));
                if(treatiseMap.get(topic)>0){
                    TopicTreatiseVo topicTreatiseVo = new TopicTreatiseVo();
                    topicTreatiseVo.setTopic(topic);
                    topicTreatiseVo.setKeywordNum(wordNumber);
                    topicTreatiseVo.setTreatiseNum(treatiseMap.get(topic));
                    topicTreatiseVos.add(topicTreatiseVo);
                }
            }
        }
        return topicTreatiseVos;
    }
    public Double m2(Double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(f));
    }
    public Map<String,Integer> getTreatiseNumber(GroupByResults groupByResults){
        Map<String,Integer> map = new HashMap<>();
        if(groupByResults!=null && !groupByResults.getRawResults().isEmpty()){
            Iterator iterator = groupByResults.iterator();
            while(iterator.hasNext()){
                Document document = (Document)iterator.next();
                String topic = document.get("_id")+"";
                List ttList = (List)document.get("value");
                if(ttList!=null && !ttList.isEmpty()){
                    if(ttList.contains("")){
                        ttList.remove("");
                    }
                    if(ttList.contains("无")){
                        ttList.remove("无");
                    }
                }
                map.put(topic,ttList.size());
            }
        }
        return map;
    }
    public List<TopicKeyWordVo> getTopicKeyWordVosByResults(GroupByResults groupByResults,String type,String firstField){
        List<TopicKeyWordVo> topicKeyWordVos = new ArrayList<>();
        Iterator iterator = groupByResults.iterator();
        String inkey = "dch_1523235705542@0";
        if("2".equals(type)){
            inkey = "dch_1523235776670@0";
        }
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            List<String> keywords = new ArrayList<>();
            List<Document> kwList = (List<Document>)document.get(firstField);
            if(kwList!=null && !kwList.isEmpty()){
                for(Document doc:kwList){
                    String keyWord = doc.get(inkey)==null?"":doc.get(inkey).toString();
                    if(!StringUtils.isEmptyParam(keyWord)){
                        keywords.add(keyWord);
                    }
                }
            }
            if(!keywords.isEmpty()){
                TopicKeyWordVo topicKeyWordVo = new TopicKeyWordVo();
                topicKeyWordVo.setTopic(topic);
                topicKeyWordVo.setKeywords(keywords);
                topicKeyWordVos.add(topicKeyWordVo);
            }
        }
        return topicKeyWordVos;
    }
    public List<TalentNumberVo> getTeamTalentVoByResults(GroupByResults groupByResults){
        List<TalentNumberVo> talentNumberVos = new ArrayList<>();
        Map<String,Map<String,Integer>> reMap = new HashMap<String,Map<String,Integer>>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            Document inDocument = (Document)document.get("dch_1523181415097");
            String masterNumStr = inDocument.get("dch_1523181415097@0")==null?"0":inDocument.get("dch_1523181415097@0").toString();
            masterNumStr = masterNumStr.equals("")?"0":masterNumStr;
            String doctorateNumStr = inDocument.get("dch_1523181415097@1")==null?"0":inDocument.get("dch_1523181415097@1").toString();
            doctorateNumStr = doctorateNumStr.equals("")?"0":doctorateNumStr;
            String abroadDocNumStr = inDocument.get("dch_1523181415097@2")==null?"0":inDocument.get("dch_1523181415097@2").toString();
            abroadDocNumStr = abroadDocNumStr.equals("")?"0":abroadDocNumStr;
            String postdoctorNumStr = inDocument.get("dch_1523181415097@3")==null?"0":inDocument.get("dch_1523181415097@3").toString();
            postdoctorNumStr = postdoctorNumStr.equals("")?"0":postdoctorNumStr;
            String internalMasterNumStr = inDocument.get("dch_1523181415097@4")==null?"0":inDocument.get("dch_1523181415097@4").toString();
            internalMasterNumStr = internalMasterNumStr.equals("")?"0":internalMasterNumStr;
            String internalDoctorateNumStr = inDocument.get("dch_1523181415097@5")==null?"0":inDocument.get("dch_1523181415097@5").toString();
            internalDoctorateNumStr = internalDoctorateNumStr.equals("")?"0":internalDoctorateNumStr;
            if(!isNotNullOrZero(masterNumStr)||!isNotNullOrZero(doctorateNumStr)||!isNotNullOrZero(abroadDocNumStr)
                    ||!isNotNullOrZero(postdoctorNumStr)||!isNotNullOrZero(internalMasterNumStr)||!isNotNullOrZero(internalDoctorateNumStr)){
                if(reMap.containsKey(topic)){
                    Map<String,Integer> inMap = reMap.get(topic);
                    inMap.put("dch_1523181415097@0",inMap.get("dch_1523181415097@0")+Integer.valueOf(masterNumStr));
                    inMap.put("dch_1523181415097@1",inMap.get("dch_1523181415097@1")+Integer.valueOf(doctorateNumStr));
                    inMap.put("dch_1523181415097@2",inMap.get("dch_1523181415097@2")+Integer.valueOf(abroadDocNumStr));
                    inMap.put("dch_1523181415097@3",inMap.get("dch_1523181415097@3")+Integer.valueOf(postdoctorNumStr));
                    inMap.put("dch_1523181415097@4",inMap.get("dch_1523181415097@4")+Integer.valueOf(internalMasterNumStr));
                    inMap.put("dch_1523181415097@5",inMap.get("dch_1523181415097@5")+Integer.valueOf(internalDoctorateNumStr));
                }else{
                    Map<String,Integer> inMap = new HashMap<>();
                    inMap.put("dch_1523181415097@0",Integer.valueOf(masterNumStr));
                    inMap.put("dch_1523181415097@1",Integer.valueOf(doctorateNumStr));
                    inMap.put("dch_1523181415097@2",Integer.valueOf(abroadDocNumStr));
                    inMap.put("dch_1523181415097@3",Integer.valueOf(postdoctorNumStr));
                    inMap.put("dch_1523181415097@4",Integer.valueOf(internalMasterNumStr));
                    inMap.put("dch_1523181415097@5",Integer.valueOf(internalDoctorateNumStr));
                    reMap.put(topic,inMap);
                }
            }
        }
        for(String key:reMap.keySet()){
            Map<String,Integer> inMap = reMap.get(key);
            TalentNumberVo talentNumberVo = new TalentNumberVo();
            talentNumberVo.setTopic(key);
            talentNumberVo.setMasterNum(inMap.get("dch_1523181415097@0"));
            talentNumberVo.setDoctorateNum(inMap.get("dch_1523181415097@1"));
            talentNumberVo.setAbroadDocNum(inMap.get("dch_1523181415097@2"));
            talentNumberVo.setPostdoctorNum(inMap.get("dch_1523181415097@3"));
            talentNumberVo.setInternalMasterNum(inMap.get("dch_1523181415097@4"));
            talentNumberVo.setInternalDoctorateNum(inMap.get("dch_1523181415097@5"));
            talentNumberVos.add(talentNumberVo);
        }
        return talentNumberVos;
    }

    public Boolean isNotNullOrZero(String input){
        if("".equals(input)||"0".equals(input)){
            return true;
        }else{
            return false;
        }
    }
    public Map<String,Map<String,Integer>> getTalentMap(GroupByResults groupByResults){
        Map<String,Map<String,Integer>> reMap = new HashMap<String,Map<String,Integer>>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            String type = document.get("dch_1523181158457")==null?"":document.get("dch_1523181158457").toString();
            String value = document.get("value")+"";
            type = type.replace(" ","");
            if("".equals(value)){
                value = "0";
            }
            if(!StringUtils.isEmptyParam(type)){
                if(reMap.containsKey(topic)){
                    Map<String,Integer> map = reMap.get(topic);
                    if(!map.containsKey(type)){
                        map.put(type,Integer.valueOf(value));
                    }
                }else{
                    Map<String,Integer> map = new HashMap<String,Integer>();
                    map.put(type,Integer.valueOf(value));
                    reMap.put(topic,map);
                }
            }
        }
        return reMap;
    }
    public List<CoursePersonJob> getCoursePersonJobByResults(GroupByResults groupByResults,String groupField){
        List<CoursePersonJob> coursePersonJobs = new ArrayList<CoursePersonJob>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()) {
            Document document = (Document) iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            String userName = document.get("dch_1523179379101")==null?"":document.get("dch_1523179379101").toString();
            List<Document> psList = (List<Document>)document.get("dch_1523180058523");
            String homeDeclarePs = psList.get(0).get("dch_1523180100600")==null?"":psList.get(0).get("dch_1523180100600").toString();
            homeDeclarePs = homeDeclarePs.equals("无")?"":homeDeclarePs;
            String abroadDeclarePs = psList.get(0).get("dch_1523180208982")==null?"":psList.get(0).get("dch_1523180208982").toString();
            abroadDeclarePs = abroadDeclarePs.equals("无")?"":abroadDeclarePs;
            String homeCurrentPs = psList.get(0).get("dch_1523180198487")==null?"":psList.get(0).get("dch_1523180198487").toString();
            homeCurrentPs = homeCurrentPs.equals("无")?"":homeCurrentPs;
            String abroadCurrentPs = psList.get(0).get("dch_1523180219654")==null?"":psList.get(0).get("dch_1523180219654").toString();
            abroadCurrentPs = abroadCurrentPs.equals("无")?"":abroadCurrentPs;
            if(!StringUtils.isEmptyParam(homeDeclarePs)||!StringUtils.isEmptyParam(abroadDeclarePs)
                    ||!StringUtils.isEmptyParam(homeCurrentPs)||!StringUtils.isEmptyParam(abroadCurrentPs)){
                CoursePersonJob coursePersonJob = new CoursePersonJob();
                coursePersonJob.setTopic(topic);
                coursePersonJob.setUserName(userName);
                coursePersonJob.setHomeDeclarePs(homeDeclarePs);
                coursePersonJob.setAbroadDeclarePs(abroadDeclarePs);
                coursePersonJob.setHomeCurrentPs(homeCurrentPs);
                coursePersonJob.setAbroadCurrentPs(abroadCurrentPs);
                coursePersonJobs.add(coursePersonJob);
            }
        }
        return coursePersonJobs;
    }
    public Map<String,Integer> getCourseMapByType(String type,GroupByResults groupByResults){
        Map<String,Integer> map = new HashMap<String,Integer>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()) {
            Document document = (Document) iterator.next();
            List valueList = (List)document.get("_id");
            String firstCs = valueList.isEmpty()?"":(valueList.get(0)==null?"":valueList.get(0).toString());
            String secondCs = valueList.isEmpty()?"":(valueList.size()<2?"":valueList.get(1).toString());
            String value = document.get("value")==null?"0":document.get("value").toString();
            if("1".equals(type)){//一级学科
                if(map.containsKey(firstCs)){
                    map.put(firstCs,map.get(firstCs)+Integer.valueOf(value));
                }else{
                    map.put(firstCs,Integer.valueOf(value));
                }
            }else{
                map.put(secondCs,Integer.valueOf(value));
            }
        }
        return map;
    }

    public List<FundsCountVo> getTopicFundsCount(GroupByResults groupByResults){
        List<FundsCountVo> fundsCountVos = new ArrayList<>();
        Map<String,Map<String,Double>> reMap = new HashMap<String,Map<String,Double>>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            String unit = document.get("dch_1523156003036")+"";
            String fundsStr = document.get("dch_1523243831591")+"";
            fundsStr = fundsStr.replace(" ","");
            if("".equals(fundsStr)){
                fundsStr = "0";
            }
            Double funds = Double.valueOf(fundsStr);
            if(reMap.containsKey(topic)){
                Map<String,Double> map = reMap.get(topic);
                if(!map.containsKey(unit)){
                    map.put(unit,funds);
                }
            }else{
                Map<String,Double> map = new HashMap<String,Double>();
                map.put(unit,funds);
                reMap.put(topic,map);
            }
        }
        for(String key:reMap.keySet()){
            FundsCountVo fundsCountVo = new FundsCountVo();
            fundsCountVo.setTopic(key);
            Map<String,Double> map = reMap.get(key);
            List<UnitFunds> unitFundsList = new ArrayList<>();
            for(String inKey:map.keySet()){
                UnitFunds unitFunds = new UnitFunds();
                unitFunds.setUnit(inKey);
                unitFunds.setFunds(map.get(inKey));
                unitFundsList.add(unitFunds);
            }
            fundsCountVo.setFundsList(unitFundsList);
            fundsCountVos.add(fundsCountVo);
        }
        return fundsCountVos;
    }

    public List<MongoResultVo> getTopicPositionCount(GroupByResults groupByResults){
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        Map<String,Map> reMap = new HashMap<String,Map>();
        Iterator iterator = groupByResults.iterator();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String topic = document.get("dch_1523154179240")+"";
            String position = document.get("dch_1523179560994")+"";
            if(reMap.containsKey(topic)){
                Map map = reMap.get(topic);
                if(!map.containsKey(position)){
                    map.put(position,"1");
                }
            }else{
                Map map = new HashMap();
                map.put(position,"1");
                reMap.put(topic,map);
            }
        }
        for(String key:reMap.keySet()){
            MongoResultVo mongoResultVo = new MongoResultVo();
            mongoResultVo.setName(key);
            mongoResultVo.setValue(reMap.get(key).size());
            mongoResultVos.add(mongoResultVo);
        }
        mongoResultVos = getSortListBySortType("1",mongoResultVos);
        return mongoResultVos;
    }

    public  List<MongoResultVo> getSortListBySortType(String sortType,List<MongoResultVo> mongoResultVos){
        if(mongoResultVos==null || mongoResultVos.isEmpty()){
            return mongoResultVos;
        }
        if("0".equals(sortType)){//升序
            Collections.sort(mongoResultVos, new Comparator<MongoResultVo>() {
                @Override
                public int compare(MongoResultVo o1, MongoResultVo o2) {
                        if(o1.getValue()>o2.getValue()){
                            return 1;
                        }else{
                            return -1;
                        }
                }
            });
        }else{//降序
            Collections.sort(mongoResultVos, new Comparator<MongoResultVo>() {
                @Override
                public int compare(MongoResultVo o1, MongoResultVo o2) {
                    if(o1.getValue()<o2.getValue()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }
        return mongoResultVos;
    }
    public List<MongoResultVo> getMongoResultVos(GroupByResults groupByResults,String groupField){
        Iterator iterator = groupByResults.iterator();
        Map codeMap = getMapValue(groupField);
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            String name = document.get(groupField)+"";
            if(document.get(groupField)==null || StringUtils.isEmptyParam(name)){
                name = document.get("_id")+"";
            }
            if(isNumeric(name)){
                name = subZeroAndDot(name);
            }
            String orignName = name;
            if(codeMap!=null && !codeMap.isEmpty()){
                String value = codeMap.get(name)==null?"":codeMap.get(name).toString();
                if(!StringUtils.isEmptyParam(value)){
                    name = value;
                }
            }
            String value = document.get("value")+"";
            if(!"null".equals(name) && !"".equals(name)){
                MongoResultVo mongoResultVo = new MongoResultVo();
                mongoResultVo.setName(name);
                mongoResultVo.setValue(Double.valueOf(value).intValue());
                mongoResultVos.add(mongoResultVo);
            }
        }
        return mongoResultVos;
    }

    public Map getMapValue(String dataElementCode){
        if(dataElementCode.contains(specialChar)){
            dataElementCode = dataElementCode.replace(specialChar,inputSpeChar);//数据库中存的还是$所以@替换成$
        }
        Map map = new HashMap();
        String sql = "select d.data_value_name,d.data_value from template_data_value d,template_data_element t " +
                "where d.data_element_id = t.id and t.data_element_code = '"+dataElementCode+"'";
        List list = templateCountFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            for(int i=0;i<list.size();i++){
                Object[] innerParams = (Object[])list.get(i);
                map.put(innerParams[1].toString(),innerParams[0]);
            }
        }
        return map;
    }

    /**
     * 判断是否为数字
     * @param input
     * @return
     */
    public boolean isNumeric(String input){
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(input);
        Boolean isMach = isNum.matches();
        if(isMach && input.length()>18){
            isMach = false;
        }
        return isMach;
    }
    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public  List<TalentNumberVo> getTalentNumberVoSort(String sortType,List<TalentNumberVo> talentNumberVos){
        if(talentNumberVos==null || talentNumberVos.isEmpty()){
            return talentNumberVos;
        }
        Collections.sort(talentNumberVos, new Comparator<TalentNumberVo>() {
            @Override
            public int compare(TalentNumberVo o2, TalentNumberVo o1) {
                if(o1.getMasterNum()>o2.getMasterNum()){
                    return 1;
                }else if(o1.getMasterNum()==o2.getMasterNum()){
                    if(o1.getDoctorateNum()>o2.getDoctorateNum()){
                        return 1;
                    }else if(o1.getDoctorateNum()==o2.getDoctorateNum()){
                        if(o1.getAbroadDocNum()>o2.getAbroadDocNum()){
                            return 1;
                        }else if(o1.getAbroadDocNum()==o2.getAbroadDocNum()){
                            if(o1.getPostdoctorNum()>o2.getPostdoctorNum()){
                                return 1;
                            }else if(o1.getPostdoctorNum()==o2.getPostdoctorNum()){
                                if(o1.getInternalMasterNum()>o2.getInternalMasterNum()){
                                    return 1;
                                }else if(o1.getInternalMasterNum()==o2.getInternalMasterNum()){
                                    return o1.getInternalDoctorateNum()>o2.getInternalDoctorateNum()?1:-1;
                                }else {
                                    return -1;
                                }
                            }else{
                                return -1;
                            }
                        }else{
                            return -1;
                        }
                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }
            }
        });
        return talentNumberVos;
    }


    public  List<TopicTreatiseVo> getTopicTreatiseSort(List<TopicTreatiseVo> topicTreatiseVos){
        if(topicTreatiseVos==null || topicTreatiseVos.isEmpty()){
            return topicTreatiseVos;
        }
        Collections.sort(topicTreatiseVos, new Comparator<TopicTreatiseVo>() {
            @Override
            public int compare(TopicTreatiseVo o1, TopicTreatiseVo o2) {
                if(o1.getTreatiseNum()<o2.getTreatiseNum()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        return topicTreatiseVos;
    }
}
