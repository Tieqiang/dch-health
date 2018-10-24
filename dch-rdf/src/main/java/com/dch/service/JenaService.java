package com.dch.service;

import com.dch.facade.JenaFaccade;
import com.dch.util.JSONUtil;
import com.dch.util.JenaConst;
import com.dch.util.JenaUtil;
import com.dch.util.StringUtils;
import com.dch.vo.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sunkqa on 2018/5/9.
 */
@Produces("application/json")
@Path("rdf/jena")
@Controller
public class JenaService {

    @Autowired
    private JenaFaccade jenaFaccade;

    @GET
    @Path("get-rdf-query-result")
    public RdfDataVo getRdfResultVoByParam(@QueryParam("label")String label,@QueryParam("dbType")String dbType) throws Exception{
//        String directory = JenaUtil.getRdfdb();
//        System.out.println(directory);
//        Dataset dataset = TDBFactory.createDataset(directory);
//        Model model = dataset.getDefaultModel();
        String queryString = "";
        String dbName = StringUtils.isEmptyParam(dbType)?"drugdb":JenaConst.DB_TYPE.getName(dbType);
        if(JenaUtil.DEFAULT_DB.equals(dbName)){
            if(StringUtils.isEmptyParam(label)){
                //什么也不输入查询的语句
                queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "SELECT ?object ?predicate ?subject WHERE  "
                        + "{ ?object ?predicate ?subject. ";
                queryString += " FILTER (?subject = <http://www.imicams.ac.cn/administrator/ontologies/2018/4/medical-ontologies#imicams_kg>)}";
            }else {
                queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "SELECT ?subject ?object ?predicate  WHERE  "
                        + "{ {?subject ?predicate ?object." +
                        " ?subject rdfs:label ?label. " +
                        " FILTER (?label = '"+label+"')} union {"
                        + "?subject ?predicate ?object." +
                        " ?object rdfs:label ?label. " +
                        " FILTER (?label = '"+label+"')}}";
            }
        }else {
            if(StringUtils.isEmptyParam(label)){
                //什么也不输入查询的语句
                queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "SELECT ?object ?predicate ?subject ?olabel ?slabel WHERE  "
                        + "{ ?object ?predicate ?subject. " +
                        " ?object rdfs:label ?olabel. " +
                        " ?subject rdfs:label ?slabel. ";
                queryString += " FILTER (?subject = <http://www.semanticweb.org/administrator/ontologies/2018/9/jkzg-ontology-42#OWLClass_00000000000000000000>)}";
            }else {
                queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "SELECT ?object ?predicate ?subject ?olabel ?slabel  "
                        + "WHERE { ?object ?predicate ?subject. " +
                        " ?object rdfs:label ?olabel. " +
                        " ?subject rdfs:label ?slabel. " +
                        " FILTER (?olabel = '"+label+"'|| ?slabel = '"+label+"')}";
            }
        }

//        Query query = QueryFactory.create(queryString);
//        QueryExecution qe = QueryExecutionFactory.create(query, model);
//        ResultSet results = qe.execSelect();
        Map<String,Map> resultMap = jenaFaccade.getQueryResultMap(queryString,label,dbName);
        String middleKey = JenaUtil.getMiddleKey(resultMap);
        RdfDataVo rdfDataVo = new RdfDataVo();
        rdfDataVo.setCode("200");
        List<RdfEntity> rdfEntityList = new ArrayList<>();
        List<RdfRelation> rdfRelationList = new ArrayList<>();
        Map nameMap = new HashMap();
        for(String key:resultMap.keySet()){
            if(!nameMap.containsKey(key)){
                RdfEntity rdfEntity = new RdfEntity();
                rdfEntity.setId(JenaUtil.getIdByKey(key,dbName));
                rdfEntity.setImage("");
                if(key.equals(middleKey)){
                    rdfEntity.setKgType("0");
                }else{
                    rdfEntity.setKgType("1");
                }
                rdfEntity.setName(JenaUtil.getNameByRdfId(key));
                rdfEntityList.add(rdfEntity);
                nameMap.put(key,"");
            }
            Map innerMap = resultMap.get(key);
            for(Object innerKey:innerMap.keySet()){
                String inKey = innerKey.toString();
                if(!nameMap.containsKey(inKey)){
                    nameMap.put(inKey,"");
                    RdfEntity rdfEntity1 = new RdfEntity();
                    rdfEntity1.setId(JenaUtil.getIdByKey(inKey,dbName));
                    rdfEntity1.setImage("");
                    if(key.equals(middleKey)){
                        rdfEntity1.setKgType("0");
                    }else{
                        rdfEntity1.setKgType("1");
                    }
                    rdfEntity1.setName(JenaUtil.getNameByRdfId(inKey));
                    rdfEntityList.add(rdfEntity1);
                }
                String value = innerMap.get(inKey)+"";
                RdfRelation rdfRelation = new RdfRelation();
                rdfRelation.setId(JenaUtil.getUID());//value
                rdfRelation.setAttId(value);
                if(value.endsWith("#type")){
                    rdfRelation.setAttName("实体");
                    rdfRelation.setFrom(JenaUtil.getIdByKey(inKey,dbName));
                    rdfRelation.setTo(JenaUtil.getIdByKey(key,dbName));
                    rdfRelation.setSource(JenaUtil.getNameByRdfId(inKey));
                    rdfRelation.setTarget(JenaUtil.getNameByRdfId(key));
                }else if(value.endsWith("#subClassOf")){
                    rdfRelation.setAttName(JenaConst.RELATION_TYPE.getName(value));
                    rdfRelation.setFrom(JenaUtil.getIdByKey(inKey,dbName));
                    rdfRelation.setTo(JenaUtil.getIdByKey(key,dbName));
                    rdfRelation.setSource(JenaUtil.getNameByRdfId(inKey));
                    rdfRelation.setTarget(JenaUtil.getNameByRdfId(key));
                }else{
                    rdfRelation.setAttName(JenaConst.RELATION_TYPE.getName(value));
                    rdfRelation.setFrom(JenaUtil.getIdByKey(key,dbName));
                    rdfRelation.setTo(JenaUtil.getIdByKey(inKey,dbName));
                    rdfRelation.setSource(JenaUtil.getNameByRdfId(key));
                    rdfRelation.setTarget(JenaUtil.getNameByRdfId(inKey));
                }
                rdfRelationList.add(rdfRelation);
            }
        }
        rdfDataVo.setRdfEntityList(rdfEntityList);
        rdfDataVo.setRdfRelationList(rdfRelationList);
        System.out.println(JSONUtil.objectToJson(rdfDataVo));

//        RdfResultVo rdfResultVo = new RdfResultVo();
//        List<RdfChildren> childrenList = new ArrayList<>();
//        if(!resultMap.isEmpty() && resultMap.size()<2){
//            for(String key:resultMap.keySet()){
//                rdfResultVo.setId(key);
//                rdfResultVo.setAttId("");
//                rdfResultVo.setAttName("");
//                rdfResultVo.setImage("");
//                rdfResultVo.setKgType("1");
//                rdfResultVo.setName(JenaUtil.getNameByRdfId(key));
//                Map map = resultMap.get(key);
//                for(Object chkey:map.keySet()){
//                    String childId = chkey.toString();
//                    String value = map.get(childId)+"";
//                    if(value.endsWith("#type")){
//                        RdfChildren parent = new RdfChildren();
//                        parent.setKgType("0");
//                        parent.setId(childId);
//                        parent.setImage("");
//                        parent.setName(JenaUtil.getNameByRdfId(childId));
//                        rdfResultVo.setAttName("实体");
//                        rdfResultVo.setAttId(value);
//                        rdfResultVo.setParent(parent);
//                    }else{
//                        RdfChildren rdfChildren = new RdfChildren();
//                        rdfChildren.setId(childId);
//                        rdfChildren.setImage("");
//                        rdfChildren.setKgType("2");
//                        rdfChildren.setAttId(value);
//                        rdfChildren.setAttName(JenaConst.RELATION_TYPE.getName(value));
//                        rdfChildren.setName(JenaUtil.getNameByRdfId(childId));
//                        childrenList.add(rdfChildren);
//                    }
//                }
//                rdfResultVo.setChildrens(childrenList);
//            }
//        }else if(!resultMap.isEmpty()){
//            String middleKey = JenaUtil.getMiddleKey(resultMap);
//            String firstKey = resultMap.get(middleKey).keySet().iterator().next()+"";
//
//            RdfChildren parent = new RdfChildren();
//            parent.setKgType("0");
//            parent.setId(firstKey);
//            parent.setImage("");
//            parent.setName(JenaUtil.getNameByRdfId(firstKey));
//            rdfResultVo.setParent(parent);
//            Map seMap = resultMap.get(middleKey);
//            String seId = middleKey;
//            String attId="";
//            for(Object seKey:seMap.keySet()){
//                attId = seMap.get(seKey)+"";
//            }
//            String relation= JenaConst.RELATION_TYPE.getName(attId);
//            int seIndex = seId.lastIndexOf("#");
//            rdfResultVo.setId(seId);
//            rdfResultVo.setAttId(attId);
//            rdfResultVo.setAttName(relation);
//            rdfResultVo.setImage("");
//            rdfResultVo.setKgType("1");
//            rdfResultVo.setName(seId.substring(seIndex+1));
//            for(String key:resultMap.keySet()){
//                Map innerMap = resultMap.get(key);
//                if(innerMap.containsKey(middleKey)){
//                    String value = innerMap.get(middleKey)+"";
//                    RdfChildren rdfChildren = new RdfChildren();
//                    rdfChildren.setId(key);
//                    rdfChildren.setImage("");
//                    rdfChildren.setKgType("2");
//                    rdfChildren.setAttId(value);
//                    rdfChildren.setAttName(JenaConst.RELATION_TYPE.getName(value));
//                    rdfChildren.setName(JenaUtil.getNameByRdfId(key));
//                    childrenList.add(rdfChildren);
//                }
//            }
//            rdfResultVo.setChildrens(childrenList);
//        }
//        qe.close();
//        dataset.end();
//        dataset.close();
        return rdfDataVo;
    }

