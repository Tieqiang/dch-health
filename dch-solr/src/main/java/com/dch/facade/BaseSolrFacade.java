package com.dch.facade;

import com.dch.facade.common.VO.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/24.
 */
@Component
public class BaseSolrFacade {

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name ="queueDestination")
    private Destination destination;

    /**
     * 向mq添加消息vo
     * @param obj
     */
    public void addObjectMessageToMq(final Object obj){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage((Serializable) obj);
                return objectMessage;
            }
        });
    }
    /**
     * 添加信息到索引库
     * @param object
     * @throws SolrServerException
     * @throws IOException
     */
    public void sendIndexToSolr(Object object) throws SolrServerException, IOException {
        SolrInputDocument document  = new SolrInputDocument();
        try {
            Class clazz = object.getClass();
            Field[] fileds = object.getClass().getDeclaredFields();
            for(Field field:fileds){
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(),clazz);
                Method getMethod = pd.getReadMethod();//获得get方法
                Object o = getMethod.invoke(object);//执行get方法返回一个Object
                document.addField(field.getName(), o==null?"":o.toString());
            }
            document.addField("tableName",clazz.getSimpleName());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //把信息添加到索引库中
        httpSolrServer.add(document);
        //提交事务
        httpSolrServer.commit();
    }
    /**
     * 根据id查询索引的信息
     * @param id
     * @param type
     * @param <T>
     * @return
     * @throws Exception
     */
    public  <T> T getSolrObjectById(String id, Class<T> type) throws Exception{
        SolrQuery query = new SolrQuery();// 查询
        query.setQuery("id:"+id);
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        if(docs==null || docs.isEmpty()){
            throw new Exception("索引信息不存在");
        }
        SolrDocument doc = docs.get(0);
        T ret = getDto(type,doc,null);
        return ret;
    }

    /**
     * 根据查询条件查询索引库信息
     * @param param
     * @param type
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> getSolrObjectByParam(String param, Class<T> type) throws Exception{
        List<T> result = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        if(param==null||"".equals(param)){
            param = "*:*";
        }
        if(type!=null){
            param = param+" AND tableName:"+type.getSimpleName();
        }
        query.setQuery(param);
        query.addSort("firstPy",SolrQuery.ORDER.asc);
        query.addSort("category",SolrQuery.ORDER.asc);
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        if(docs!=null && !docs.isEmpty()){
            for(SolrDocument doc : docs){
                result.add(getDto(type,doc,null));
            }
        }
        return result;
    }

    /**
     * 根据参数名，每页条数，当前页查询索引库信息
     * @param param
     * @param perPage
     * @param currentPage
     * @param type
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Page<T> getSolrObjectByParamAndPageParm(String param,String hlFields,int perPage,int currentPage, Class<T> type) throws Exception{
        Page<T> resul = new Page<>();
        List<T> resultList = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询

        if(param==null||"".equals(param)){
            param = "*:*";
        }
        if(type!=null){
            //param = param+" AND tableName:"+type.getSimpleName();
            query.addFilterQuery("tableName:"+type.getSimpleName());
        }
        query.setQuery(param);
        query.addSort("firstPy",SolrQuery.ORDER.asc);
        query.addSort("category",SolrQuery.ORDER.asc);
        if(hlFields!=null && !"".equals(hlFields)){
            //开启高亮
            query.setHighlight(true);
            //高亮显示的格式
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");
            //我需要那几个字段进行高亮
            query.setParam("hl.fl", hlFields);
            query.setHighlightSnippets(2);//结果分片数，默认为1
            query.setHighlightFragsize(10000);//每个分片的最大长度，默认为100
        }
//        if(isFilter){
//            query.addFilterQuery(filterKeyWords);
//        }
        if(currentPage<=0){
            currentPage = 1;
        }
        if(perPage<=0){
            perPage = 15;
        }
        query.setStart((currentPage-1)*perPage);
        query.setRows(perPage);

        QueryResponse queryResponse = httpSolrServer.query(query, SolrRequest.METHOD.POST);
        //返回所有的结果...
        SolrDocumentList childDocs=queryResponse.getResults();
        Long total = childDocs.getNumFound();
        Map<String, Map<String, List<String>>> maplist=queryResponse.getHighlighting();
        resul.setPerPage((long)perPage);
        resul.setCounts((long)total);
        for(SolrDocument sd : childDocs){
            resultList.add(getDto(type,sd,maplist));
        }
        resul.setData(resultList);
        return resul;
    }

    public <T> Page<T> getExactSolrVoByParamAndPageParm(String keyWords,String fiterStr,String hlFields,int perPage,int currentPage, Class<T> type) throws Exception{
        Page<T> resul = new Page<>();
        List<T> resultList = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        //设置默认查询
        query.set("df", "exactkeywords");
        String orignKeyWords = keyWords;
        if (keyWords != null && !"".equals(keyWords)) {
            if(keyWords.indexOf(" ")!=-1){
                keyWords = keyWords.replace(" ","* *");
                keyWords = "(*"+keyWords+"*)";
            }else{
                keyWords = "*"+keyWords+"*";//模糊匹配
            }
        }else {
            keyWords = "*:*";
        }
        if(type!=null){
            if(fiterStr!=null && !"".equals(fiterStr)){
                query.addFilterQuery(fiterStr+" AND tableName:"+type.getSimpleName());
            }else{
                query.addFilterQuery("tableName:"+type.getSimpleName());
            }
        }
        query.setQuery(keyWords);
        query.addSort("firstPy",SolrQuery.ORDER.asc);
        query.addSort("category",SolrQuery.ORDER.asc);
        if(hlFields!=null && !"".equals(hlFields)){
            //开启高亮
            query.setHighlight(true);
            //高亮显示的格式
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");
            //我需要那几个字段进行高亮
            query.setParam("hl.fl", hlFields);
            //query.set("hl.preserveMulti","true");
            query.set("hl.highlightMultiTerm","true");
            query.set("hl.usePhraseHighlighter","true");
            query.setHighlightSnippets(2);//结果分片数，默认为1
            query.setHighlightFragsize(10000);//每个分片的最大长度，默认为100
        }

        if(currentPage<=0){
            currentPage = 1;
        }
        if(perPage<=0){
            perPage = 15;
        }
        query.setStart((currentPage-1)*perPage);
        query.setRows(perPage);

        QueryResponse queryResponse = httpSolrServer.query(query, SolrRequest.METHOD.POST);
        //返回所有的结果...
        SolrDocumentList childDocs=queryResponse.getResults();
        Long total = childDocs.getNumFound();
        Map<String, Map<String, List<String>>> maplist=queryResponse.getHighlighting();
        resul.setPerPage((long)perPage);
        resul.setCounts((long)total);
        for(SolrDocument sd : childDocs){
            resultList.add(getSolrVo(type,sd,orignKeyWords));
        }
        resul.setData(resultList);
        return resul;
    }

    public <T> T getSolrVo(Class<T> type,SolrDocument doc,String keyWords) throws Exception{
        Object id=doc.get("id");
        T ret  = (T)Class.forName(type.getName()).newInstance();
        Field[] fileds = ret.getClass().getDeclaredFields();
        String[] strArray = keyWords.split(" ");
        for(Field field:fileds){
            String value = doc.getFieldValue(field.getName())==null?"":doc.getFieldValue(field.getName()).toString();
            field.setAccessible(true); // 设置些属性是可以访问的
            if(keyWords!=null && !"".equals(keyWords) && !"id".equals(field.getName())){
                field.set(ret,getHilightValue(value,strArray));
            }else{
                field.set(ret,value);
            }
        }
        return ret;
    }

    public String getHilightValue(String value,String[] strArray){
        if(strArray==null || strArray.length<1){
            return value;
        }
        for(String key:strArray){
            value = value.replace(key,"<font color='red'>"+key+"</font>");
        }
        return value;
    }
    public <T> T getDto(Class<T> type,SolrDocument doc,Map<String, Map<String, List<String>>> hlMap) throws Exception{
        Object id=doc.get("id");
        Map<String, List<String>>  fieldMap = (hlMap==null?null:hlMap.get(id));
        T ret  = (T)Class.forName(type.getName()).newInstance();
        Field[] fileds = ret.getClass().getDeclaredFields();
        for(Field field:fileds){
            String value = doc.getFieldValue(field.getName())==null?"":doc.getFieldValue(field.getName()).toString();
            field.setAccessible(true); // 设置些属性是可以访问的
            if(fieldMap!=null && fieldMap.get(field.getName())!=null){
                List<String> stringlist=fieldMap.get(field.getName());
                field.set(ret,getHlString(stringlist));
            }else{
                field.set(ret,value);
            }
        }
        return ret;
    }

    public String getHlString(List<String> hlList){
        StringBuffer stringBuffer = new StringBuffer("");
        if(hlList!=null && !hlList.isEmpty()){
            for(String hl:hlList){
                stringBuffer.append(hl);
            }
        }
        return stringBuffer.toString();
    }
    /**
     * 根据id删除索引操作
     * @param id
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteById(String id) throws SolrServerException, IOException{
        httpSolrServer.deleteById(id);
        httpSolrServer.commit();
    }

    /**
     * 根据条件删除索引操作 "id:c001")
     * @param param
     * @throws Exception
     */
    public void deleteDocumentByQuery(String param) throws Exception {
        //根据查询条件删除文档
        httpSolrServer.deleteByQuery(param);
        //提交修改
        httpSolrServer.commit();
    }

}
