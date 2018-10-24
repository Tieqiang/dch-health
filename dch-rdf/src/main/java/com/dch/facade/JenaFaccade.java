package com.dch.facade;

import com.dch.facade.common.BaseFacade;
import com.dch.util.JenaUtil;
import com.dch.util.StringUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.hibernate.annotations.Synchronize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/5/14.
 */
@Component
public class JenaFaccade extends BaseFacade {

    public Map<String,Map> getQueryResultMap(String queryString,String label,String dbName){
        Map<String,Map> resultMap = new HashMap<String,Map>();
        List<Map<String,Object>> list = getRdfQuerySetToMap(queryString,dbName);
        for(Map lmap:list){
            String subject = lmap.get("subject")+"";
            String predicate = lmap.get("predicate")+"";
            String object = lmap.get("object")+"";
            if(object.contains("http") && !object.endsWith("Individual") && !object.endsWith("#Class")){
                System.out.println(subject +" | "+predicate+" | "+object);
                if(!JenaUtil.DEFAULT_DB.equals(dbName)){
                    String slabel = lmap.get("slabel")+"";
                    String olabel = lmap.get("olabel")+"";
                    subject = subject + "#" + slabel;
                    object = object + "#" + olabel;
                }
                if(resultMap.containsKey(subject)){
                    Map map = resultMap.get(subject);
                    map.put(object,predicate);
                }else{
                    Map map = new HashMap();
                    map.put(object,predicate);
                    resultMap.put(subject,map);
                }
            }
        }
        return resultMap;
    }

    public synchronized List<Map<String,Object>> getRdfQuerySetToMap(String queryString,String dbName){
        List<Map<String,Object>> list = new ArrayList<>();
        try{
            String directory = JenaUtil.getRdfdb(dbName);
            System.out.println(directory);
            Dataset dataset = TDBFactory.createDataset(directory);
            Model model = dataset.getDefaultModel();
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            while(results.hasNext()){
                QuerySolution qs = results.next();
                String subject = qs.get("subject")==null?"":qs.get("subject").toString();
                String predicate = qs.get("predicate")==null?"":qs.get("predicate").toString();
                String object = qs.get("object")==null?"":qs.get("object").toString();
                Map<String,Object> map = new HashMap<>();
                map.put("subject",subject);
                map.put("predicate",predicate);
                map.put("object",object);
                if(!JenaUtil.DEFAULT_DB.equals(dbName)){
                    String slabel = qs.get("slabel")==null?"":qs.get("slabel").toString();
                    String olabel = qs.get("olabel")==null?"":qs.get("olabel").toString();
                    map.put("slabel",slabel);
                    map.put("olabel",olabel);
                }
                list.add(map);
            }
            qe.close();
            dataset.end();
            dataset.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