    @GET
    @Path("get-rdf-entity-by-id")
    public List<RdfElementVo> getRdfElementVoById(@QueryParam("id") String rdfId,@QueryParam("dbName")String dbName){
        String directory = JenaUtil.getRdfdb(dbName);
        Dataset dataset = TDBFactory.createDataset(directory);
        Model model = dataset.getDefaultModel();
        List<RdfElementVo> rdfElementVos = new ArrayList<>();
        try {
            rdfId = URLDecoder.decode(rdfId, "UTF-8");
            if(!StringUtils.isEmptyParam(rdfId)){
                String  queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "SELECT ?subject ?predicate ?object WHERE  "
                        + "{ ?subject ?predicate ?object. "
                        + "FILTER (?subject =<"+rdfId+">)}";
                Query query = QueryFactory.create(queryString);
                QueryExecution qe = QueryExecutionFactory.create(query, model);
                ResultSet results = qe.execSelect();
                Map<String,Map> resultMap = new HashMap<String,Map>();
                while(results.hasNext()){
                    QuerySolution qs = results.next();
                    String subject = qs.get("subject")+"";
                    String predicate = qs.get("predicate")+"";
                    String object = qs.get("object")+"";
                    System.out.println(subject +" | "+predicate+" | "+object);
                    if(!predicate.endsWith("type") && !object.endsWith("Individual") && !object.endsWith("#Class")){
                        RdfElementVo rdfElementVo = new RdfElementVo();
                        rdfElementVo.setId(predicate);
                        String name = JenaUtil.getNameByRdfId(predicate);
                        if("produceBy".equals(name)){
                            name = "生产厂商";
                        }
                        rdfElementVo.setName(name);
                        if(object.contains("http")){
                            rdfElementVo.setValue(JenaUtil.getNameByRdfId(object));
                        }else{
                            String realValue = JenaUtil.delHtmlTag(object);
                            rdfElementVo.setValue(realValue);
                        }
                        rdfElementVos.add(rdfElementVo);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return rdfElementVos;
    }

//    public static void main(String args[]) throws Exception{
//        System.out.println(JenaUtil.getUID());
//        JenaService jenaService = new JenaService();
//        RdfDataVo rdfDataVo = jenaService.getRdfResultVoByParam("武汉人福药业有限责任公司");
//        List<RdfElementVo> rdfElementVos = jenaService.getRdfElementVoById("http://www.imicams.ac.cn/administrator/ontologies/2018/4/medical-ontologies#三维制霉素栓");
//        System.out.println(JSONUtil.objectToJson(rdfDataVo));
//    }
}
