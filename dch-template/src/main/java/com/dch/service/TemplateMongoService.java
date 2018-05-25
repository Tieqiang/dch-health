package com.dch.service;

import com.dch.entity.TemplateQueryRule;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.util.JSONUtil;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;

import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by sunkqa on 2018/3/9.
 */
@Controller
@Produces("application/json")
@Path("templateMongo")
public class TemplateMongoService {
    private final String specialChar = "@";
    private final String inputSpeChar = "$";
    final private static String collectionName = "templateFilling";//对应表单填报集
    //public static final Logger logger = LogManager.getLogger(MongoService.class);
    @Autowired
    private MongoTemplate mongoTemplate ;

    @Autowired
    private BaseFacade baseFacade;

    @GET
    @Path("get-result")
    public void mongo(){
        try {
            mongoTemplate.dropCollection(collectionName);
            String hql = "select id,template_id from template_result_master where status<>'-1' ";
            List templateIdList = baseFacade.createNativeQuery(hql).getResultList();
            for(int i=0;i<templateIdList.size();i++){
                Object[] params = (Object[])templateIdList.get(i);
                String templateId = params[1]+"";
                String masterId = params[0]+"";
                hql = " from TemplateResult where status<>'-1' and templateId = '"+templateId+"' and masterId = '"+masterId+"'";
                List<TemplateResult> templateResultList = baseFacade.createQuery(TemplateResult.class,hql,new ArrayList<Object>()).getResultList();
                if(templateResultList!=null && !templateResultList.isEmpty()){
                    StringBuffer stringBuffer = new StringBuffer("{").append("\"templateId\":").append("\"").append(templateId).append("\"")
                            .append(",").append("\"masterId\":").append("\"").append(masterId).append("\"");
                    int k=0;
                    for(TemplateResult templateResult:templateResultList){
                        String result = templateResult.getTemplateResult();
                        try {
                            if(!"null".equals(result) && !"{}".equals(result)){
                                Map map = (Map) JSONUtil.JSONToObj(result,Map.class);
                                result = getNewResult(map);//将json串中的数字字符串转化为数值
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(!"null".equals(result) && !"{}".equals(result)){
                            if(k==0 && !StringUtils.isEmptyParam(result)){
                                result = result.substring(1,result.length()-1);
                            }else if(k>0 && !StringUtils.isEmptyParam(result)){
                                result = result.substring(1,result.length()-1);
                            }
                            stringBuffer.append(",").append(result);
                            k++;
                        }
                    }
                    stringBuffer.append("}");
                    String toMongoResult = stringBuffer.toString();
                    toMongoResult = toMongoResult.replace(inputSpeChar,specialChar);
                    System.out.println(toMongoResult);
                    mongoTemplate.insert(toMongoResult,collectionName);
                }
            }
        }catch (Exception e){
            System.out.println("入Mongo库异常");
            e.printStackTrace();
        }
    }

    /**
     * 将json串中的数字字符串转化为数值为了在mongo中查询使用
     * @param map
     * @return
     * @throws Exception
     */
    public String getNewResult(Map map) throws Exception{
        String result = "";
        for(Object obj:map.keySet()){
            String value =  map.get(obj)==null?"":map.get(obj).toString();
            try {
                if(value.startsWith("[{") && value.endsWith("}]") && value.contains(inputSpeChar)){
                    List<Map> mapList = (List<Map>)map.get(obj);
                    for(Map inMap:mapList){
                        for(Object key:inMap.keySet()){
                            String inValue = inMap.get(key)==null?"":inMap.get(key).toString();
                            boolean isNum = isNumeric(inValue);
                            if(isNum){
                                inMap.put(key,new BigDecimal(inValue));
                            }
                        }
                    }
                }else if(value.startsWith("[") && value.endsWith("]") && !value.contains("{")){
                    List list = (List)map.get(obj);
                    List newList = new ArrayList();
                    if(list!=null && !list.isEmpty()){
                        for(int i=0;i<list.size();i++){
                            String inValue = list.get(i)==null?"":list.get(i).toString();
                            boolean isNum = isNumeric(inValue);
                            if(isNum){
                                newList.add(new BigDecimal(inValue));
                            }else{
                                newList.add(inValue);
                            }
                        }
                    }
                    map.put(obj,newList);
                }else if(value.startsWith("{") && value.endsWith("}") && !value.contains("[")){
                    Map inMap = (Map)map.get(obj);
                    for(Object key:inMap.keySet()){
                        String invalue = inMap.get(key)==null?"": inMap.get(key).toString();
                        boolean isNum = isNumeric(invalue);
                        if(isNum){
                            inMap.put(key,new BigDecimal(invalue));
                        }
                    }
                }else if(value.startsWith("{") && value.endsWith("}") && value.contains("[")){//Map里面 嵌套集合

                }else{
                    if(!value.startsWith("0")||"0".equals(value)){//不是以0开头的数字
                        boolean isNum = isNumeric(value);
                        if(isNum){
                            map.put(obj,new BigDecimal(value));
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONUtil.objectToJsonString(map);
    }

    @GET
    @Path("test-query")
    @Transactional
    public Response testQuery(@javax.ws.rs.QueryParam("templateId")String templateId, @javax.ws.rs.QueryParam("target")String target,
                                         @javax.ws.rs.QueryParam("targetName")String targetName) throws Exception{
        MongoQueryVo mongoQueryVo = new MongoQueryVo();
        mongoQueryVo.setTarget(target);
        mongoQueryVo.setTargetName(targetName);
        mongoQueryVo.setTemplateId(templateId);
        List<QueryParam> queryParams = new ArrayList<>();
        QueryParam queryTerm = new QueryParam();
        queryTerm.setIfDuplicate("1");
        queryTerm.setXaxis("dch_1517974037900");
        queryTerm.setYaxis("dch_1517974051072");
        queryTerm.setType("2");
        queryParams.add(queryTerm);
        //return getNewMongoResultVoByParam(queryParams);
        return null;
    }

    /**
     * mongo查询统计结果，根据传入统计规则统计数量信息
     * @param templateQueryInVoList
     * @return
     */
    @POST
    @Path("query-new-count-result")
    @Transactional
    public Response getNewMongoResultVoByParam(List<TemplateQueryInVo> templateQueryInVoList) throws Exception{
        List<TemplateQueryResultVo> templateQueryResultVos = new ArrayList<>();
        try {
            for(TemplateQueryInVo templateQueryRule:templateQueryInVoList){
                TemplateQueryResultVo templateQueryResultVo = new TemplateQueryResultVo();
                templateQueryResultVo.setTemplateId(templateQueryRule.getTemplateId());
                templateQueryResultVo.setRuleName(templateQueryRule.getRuleName());
                List<CountQueryResultVo> countQueryResultVoList = new ArrayList<>();
                String templateId = templateQueryRule.getTemplateId();
                List<QueryParam> queryParamList = getQueryParmByContent(templateQueryRule);
//                if(queryParamList==null||queryParamList.isEmpty()){
//                  continue;
//                }
                int k=0;
                for(QueryParam queryParam:queryParamList){
                    CountQueryResultVo countQueryResultVo = new CountQueryResultVo();
                    countQueryResultVo.setTitle(queryParam.getTitle());
                    countQueryResultVo.setChart(queryParam.getChart());
                    if(templateQueryRule.getContent()!=null && templateQueryRule.getContent().get(k)!=null){
                        countQueryResultVo.setConfig(JSONUtil.objectToJsonString(templateQueryRule.getContent().get(k)));
                    }
                    k++;

                    Boolean isDistinct = false;
                    GroupByResults<BasicDBObject> groupByResults = null;
                    judgeIfInputFiled(queryParam,templateId);
                    Map<String,Boolean> juMap = judgeIsNumber(queryParam.getYaxis(),templateId);
                    Boolean isLegal = juMap.get("isLegal");
                    Boolean isExist = juMap.get("isExist");
                    if(isExist && !isLegal){
                        throw new Exception("y轴传入字段无法统计，请重新选择");
                    }
                    Boolean isNumber = juMap.get("isNumber");
                    Criteria criteria = where("templateId").is(templateId);

                    Map<String,Boolean> xMap = judgeIsNumber(queryParam.getXaxis(),templateId);
                    Boolean isxLegal = xMap.get("isLegal");//判断x轴字段是否是数组或正常值
                    if(isxLegal){
                        if(queryParam.getXaxis().contains(inputSpeChar)){
                            groupByResults = getSpecialGroupByResult(isNumber,templateId,queryParam);
                        }else{
                            queryParam.setXaxis(queryParam.getXaxis().replace(inputSpeChar,specialChar));//将$替换成@符号
                            queryParam.setYaxis(queryParam.getYaxis().replace(inputSpeChar,specialChar));//将$替换成@符号
                            if(isNumber){//y轴是数字类型的字段 如果是数字类型的 根据计算类型来求和或者求平均
                                //如果x轴的值是数组则用mongo操作unwind进行分组查询
                                if("1".equals(queryParam.getType())){//求和
                                    GroupBy groupBy = GroupBy.key(queryParam.getXaxis()).initialDocument("{value:0}")
                                            .reduceFunction("function(doc,prev){prev.value+=doc."+queryParam.getYaxis()+"}");
                                    groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
                                }else if("2".equals(queryParam.getType())){//求平均
                                    GroupBy groupBy = GroupBy.key(queryParam.getXaxis()).initialDocument("{allAgeTotal:0,count:0,value:0}")
                                            .reduceFunction("function(doc,prev){prev.allAgeTotal+=doc."+queryParam.getYaxis()+";prev.count+=1;}")
                                            .finalizeFunction("function(prev){prev.value=prev.allAgeTotal/prev.count;}");
                                    groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
                                }else{
                                    GroupBy groupBy = GroupBy.key(queryParam.getXaxis()).initialDocument("{value:0}")
                                            .reduceFunction("function(doc,prev){prev.value+=1;}");
                                    groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
                                }
                            }else{
                                if(StringUtils.isEmptyParam(queryParam.getYaxis())){
                                    GroupBy groupBy = GroupBy.key(queryParam.getXaxis()).initialDocument("{value:0}")
                                            .reduceFunction("function(doc,prev){prev.value+=1;}");
                                    groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
                                }else{
                                    GroupBy groupBy = GroupBy.key(queryParam.getXaxis()).initialDocument("{value:0}")
                                            .reduceFunction("function(doc,prev){if(doc."+queryParam.getYaxis()+"){prev.value+=1;}}");
                                    groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
                                }
                            }
                        }
                        if(!"0".equals(queryParam.getIfDuplicate())){//去重
                            isDistinct = true;
                        }
                    }else{//x轴字段是数组
                        groupByResults = getXgroupByResults(isNumber,templateId,queryParam);
                        if(!"0".equals(queryParam.getIfDuplicate())){//去重
                            isDistinct = true;
                        }
                    }
                    if(groupByResults==null){
                        continue;
                    }
                    Iterator iterator = groupByResults.iterator();
                    Map codeMap = getMapValue(queryParam.getXaxis());
                    List<MongoResultVo> mongoResultVos = new ArrayList<>();
                    while(iterator.hasNext()){
                        Document document = (Document)iterator.next();
                        String name = document.get(queryParam.getXaxis())+"";
                        if(document.get(queryParam.getXaxis())==null || StringUtils.isEmptyParam(name)){
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
                        if(isDistinct){
                            if(queryParam.getXaxis().contains(inputSpeChar)||queryParam.getXaxis().contains(specialChar)){//含有特殊字符 说明查询的是集合
                                value = getSpecialDistinctValue(isxLegal,templateId,orignName,isNumber,queryParam);
                            }else {
                                value = getDistinctValue(isxLegal,templateId,orignName,isNumber,queryParam);
                            }
                        }
                        if(!"null".equals(name)){
                            MongoResultVo mongoResultVo = new MongoResultVo();
                            mongoResultVo.setName(name);
                            mongoResultVo.setValue(Double.valueOf(value).intValue());
                            mongoResultVos.add(mongoResultVo);
                        }
                    }
                    countQueryResultVo.setMongoResultVoList(getSortListBySortType(queryParam.getSortType(),mongoResultVos));
                    countQueryResultVoList.add(countQueryResultVo);
                }
                templateQueryResultVo.setCountQueryResultVoList(countQueryResultVoList);
                templateQueryResultVos.add(templateQueryResultVo);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(templateQueryResultVos).build();
    }

    public GroupByResults<BasicDBObject> getSpecialGroupByResult(Boolean isNumber,String templateId,QueryParam queryParam){
        String xField = queryParam.getXaxis();
        String firstField = queryParam.getXaxis();
        Aggregation aggregation = null;
        if(xField.contains(inputSpeChar)){
            xField = xField.replace(inputSpeChar,specialChar);
            firstField = xField.substring(0,xField.lastIndexOf(specialChar));
            xField = firstField+"."+xField;
        }
        String yFiled = queryParam.getYaxis().replace(inputSpeChar,specialChar);
        yFiled = firstField + "."+yFiled;
        Criteria criteria = where("templateId").is(templateId);
        if(isNumber){
            if("1".equals(queryParam.getType())){//求和
                aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                        Aggregation.unwind(firstField),Aggregation.group(xField).sum(yFiled).as("value"));
            }else{//求平均
                aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                        Aggregation.unwind(firstField),Aggregation.group(xField).avg(yFiled).as("value"));
            }
        }else {
            aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                    Aggregation.unwind(firstField),Aggregation.group(xField).count().as("value"));
        }
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        return new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
    }

    public GroupByResults<BasicDBObject> getXgroupByResults(Boolean isNumber,String templateId,QueryParam queryParam){
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = null;
        if(isNumber){//y轴是数字类型的
            if("1".equals(queryParam.getType())){//求和
                aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),
                        Aggregation.unwind(queryParam.getXaxis()),Aggregation.group(queryParam.getXaxis()).sum(queryParam.getYaxis()).as("value"));
            }else if("2".equals(queryParam.getType())){//求平均
                aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),
                        Aggregation.unwind(queryParam.getXaxis()),Aggregation.group(queryParam.getXaxis()).avg(queryParam.getYaxis()).as("value"));
            }
        }else{
            String xField = queryParam.getXaxis();
            String firstField = queryParam.getXaxis();
            if(xField.contains("$")){
                firstField = xField.substring(0,xField.indexOf("$"));
                xField = firstField+"."+xField;
            }
             aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                    Aggregation.unwind(firstField),Aggregation.group(xField).count().as("value"));
        }
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        return new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
    }

    public GroupByResults<BasicDBObject> getMygroupByResults(Boolean isNumber,String templateId,QueryParam queryParam){
        Criteria criteria = where("templateId").is(templateId);
        Aggregation aggregation = null;
        if(isNumber){//y轴是数字类型的
            if("1".equals(queryParam.getType())){//求和
                aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(queryParam.getXaxis()).sum(queryParam.getYaxis()).as("value"));
            }else if("2".equals(queryParam.getType())){//求平均
                aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(queryParam.getXaxis()).avg(queryParam.getYaxis()).as("value"));
            }
        }else{
            aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(queryParam.getXaxis()).count().as("value"));
        }
        AggregationResults<BasicDBObject> groupResults = mongoTemplate.aggregate(aggregation,collectionName,BasicDBObject.class);
        return new GroupByResults(groupResults.getMappedResults(),groupResults.getRawResults());
    }

    public String getSpecialDistinctValue(Boolean isxLegal,String templateId,String orignName,boolean isNumber,QueryParam queryParam) throws Exception{
        String result = "";
        try {
            String filed = queryParam.getXaxis().replace(inputSpeChar,specialChar);
            String firstField = filed.substring(0,filed.indexOf(specialChar));
            Criteria criteria = Criteria.where("templateId").is(templateId);

            String yField = queryParam.getYaxis().replace(inputSpeChar,specialChar);

            if(isNumeric(orignName)){
                if(!isHaveNumberPoint(orignName)){
                    criteria.andOperator(where(firstField+"."+filed).is(Integer.valueOf(orignName)));
                }else{
                    criteria.andOperator(where(firstField+"."+filed).is(Double.valueOf(orignName)));
                }
            }else{
                criteria.andOperator(where(firstField+"."+filed).is(orignName));
            }
            Query query = new Query(criteria);
            List<Map> basicDBObject = (List<Map>)mongoTemplate.find(query,Map.class,collectionName);
            if(isNumber){//y轴是数字 进行去重
                Set<String> set = new HashSet<String>();
                Double totalDouble = 0.0;
                if(basicDBObject!=null && !basicDBObject.isEmpty()){
                    for(Map map:basicDBObject){
                        if(map.get(firstField)!=null){
                            List<Map> listDoc = (List<Map>)map.get(firstField);
                            listDoc = getNeedList(listDoc,filed,orignName);
                            int size = listDoc.size();
                            for(int i=0;i<size;i++){
                                Map innerObj = listDoc.get(i);
                                if(StringUtils.isEmptyParam(queryParam.getYaxis())){
                                    String value = innerObj.get(filed)==null?"0":innerObj.get(filed).toString();
                                    set.add(value);
                                }else {
                                    String value = innerObj.get(yField)==null?"0":innerObj.get(yField).toString();
                                    set.add(value);
                                }
                            }
                        }
                    }
                }
                for(String svalue:set){
                    totalDouble += Double.valueOf(svalue);
                }
                if("2".equals(queryParam.getType()) && totalDouble>0){//求平均
                    totalDouble = totalDouble/set.size();
                }
                DecimalFormat df = new DecimalFormat("#.00");
                result = df.format(totalDouble);
            }else{
                Set set = new HashSet();
                if(basicDBObject!=null && !basicDBObject.isEmpty()){
                    for(Map map:basicDBObject){
                        if(map.get(firstField)!=null){
                            List<Map> listDoc = (List<Map>)map.get(firstField);
                            listDoc = getNeedList(listDoc,filed,orignName);
                            int size = listDoc.size();
                            for(int i=0;i<size;i++){
                                Map innerObj = listDoc.get(i);
                                if(StringUtils.isEmptyParam(queryParam.getYaxis())){
                                    String values = innerObj.get(filed)==null?"":innerObj.get(filed).toString();
                                    set.add(values);
                                }else {
                                    String values = innerObj.get(yField)==null?"":innerObj.get(yField).toString();
                                    set.add(values);
                                }
                            }
                        }
                    }
                }
                result = set.size()+"";
            }
        }catch (NumberFormatException e){
            String codeName = getCodeNameByCode(queryParam.getYaxis());
            if(StringUtils.isEmptyParam(codeName)){
                Map codeMap = getMapValue(queryParam.getYaxis());
                codeName = codeMap.get(queryParam.getYaxis())+"";
            }
            throw new Exception("所选y轴"+codeName+"统计字段值有非数字类型，请新填写数字类型数据，再进行统计");
        }
        return subZeroAndDot(result);
    }

    public List<Map> getNeedList(List<Map> listDoc,String xFiled,String xValue){
        List<Map> list = new ArrayList<>();
        for(Map map:listDoc){
            String value = map.get(xFiled)==null?"":map.get(xFiled).toString();
            if(value.equals(xValue)){
                list.add(map);
            }
        }
        return list;
    }
    public String getDistinctValue(Boolean isxLegal,String templateId,String orignName,boolean isNumber,QueryParam queryParam) throws Exception{
        String result = "";
        try {
            DBCollection collection = mongoTemplate.getMongoDbFactory().getLegacyDb().getCollection(collectionName);
            DBObject query = new BasicDBObject();
            query.put("templateId", templateId);
            if(isNumeric(orignName)){
                if(!isHaveNumberPoint(orignName)){
                    query.put(queryParam.getXaxis(),Integer.valueOf(orignName));
                }else{
                    query.put(queryParam.getXaxis(), Double.valueOf(orignName));
                }
            }else{
                query.put(queryParam.getXaxis(), orignName);
            }
            if(!isxLegal){
                BasicDBList values = new BasicDBList();
                values.add(query.get(queryParam.getXaxis())+"");
                query.put(queryParam.getXaxis(),new BasicDBObject("$in",values));
                //条件表达式：$ge(>)  $get(>=)  $lt(<)  $lte(<=)  $ne(<>)  $in  $nin  $all $exists $or  $nor $where $type等等
            }
            if(isNumber){//y轴是数字 进行去重
                List list = collection.distinct(queryParam.getYaxis(), query);
                Double totalDouble = 0.0;
                if(list!=null && !list.isEmpty()){
                    for(int i=0;i<list.size();i++){
                        String value =  list.get(i)==null?"0":list.get(i).toString();
                        if(StringUtils.isEmptyParam(value)){
                            value = "0";
                        }
                        totalDouble += Double.valueOf(value);
                    }
                }
                if("2".equals(queryParam.getType()) && totalDouble>0){//求平均
                    totalDouble = totalDouble/list.size();
                }
                DecimalFormat df = new DecimalFormat("#.00");
                result = df.format(totalDouble);
            }else{
                if(StringUtils.isEmptyParam(queryParam.getYaxis())){
                    int count =  collection.distinct(queryParam.getXaxis(), query).size();
                    result = count + "";
                }else{
                    int count =  collection.distinct(queryParam.getYaxis(), query).size();
                    result = count + "";
                }
            }
        }catch (NumberFormatException e){
            String codeName = getCodeNameByCode(queryParam.getYaxis());
            if(StringUtils.isEmptyParam(codeName)){
                Map codeMap = getMapValue(queryParam.getYaxis());
                codeName = codeMap.get(queryParam.getYaxis())+"";
            }
            throw new Exception("所选y轴"+codeName+"统计字段值有非数字类型，请新填写数字类型数据，再进行统计");
        }
        return subZeroAndDot(result);
    }

    public String getCodeNameByCode(String code){
        String codeName = "";
        String hql = "select dataElementName from TemplateDataElement  " +
                "where dataElementCode = '"+code+"'";
        List<String> list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty()){
            for(int i=0;i<list.size();i++){
                codeName = list.get(0);
            }
        }
        return codeName;
    }

    public void judgeIfInputFiled(QueryParam queryParam,String templateId) throws Exception{
        String hql = "select t.id from TemplateDataElement as t,TemplateDataElement as t1 where t.status<>'-1' and t1.status<>'-1' " +
                " and t.parentDataId = t1.id and t1.dataElementCode = '"+queryParam.getXaxis()+"'";
        List<String> list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty()){
            throw new Exception("X轴所选字段不是根节点字段，无法统计，请选择根节点字段");
        }else {
            hql = "select t.id from TemplateDataElement as t,TemplateDataElement as t1 where t.status<>'-1' and t1.status<>'-1' " +
                    " and t.parentDataId = t1.id and t1.dataElementCode = '"+queryParam.getYaxis()+"'";
            list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
            if(list!=null && !list.isEmpty()){
                throw new Exception("Y轴所选字段不是根节点字段，无法统计，请选择根节点字段");
            }
        }
    }
    /**
     * 判断y轴是否是统计的数字类型的，如果是的话 将其值求和或者求评价
     * @param filed
     * @param templateId
     * @return
     */
    public Map<String,Boolean> judgeIsNumber(String filed,String templateId){
        Map<String,Boolean> map = new HashMap<String,Boolean>();
        Boolean isNumber = false;
        Boolean isLegal = false;
        Boolean isExist = false;
        try {
            String qFiled = filed.replace(inputSpeChar,specialChar);
            Query query = new Query(Criteria.where("templateId").is(templateId).and(qFiled).ne("").ne(null));
            Document basicDBObject = (Document)mongoTemplate.findOne(query,Document.class,collectionName);
            if(basicDBObject!=null && basicDBObject.get(filed)!=null){//说明查询的不是集合中的字段
                if(basicDBObject!=null && basicDBObject.get(filed)!=null){
                    isExist = true;
                    isNumber = isNumeric(basicDBObject.get(filed)+"");
                    String value = basicDBObject.get(filed)+"";
                    List<Object> list2 = (List)JSONUtil.JSONToObj(value,List.class);
                    if(list2==null){
                        isLegal = true;
                    }
                }
            }else{
                String filedFisrt = "";
                String normalField = "";
                if(filed!=null && filed.contains(inputSpeChar)){
                    normalField = filed.replace(inputSpeChar,specialChar);
                    filedFisrt = filed.substring(0,filed.indexOf(inputSpeChar));
                    filed = filedFisrt+"." + normalField;
                }
                isNumber = true;
                Query query2 = new Query(Criteria.where("templateId").is(templateId).and(filed).ne("").ne(null));
                basicDBObject = (Document)mongoTemplate.findOne(query2,Document.class,collectionName);
                if(basicDBObject!=null && basicDBObject.get(filedFisrt)!=null){
                    List<Map> listDoc = (List<Map>)basicDBObject.get(filedFisrt);
                    int size = listDoc.size();
                    for(int i=0;i<size;i++){
                        Document innerObj = (Document)listDoc.get(i);
                        String values = innerObj.get(normalField)+"";
                        isExist = true;
                        if(!isNumeric(values)){
                            isNumber = false;
                        }
                        List<Object> list2 = (List)JSONUtil.JSONToObj(values,List.class);
                        if(list2==null){
                            isLegal = true;
                        }
                    }
                }
            }
        }catch (Exception e){
            isLegal = true;
        }
        map.put("isNumber",isNumber);
        map.put("isLegal",isLegal);
        map.put("isExist",isExist);
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

    public static boolean isHaveNumberPoint(String input){
        if(input==null || "".equals(input)){
            return false;
        }
        return input.contains(".");
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

    public Map getMapValue(String dataElementCode){
        if(dataElementCode.contains(specialChar)){
            dataElementCode = dataElementCode.replace(specialChar,inputSpeChar);//数据库中存的还是$所以@替换成$
        }
        Map map = new HashMap();
        String sql = "select d.data_value_name,d.data_value from template_data_value d,template_data_element t " +
                "where d.data_element_id = t.id and t.data_element_code = '"+dataElementCode+"'";
        List list = baseFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            for(int i=0;i<list.size();i++){
                Object[] innerParams = (Object[])list.get(i);
                map.put(innerParams[1].toString(),innerParams[0]);
            }
        }
        return map;
    }

    public List<String> getValueListByNameAndCode(String dataElementCode,String name){
        List<String> returnList = new ArrayList<>();
        String sql = "select d.data_value_name,d.data_value from template_data_value d,template_data_element t " +
                "where d.data_element_id = t.id and t.data_element_code = '"+dataElementCode+"' and d.data_value_name like '%"+name+"%'";
        List list = baseFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            for(int i=0;i<list.size();i++){
                Object[] innerParams = (Object[])list.get(i);
                returnList.add(innerParams[1].toString());
            }
        }
        return returnList;
    }

    public  List<QueryParam> getQueryParmByContent(TemplateQueryInVo templateQueryInVo){
        List<QueryParam> queryParamList = new ArrayList<>();
        List<Map> mapList = templateQueryInVo.getContent();
        if(mapList!=null && !mapList.isEmpty()){
            for(int i=0;i<mapList.size();i++){
                Map map = mapList.get(i);
                QueryParam queryParam = new QueryParam();
                queryParam.setTitle(map.get("title")==null?"":map.get("title").toString());
                queryParam.setType(map.get("type")==null?"":map.get("type").toString());
                queryParam.setXaxis(map.get("xaxis")==null?"":map.get("xaxis").toString());
                queryParam.setYaxis(map.get("yaxis")==null?"":map.get("yaxis").toString());
                queryParam.setIfDuplicate(map.get("ifDuplicate")==null?"":map.get("ifDuplicate").toString());
                queryParam.setSortType(map.get("sortType")==null?"":map.get("sortType").toString());
                queryParam.setChart(map.get("chart")==null?"":map.get("chart").toString());
//                queryParam.setXaxis(queryParam.getXaxis().replace(inputSpeChar,specialChar));
//                queryParam.setYaxis(queryParam.getYaxis().replace(inputSpeChar,specialChar));
                queryParamList.add(queryParam);
            }
        }
        return queryParamList;
    }

    public  List<MongoResultVo> getSortListBySortType(String sortType,List<MongoResultVo> mongoResultVos){
        if(mongoResultVos==null || mongoResultVos.isEmpty()){
            return mongoResultVos;
        }
        if("0".equals(sortType)){//升序
            Collections.sort(mongoResultVos, new Comparator<MongoResultVo>() {
                @Override
                public int compare(MongoResultVo o1, MongoResultVo o2) {
                    if(isNumeric(o1.getName()) && isNumeric(o2.getName())){
                        Double d1 = Double.valueOf(o1.getName());
                        Double d2 = Double.valueOf(o2.getName());
                        if(d1>d2){
                            return 1;
                        }else{
                            return -1;
                        }
                    }else{
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            });
        }else{//降序
            Collections.sort(mongoResultVos, new Comparator<MongoResultVo>() {
                @Override
                public int compare(MongoResultVo o1, MongoResultVo o2) {
                    if(isNumeric(o1.getName()) && isNumeric(o2.getName())){
                        Double d1 = Double.valueOf(o1.getName());
                        Double d2 = Double.valueOf(o2.getName());
                        if(d1>d2){
                            return -1;
                        }else{
                            return 1;
                        }
                    }else{
                        return o2.getName().compareTo(o1.getName());
                    }
                }
            });
        }
        return mongoResultVos;
    }

    public static void main(String args[]) throws Exception{
        Map map = new HashMap();
        map.put("age","1");
        map.put("name","sun");
        Map map1 = new HashMap();
        map1.put("age","1");
        map1.put("name","sun");
        List<Map> list = new ArrayList<>();
        list.add(map);
        list.add(map1);
        Map mar = new HashMap();
        mar.put("tem","1");
        mar.put("ruleName","1");
        mar.put("content",list);
        System.out.println(JSONUtil.objectToJsonString(mar));
        String x = "1";
        List<Object> list2 = (List)JSONUtil.JSONToObj(x,List.class);
        System.out.println(list2.size());
//        String content = "[{\"title\":\"12\",\"xaxis\":\"1\",\"yaxis\":\"2\",\"type\":\"3\",\"ifDuplicate\":\"1\",\"sortType\":\"2\",\"chart\":\"饼状图\"}]";
//        getQueryParmByContent(content);
//        List<MongoResultVo> mongoResultVos = new ArrayList<>();
//        MongoResultVo mongoResultVo = new MongoResultVo();
//        mongoResultVo.setName("19");
//        mongoResultVo.setValue(1);
//        mongoResultVos.add(mongoResultVo);
//        MongoResultVo mongoResultVo1 = new MongoResultVo();
//        mongoResultVo1.setName("12");
//        mongoResultVo1.setValue(1);
//        mongoResultVos.add(mongoResultVo1);
//        MongoResultVo mongoResultVo2 = new MongoResultVo();
//        mongoResultVo2.setName("30");
//        mongoResultVo2.setValue(2);
//        mongoResultVos.add(mongoResultVo2);
//        mongoResultVos = getSortListBySortType("0",mongoResultVos);
//        for(int i=0;i<mongoResultVos.size();i++){
//            MongoResultVo mr = mongoResultVos.get(i);
//            System.out.println(mr.getName());
//        }
    }
}
