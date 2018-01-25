package com.dch.service;

import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.MongoQueryVo;
import com.dch.vo.MongoResultVo;
import com.dch.vo.Person;
import com.dch.vo.QueryTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Controller
@Produces("application/json")
@Path("mongo")
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate ;

    @Autowired
    private BaseFacade baseFacade;

    @GET
    @Path("get-result")
    public void mongo(){
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
                //mongoTemplate.insert(toMongoResult,"templateResult");
            }
        }
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

    @GET
    @Path("test-query")
    public List<MongoResultVo> testQuery(@QueryParam("templateId")String templateId, @QueryParam("target")String target,
                                         @QueryParam("targetName")String targetName){
        MongoQueryVo mongoQueryVo = new MongoQueryVo();
        mongoQueryVo.setTarget(target);
        mongoQueryVo.setTargetName(targetName);
        mongoQueryVo.setTemplateId(templateId);
        List<QueryTerm> queryTerms = new ArrayList<>();
        QueryTerm queryTerm = new QueryTerm();
        queryTerm.setLogicOpt("and");
        queryTerm.setOperator(">");
        queryTerm.setParamName(target);
        queryTerm.setValue("12");
        queryTerms.add(queryTerm);
        mongoQueryVo.setQueryParamList(queryTerms);
        return getMongoResultVoByParam(mongoQueryVo);
    }

    /**
     * mongo查询统计结果，根据传入统计规则统计数量信息
     * @param mongoQueryVo
     * @return
     */
    @POST
    @Path("query-count-result")
    public List<MongoResultVo> getMongoResultVoByParam(MongoQueryVo mongoQueryVo){
        List<MongoResultVo> mongoResultVos = new ArrayList<>();
        Query query = new Query();
        Map map = getMapValue(mongoQueryVo.getTarget());
        if(map!=null && map.size()>1){
            for(Object key:map.keySet()){
                Criteria criteria = where("templateId").is(mongoQueryVo.getTemplateId());
                criteria =  analyseQueryVo(criteria,mongoQueryVo);
                String value = (String)map.get(key);
                criteria.and(mongoQueryVo.getTarget()).is(value);//查询男或女的数量
                query.addCriteria(criteria);
                List<Object> userList1 = mongoTemplate.find(query, Object.class,"templateResult");
                MongoResultVo mongoResultVo = new MongoResultVo();
                mongoResultVo.setName(key+"");
                mongoResultVo.setValue(userList1!=null?userList1.size():0);
                criteria = null;
                query = new Query();
                mongoResultVos.add(mongoResultVo);
            }
        }else{
            Criteria criteria = where("templateId").is(mongoQueryVo.getTemplateId());
            criteria =  analyseQueryVo(criteria,mongoQueryVo);
            query.addCriteria(criteria);
            List<Object> userList1 = mongoTemplate.find(query, Object.class,"templateResult");
            MongoResultVo mongoResultVo = new MongoResultVo();
            mongoResultVo.setName(mongoQueryVo.getTargetName());
            mongoResultVo.setValue(userList1!=null?userList1.size():0);
            mongoResultVos.add(mongoResultVo);
        }
        return mongoResultVos;
    }

    public Criteria analyseQueryVo(Criteria criteria,MongoQueryVo mongoQueryVo){
        if(mongoQueryVo!=null && criteria!=null){
            if(mongoQueryVo.getQueryParamList()!=null && !mongoQueryVo.getQueryParamList().isEmpty()){
               List<QueryTerm> queryParamList = mongoQueryVo.getQueryParamList();
               for(QueryTerm queryParam:queryParamList){
                   if("and".equals(queryParam.getLogicOpt())){
                       if("=".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).is(queryParam.getValue());
                       }else if(">".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).gt(queryParam.getValue());
                       }else if(">=".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).gte(queryParam.getValue());
                       }else if("<".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).lt(queryParam.getValue());
                       }else if("<=".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).lte(queryParam.getValue());
                       }else if("<>".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).ne(queryParam.getValue());
                       }else if("like".equals(queryParam.getOperator())){
                           criteria.and(queryParam.getParamName()).regex(".*?" + queryParam.getValue() + ".*");//? like
                       }
                   }else{
                       if("=".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).is(queryParam.getValue()));
                       }else if(">".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).gt(queryParam.getValue()));
                       }else if(">=".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).gte(queryParam.getValue()));
                       }else if("<".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).lt(queryParam.getValue()));
                       }else if("<=".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).lte(queryParam.getValue()));
                       }else if("<>".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).ne(queryParam.getValue()));
                       }else if("like".equals(queryParam.getOperator())){
                           criteria.orOperator(where(queryParam.getParamName()).regex(".*?" + queryParam.getValue() + ".*"));//? like
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
                map.put(innerParams[0].toString(),innerParams[1]);
            }
        }
        return map;
    }
}
