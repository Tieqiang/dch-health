package com.dch.service;

import com.dch.entity.TemplateQueryRule;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.util.JSONUtil;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
    final private static String collectionName = "templateFilling";//对应表单填报集
    //public static final Logger logger = LogManager.getLogger(MongoService.class);
    @Autowired
    private MongoTemplate mongoTemplate ;

    @Autowired
    private BaseFacade baseFacade;

    @GET
    @Path("get-result")
    public void mongo(){
        mongoTemplate.dropCollection(collectionName);
        String hql = "select id,template_id from template_result_master where status<>'-1'";
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
                        if(!"null".equals(result)){
                            Map map = (Map) JSONUtil.JSONToObj(result,Map.class);
                            result = getNewResult(map);//将json串中的数字字符串转化为数值
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(!"null".equals(result)){
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
                System.out.println(toMongoResult);
                mongoTemplate.insert(toMongoResult,collectionName);
            }
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
            if(!value.startsWith("0")||"0".equals(value)){//不是以0开头的数字
                boolean isNum = isNumeric(value);
                if(isNum){
                    map.put(obj,new BigDecimal(value));
                }
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
                if(queryParamList==null||queryParamList.isEmpty()){
                  continue;
                }
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
                            value = getDistinctValue(isxLegal,templateId,orignName,isNumber,queryParam);
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
             aggregation  = Aggregation.newAggregation(Aggregation.match(criteria),
                    Aggregation.unwind(queryParam.getXaxis()),Aggregation.group(queryParam.getXaxis()).count().as("value"));
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

    public String getDistinctValue(Boolean isxLegal,String templateId,String orignName,boolean isNumber,QueryParam queryParam){
        String result = "";
        DBCollection collection = mongoTemplate.getMongoDbFactory().getLegacyDb().getCollection(collectionName);
        DBObject query = new BasicDBObject();
        query.put("templateId", templateId);
        if(isNumeric(orignName)){
            if(!isHaveNumberPoint(orignName)){
                query.put(queryParam.getXaxis(), Integer.valueOf(orignName));
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
                  String value =  list.get(i)+"";
                    totalDouble += Double.valueOf(value);
                }
            }
            if("2".equals(queryParam.getType()) && totalDouble>0){//求平均
                totalDouble = totalDouble/list.size();
            }
            DecimalFormat df = new DecimalFormat("#.00");
            result = df.format(totalDouble);
        }else{
            int count =  collection.distinct(queryParam.getYaxis(), query).size();
            result = count + "";
        }
        return subZeroAndDot(result);
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
            Query query = new Query(Criteria.where("templateId").is(templateId).and(filed).ne("").ne(null));
            Document basicDBObject = (Document)mongoTemplate.findOne(query,Document.class,collectionName);
            if(basicDBObject!=null && basicDBObject.get(filed)!=null){
                isExist = true;
                isNumber = isNumeric(basicDBObject.get(filed)+"");
                String value = basicDBObject.get(filed)+"";
                List<Object> list2 = (List)JSONUtil.JSONToObj(value,List.class);
                if(list2==null){
                    isLegal = true;
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
        return isNum.matches();
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
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }else{//降序
            Collections.sort(mongoResultVos, new Comparator<MongoResultVo>() {
                @Override
                public int compare(MongoResultVo o1, MongoResultVo o2) {
                    return o2.getName().compareTo(o1.getName());
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
