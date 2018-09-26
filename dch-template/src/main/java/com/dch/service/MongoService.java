package com.dch.service;

import com.dch.entity.TemplateQueryRule;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.util.JSONUtil;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.*;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@Controller
@Produces("application/json")
@Path("mongo")
public class MongoService {
   final private static String collectionName = "templateResult";
    //public static final Logger logger = LogManager.getLogger(MongoService.class);

    @Autowired
    private MongoTemplate mongoTemplate ;

    @Autowired
    private BaseFacade baseFacade;

    @GET
    @Path("get-result")
    public void mongo(){
        mongoTemplate.dropCollection("templateResult");
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
                            Map map = (Map)JSONUtil.JSONToObj(result,Map.class);
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
    @Path("get-all")
    public List<Person> getAll(){
        List<Person> userList1 = new ArrayList<>();
        // 查询主要用到Query和Criteria两个对象
        //Query query = new Query();
        //Criteria criteria = where("age").gt(22);    // 大于
        // criteria.and("name").is("cuichongfei");等于
        // List<String> interests = new ArrayList<String>();
        // interests.add("study");
        // interests.add("linux");
        // criteria.and("interest").in(interests); in查询
        // criteria.and("home.address").is("henan"); 内嵌文档查询
        // criteria.and("").exists(false); 列存在
        // criteria.and("").lte(); 小于等于
        // criteria.and("").regex(""); 正则表达式
        // criteria.and("").ne(""); 不等于
        // 多条件查询
        // criteria.orOperator(Criteria.where("key1").is("0"),Criteria.where("key1").is(null));
        //query.addCriteria(criteria);
        //List<Person> userList1 = mongoTemplate.find(query, Person.class);
        return userList1;
    }

    /**
     * 查询统计的条件信息
     * @return
     */
    @GET
    @Path("get-template-query-rules")
    public List<TemplateQueryRule> getTemplateQueryRules(@QueryParam("templateId")String templateId,@QueryParam("ruleName")String ruleName,
                                                          @QueryParam("parentId")String parentId){
        String userId = UserUtils.getCurrentUser().getId();
        String hql = "from TemplateQueryRule where status<>'-1' and templateId = '"+templateId+"' and createBy = '"+userId+"'";
        if(!StringUtils.isEmptyParam(ruleName)){
            hql += " and ruleName = '"+ruleName+"'";
        }
        if(!StringUtils.isEmptyParam(parentId)){
            hql += " and parentId = '"+parentId+"'";
        }else {
            hql += " and parentId is null";
        }
        List<TemplateQueryRule> templateQueryRuleList = baseFacade.createQuery(TemplateQueryRule.class,hql,new ArrayList<Object>()).getResultList();
        return templateQueryRuleList;
    }

    /**
     * 查询一级和二级报表信息（front-end developer has no inability，i have to do so）
     * @return
     */
    @GET
    @Path("get-all-template-query-rules")
    public List<TemplateReportRuleVo> getAllTemplateQueryRules(@QueryParam("templateId")String templateId, @QueryParam("ruleName")String ruleName){
        List<TemplateReportRuleVo> list = new ArrayList<>();
        String userId = UserUtils.getCurrentUser().getId();
        String hql = "from TemplateQueryRule where status<>'-1' and templateId = '"+templateId+"' and createBy = '"+userId+"'";
        if(!StringUtils.isEmptyParam(ruleName)){
            hql += " and ruleName = '"+ruleName+"'";
        }
        hql += " order by createDate asc";
        List<TemplateQueryRule> templateQueryRuleList = baseFacade.createQuery(TemplateQueryRule.class,hql,new ArrayList<Object>()).getResultList();
        Map<String,List<TemplateReportRuleVo>> firstMap = getFirstAndSecondRules(templateQueryRuleList,"0");
        Map<String,List<TemplateReportRuleVo>> secondMap = getFirstAndSecondRules(templateQueryRuleList,"1");
        for(String key:firstMap.keySet()){
            TemplateReportRuleVo templateReportRuleVo = firstMap.get(key).get(0);
            templateReportRuleVo.setReportRuleVoList(secondMap.get(key));
            list.add(templateReportRuleVo);
        }
        return list;
    }

    public TreeMap<String,List<TemplateReportRuleVo>> getFirstAndSecondRules(List<TemplateQueryRule> templateQueryRuleList,String type){
        TreeMap<String,List<TemplateReportRuleVo>> allMap = new TreeMap<>();
        for(TemplateQueryRule templateQueryRule:templateQueryRuleList){
            if("0".equals(type)){
                if(StringUtils.isEmptyParam(templateQueryRule.getParentId())){
                    List<TemplateReportRuleVo> firstList = new ArrayList<>();
                    TemplateReportRuleVo templateReportRuleVo = new TemplateReportRuleVo(templateQueryRule.getId(),templateQueryRule.getTemplateId(),
                            templateQueryRule.getRuleName(),templateQueryRule.getContent(),templateQueryRule.getRuleDesc(),
                            templateQueryRule.getCreateBy(),templateQueryRule.getModifyBy());
                    firstList.add(templateReportRuleVo);
                    allMap.put(templateReportRuleVo.getId(),firstList);
                }
            }else{
                if(!StringUtils.isEmptyParam(templateQueryRule.getParentId())){
                    TemplateReportRuleVo templateReportRuleVo = new TemplateReportRuleVo(templateQueryRule.getId(),templateQueryRule.getTemplateId(),
                            templateQueryRule.getRuleName(),templateQueryRule.getContent(),templateQueryRule.getRuleDesc(),
                            templateQueryRule.getCreateBy(),templateQueryRule.getModifyBy());
                    if(allMap.containsKey(templateQueryRule.getParentId())){
                        List<TemplateReportRuleVo> templateReportRuleVos = allMap.get(templateQueryRule.getParentId());
                        templateReportRuleVos.add(templateReportRuleVo);
                    }else{
                        List<TemplateReportRuleVo> templateReportRuleVos = new ArrayList<>();
                        templateReportRuleVos.add(templateReportRuleVo);
                        allMap.put(templateQueryRule.getParentId(),templateReportRuleVos);
                    }
                }
            }
        }
        return allMap;
    }

    @GET
    @Path("test-query")
    @Transactional
    public List<MongoResultVo> testQuery(@QueryParam("templateId")String templateId, @QueryParam("target")String target,
                                         @QueryParam("targetName")String targetName) throws Exception{
        MongoQueryVo mongoQueryVo = new MongoQueryVo();
        mongoQueryVo.setTarget(target);
        mongoQueryVo.setTargetName(targetName);
        mongoQueryVo.setTemplateId(templateId);
        List<QueryTerm> queryTerms = new ArrayList<>();
        QueryTerm queryTerm = new QueryTerm();
        queryTerm.setLogicOpt("and");
        queryTerm.setOperator("like");
        queryTerm.setParamName("dch_1517974002710");
        queryTerm.setValue("2");
        queryTerms.add(queryTerm);
        mongoQueryVo.setQueryParamList(queryTerms);
//        logger.debug("测试查询....");
//        logger.info("测试查询....");
//        logger.error("测试查询....");
        return getNewMongoResultVoByParam(mongoQueryVo);
    }

    /**
     * mongo查询统计结果，根据传入统计规则统计数量信息
     * @param mongoQueryVo
     * @return
     */
    @POST
    @Path("query-new-count-result")
    @Transactional
    public List<MongoResultVo> getNewMongoResultVoByParam(MongoQueryVo mongoQueryVo) throws Exception{
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        try {
            //saveOrUpdateQueryRule(mongoQueryVo);
//            Aggregation aggregation  = Aggregation.newAggregation(Aggregation.group(mongoQueryVo.getTarget()).count().as("value"));
//            Criteria criteria = where("templateId").is(mongoQueryVo.getTemplateId());
//            criteria =  analyseQueryVo(criteria,mongoQueryVo);
//            aggregation.match(criteria);
//            Query query =new Query(criteria);
//            List<Map> maps = mongoTemplate.aggregate(aggregation,collectionName,Map.class).getMappedResults();
//            for(Map map:maps){
//               String id = (String)map.get("_id");
//               String value = map.get("value")+"";
//               if(!StringUtils.isEmptyParam(id)){
//                   MongoResultVo mongoResultVo = new MongoResultVo();
//                   mongoResultVo.setName(id);
//                   mongoResultVo.setValue(Integer.valueOf(value));
//                   mongoResultVos.add(mongoResultVo);
//               }
//            }
            Criteria criteria = where("templateId").is(mongoQueryVo.getTemplateId());
            criteria =  analyseQueryVo(criteria,mongoQueryVo);
            GroupBy groupBy = GroupBy.key(mongoQueryVo.getTarget()).initialDocument("{value:0}")
                    .reduceFunction("function(doc,prev){prev.value+=1}");
            GroupByResults<BasicDBObject> groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
            Iterator iterator = groupByResults.iterator();
            Map codeMap = getMapValue(mongoQueryVo.getTarget());
            while(iterator.hasNext()){
                Document document = (Document)iterator.next();
                String name = document.get(mongoQueryVo.getTarget())+"";
                if(isNumeric(name)){
                    name = subZeroAndDot(name);
                }
                if(codeMap!=null && !codeMap.isEmpty()){
                    String value = codeMap.get(name)==null?"":codeMap.get(name).toString();
                    if(!StringUtils.isEmptyParam(value)){
                        name = value;
                    }
                }
                String value = document.get("value")+"";
                if(!"null".equals(name)){
                    MongoResultVo mongoResultVo = new MongoResultVo();
                    mongoResultVo.setName(name);
                    mongoResultVo.setValue(Double.valueOf(value).intValue());
                    mongoResultVos.add(mongoResultVo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("查询统计异常");
        }
        return mongoResultVos;
    }

    /**
     * mongo查询统计结果，根据传入统计规则统计数量信息
     * @param mongoQueryVo
     * @return
     */
    @POST
    @Path("query-count-result")
    @Transactional
    public List<MongoResultVo> getMongoResultVoByParam(MongoQueryVo mongoQueryVo) throws Exception{
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        try {
            //saveOrUpdateQueryRule(mongoQueryVo);
            Criteria criteria = where("templateId").is(mongoQueryVo.getTemplateId());
            criteria =  analyseQueryVo(criteria,mongoQueryVo);
            GroupBy groupBy = GroupBy.key(mongoQueryVo.getTarget()).initialDocument("{value:0}")
                    .reduceFunction("function(doc,prev){prev.value+=1}");
            GroupByResults<BasicDBObject> groupByResults = mongoTemplate.group(criteria,collectionName,groupBy,BasicDBObject.class);
            Iterator iterator = groupByResults.iterator();
            Map codeMap = getMapValue(mongoQueryVo.getTarget());
            while(iterator.hasNext()){
                Document document = (Document)iterator.next();
                String name = document.get(mongoQueryVo.getTarget())+"";
                if(isNumeric(name)){
                    name = subZeroAndDot(name);
                }
                if(codeMap!=null && !codeMap.isEmpty()){
                    String value = codeMap.get(name)==null?"":codeMap.get(name).toString();
                    if(!StringUtils.isEmptyParam(value)){
                        name = value;
                    }
                }
                String value = document.get("value")+"";
                if(!"null".equals(name)){
                    MongoResultVo mongoResultVo = new MongoResultVo();
                    mongoResultVo.setName(name);
                    mongoResultVo.setValue(Double.valueOf(value).intValue());
                    mongoResultVos.add(mongoResultVo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("查询统计异常");
        }
        return mongoResultVos;
    }

    /**
     * 保存修改删除查询规则
     * @param templateQueryRule
     * @return
     * @throws Exception
     */
    @POST
    @Path("merge-query-rule")
    @Transactional
    public Response mergeQueryRule(TemplateQueryRule templateQueryRule) throws Exception{
        if(!"-1".equals(templateQueryRule.getStatus())){//新增设置状态值
            String userId = UserUtils.getCurrentUser().getId();
            String hql = "select templateId from TemplateQueryRule where status<>'-1' and templateId = '"+templateQueryRule.getTemplateId()+"' and ruleName = '"+templateQueryRule.getRuleName()+"' and id<>'"+templateQueryRule.getId()+"'" +
                         " and createBy = '"+userId+"'";
            List<String> list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
            if(list!=null && !list.isEmpty()){
                throw new Exception("相同表单模板下不能添加相同的规则名称");
            }
            templateQueryRule.setStatus("0");
        }
        TemplateQueryRule merge = baseFacade.merge(templateQueryRule);
        return Response.status(Response.Status.OK).entity(merge).build();
    }
    /**
     * 保存或者更新查询条件
     * @param mongoQueryVo
     * @throws Exception
     */
    @Transactional
    public void saveOrUpdateQueryRule(MongoQueryVo mongoQueryVo)throws Exception{
        String hql = " from TemplateQueryRule where status<>'-1' and templateId = '"+mongoQueryVo.getTemplateId()+"' and ruleName = '"+mongoQueryVo.getTargetName()+"'";
        List<TemplateQueryRule> templateQueryRules = baseFacade.createQuery(TemplateQueryRule.class,hql,new ArrayList<Object>()).getResultList();
        if(templateQueryRules!=null && !templateQueryRules.isEmpty()){
            TemplateQueryRule templateQueryRule = templateQueryRules.get(0);
            templateQueryRule.setContent(JSONUtil.objectToJsonString(mongoQueryVo));
            baseFacade.merge(templateQueryRule);
        }else{
            TemplateQueryRule templateQueryRule = new TemplateQueryRule();
            templateQueryRule.setTemplateId(mongoQueryVo.getTemplateId());
            templateQueryRule.setRuleName(mongoQueryVo.getTargetName());
            templateQueryRule.setContent(JSONUtil.objectToJsonString(mongoQueryVo));
            templateQueryRule.setStatus("0");
            baseFacade.merge(templateQueryRule);
        }
    }

    public Criteria analyseQueryVo(Criteria criteria,MongoQueryVo mongoQueryVo){
        if(mongoQueryVo!=null && criteria!=null){
            if(mongoQueryVo.getQueryParamList()!=null && !mongoQueryVo.getQueryParamList().isEmpty()){
               List<QueryTerm> queryParamList = mongoQueryVo.getQueryParamList();
               for(QueryTerm queryParam:queryParamList){
                   String value = queryParam.getValue();
                   //Map returnMap = getCodeName2ValueMap(queryParam.getParamName());
                   List<String> valueList = getValueListByNameAndCode(queryParam.getParamName(),value);
                   if(valueList!=null && !valueList.isEmpty()){
                       value = valueList.get(0);
                   }
                   if("and".equals(queryParam.getLogicOpt()) || StringUtils.isEmptyParam(queryParam.getLogicOpt())){
                       if("=".equals(queryParam.getOperator())){
                           if("0".equals(value)||(isNumeric(value) && !value.startsWith("0"))){
                               criteria.and(queryParam.getParamName()).is(Double.valueOf(value));
                           }else{
                               criteria.and(queryParam.getParamName()).is(value);
                           }
                       }else if(">".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).gt(Double.valueOf(value));
                       }else if(">=".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).gte(Double.valueOf(value));
                       }else if("<".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).lt(Double.valueOf(value));
                       }else if("<=".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).lte(Double.valueOf(value));
                       }else if("<>".equals(queryParam.getOperator())){
                           if("0".equals(value)||(isNumeric(value) && !value.startsWith("0"))){
                               criteria.and(queryParam.getParamName()).ne(Double.valueOf(value));
                           }else{
                               criteria.and(queryParam.getParamName()).ne(value);
                           }
                       }else if("like".equals(queryParam.getOperator())){
                           if(valueList!=null && !valueList.isEmpty()){
                               criteria.and(queryParam.getParamName()).in(valueList);
                           }else{
                               criteria.and(queryParam.getParamName()).regex(".*?" + queryParam.getValue() + ".*");//? like
                           }
                       }
                   }else{
                       if("=".equals(queryParam.getOperator())){
                           if("0".equals(value)||(isNumeric(value) && !value.startsWith("0"))){
                               criteria.orOperator(where(queryParam.getParamName()).is(Double.valueOf(value)));
                           }else{
                               criteria.orOperator(where(queryParam.getParamName()).is(value));
                           }
                       }else if(">".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).gt(Double.valueOf(value)));
                       }else if(">=".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).gte(Double.valueOf(value)));
                       }else if("<".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).lt(Double.valueOf(value)));
                       }else if("<=".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).lte(Double.valueOf(value)));
                       }else if("<>".equals(queryParam.getOperator())){
                           if("0".equals(value)||(isNumeric(value) && !value.startsWith("0"))){
                               criteria.orOperator(where(queryParam.getParamName()).ne(Double.valueOf(value)));
                           }else{
                               criteria.orOperator(where(queryParam.getParamName()).ne(value));
                           }
                       }else if("like".equals(queryParam.getOperator())){
                           if(valueList!=null && !valueList.isEmpty()){
                               criteria.orOperator(where(queryParam.getParamName()).in(valueList));//? like
                           }else{
                               criteria.orOperator(where(queryParam.getParamName()).regex(".*?" + queryParam.getValue() + ".*"));//? like
                           }
                       }
                   }
               }
            }
        }
        return criteria;
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
    /**
     * 根据编码获取名称和值域的对照关系集合
     * @param dataElementCode
     * @return
     */
    public Map getCodeName2ValueMap(String dataElementCode){
        Map returnMap = new HashMap();
        Map map = getMapValue(dataElementCode);
        for(Object obj:map.keySet()){
            returnMap.put(map.get(obj),obj);
        }
        return returnMap;
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


    @GET
    @Path("test-log")
    public List<String> testQuery(@QueryParam("templateId")String templateId) throws Exception{
//        logger.debug("测试查询...debug.");
//        logger.info("测试查询....info");
//        logger.error("测试查询....error");
        List list = new ArrayList();
        list.add("sucess");
        Query query = new Query();
        query.addCriteria(Criteria.where("masterId").gt("8aa183c362a94b5b0162a94c00280090"));
        query.fields().include("_id");
        query.fields().include("dch_1523154179240");
        query.fields().include("dch_1523154196755");
        query.fields().include("dch_1523154214456");
        query.fields().include("dch_1523179199291.dch_1523179560994");
        List<Document> result = mongoTemplate.find(query,Document.class,"templateFilling");
        for(int i=0;i<result.size();i++){
            Document document = (Document)result.get(i);
            System.out.println(document.get("dch_1523154179240"));
        }
        String json = JSONUtil.objectToJsonString(result);
        System.out.println(json);
        return list;
    }
}
