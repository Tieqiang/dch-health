package com.dch.service;

import com.dch.facade.JenaFaccade;
import com.dch.facade.RedisFacade;
import com.dch.util.JenaUtil;
import com.dch.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.*;

/**
 * Created by sunkqa on 2018/5/10.
 */
@Produces("application/json")
@Path("redis")
@Controller
public class RedisQueryService {

    @Autowired
    private JenaFaccade jenaFaccade;

    @Autowired
    private RedisFacade redisFacade;
    @PostConstruct
    public void iniRedisCache(){
        Set<String> set = redisFacade.getRedisTemplate().keys("*");
        if(set==null || set.isEmpty()){
            List<String> nameList = getRdfDatas();
            if(nameList!=null && !nameList.isEmpty()){
                for(String rname:nameList){
                    redisFacade.getRedisTemplate().opsForValue().set(rname,rname);
                }
            }
        }
    }

    public List<String> getRdfDatas(){
        List<String> nameList = new ArrayList<String>();
        String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT DISTINCT ?subject  WHERE { ?subject ?predicate ?object . " +
                "?subject rdf:type ?type FILTER (?type = <http://www.w3.org/2002/07/owl#NamedIndividual>) } ";
        List<Map<String,Object>> list = jenaFaccade.getRdfQuerySetToMap(sparql,JenaUtil.DEFAULT_DB);
        for(Map map:list){
            String name = map.get("subject")==null?"":map.get("subject").toString();
            name = JenaUtil.getNameByRdfId(name);
            if(!StringUtils.isEmptyParam(name)){
                nameList.add(name);
            }
        }
        return nameList;
    }
    /**
     * 根据传入内容获取推荐的查询信息
     * @param label
     * @param type 0:以label开头 1:以label结尾 2:包含label *：通配任意多个字符 ?：通配单个字符 []：通配括号内的某一个字符
     * @return
     */
    @GET
    @Path("get-recommend-datas")
    public List<String> getRecommendDatas(@QueryParam("label")String label, @QueryParam("type")String type){
        List<String> datas = new ArrayList<String>();
        if(!StringUtils.isEmptyParam(label)){
            //Set<String> myset = redisFacade.getRedisTemplate().keys("*");
            if("0".equals(type)){
                Set set = redisFacade.getRedisTemplate().keys(label+"*");
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = redisFacade.readStringValue(key);
                    System.out.println(key + value);
                    datas.add(value);
                }
            }else if("1".equals(type)){
                Set set = redisFacade.getRedisTemplate().keys("*"+label);
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = redisFacade.readStringValue(key);
                    System.out.println(key + value);
                    datas.add(value);
                }
            }else{
                Set set = redisFacade.getRedisTemplate().keys("*"+label+"*");
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = redisFacade.readStringValue(key);
                    System.out.println(key + value);
                    datas.add(value);
                }
            }
        }
        return datas;
    }
}
