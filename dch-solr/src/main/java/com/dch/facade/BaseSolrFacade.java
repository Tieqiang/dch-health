package com.dch.facade;

import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.DrugCommonVo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
     * 根据搜索内容查询信息
     * @param content 查询内容
     * @param perPage 每页条数
     * @param currentPage 当前页码
     * @return
     * @throws Exception
     */
    public Page<DrugCommonVo> searchDrugCommonVos(String content,int perPage,int currentPage) throws SolrServerException {
        Page<DrugCommonVo> drugCommonVoPage = new Page<>() ;
        List<DrugCommonVo> drugCommonVos = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        query.setQuery("content:"+content);
        //query.setRows(20);
        if(perPage>0){
            query.setStart((currentPage-1)*perPage);
            query.setRows(perPage);
        }
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        drugCommonVoPage.setPerPage((long)perPage);
        drugCommonVoPage.setCounts((long)docs.size());
        for (SolrDocument sd : docs) {
            drugCommonVos.add(produceByDoc(sd));
        }
        drugCommonVoPage.setData(drugCommonVos);
        return drugCommonVoPage;
    }

    /**
     * 添加信息到索引库
     * @param drugCommonVo
     * @throws SolrServerException
     * @throws IOException
     */
    public void sendIndexToSolr(DrugCommonVo drugCommonVo) throws SolrServerException, IOException {
        SolrInputDocument document  = new SolrInputDocument();
        //3.添加信息
        document.addField("id", drugCommonVo.getId());
        document.addField("parentId", drugCommonVo.getParentId());
        document.addField("title", drugCommonVo.getTitle());
        document.addField("desc", drugCommonVo.getDesc());
        document.addField("label", drugCommonVo.getLabel());
        document.addField("category", drugCommonVo.getCategory());
        //把信息添加到索引库中
        httpSolrServer.add(document);
        //提交事务
        httpSolrServer.commit();
    }

    /**
     * 根据查询的solr文档信息封装成vo对象
     * @param sd
     * @return
     */
    public static DrugCommonVo produceByDoc(SolrDocument sd){
        DrugCommonVo drugCommonVo = new DrugCommonVo();
        drugCommonVo.setId(sd.getFieldValue("id")+"");
        drugCommonVo.setParentId(sd.getFieldValue("parentId")+"");
        drugCommonVo.setTitle(sd.getFieldValue("title")+"");
        drugCommonVo.setDesc(sd.getFieldValue("desc")+"");
        drugCommonVo.setLabel(sd.getFieldValue("label")+"");
        drugCommonVo.setCategory(sd.getFieldValue("category")+"");
        return drugCommonVo;
    }

    /**
     * 向mq添加消息vo
     * @param drugCommonVo
     */
    public void addDrugCommonVo(final DrugCommonVo drugCommonVo){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(drugCommonVo);
                return objectMessage;
            }
        });
    }

    /**
     * 根据分类id查询分类下的子分类并匹配关键字
     * @param id
     * @param content
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    public Page<DrugCommonVo> searchChildDrugCommonVos(String id, String content, int perPage, int currentPage) throws Exception{
        Page<DrugCommonVo> drugCommonVoPage = new Page<>();
        List<DrugCommonVo> resultList = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        query.setQuery("id:"+id);
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        if(docs==null || docs.isEmpty()){
            throw new Exception("分类信息不存在");
        }
        String para = "";
        DrugCommonVo drugCommonVo = produceByDoc(docs.get(0));
        if(StringUtils.isEmptyParam(drugCommonVo.getParentId())||"0".equals(drugCommonVo.getParentId())){//为空说明是一级分类
            query.clear();
            query.setQuery("parentId:"+id);
            SolrDocumentList childDocs = httpSolrServer.query(query).getResults();
            List<DrugCommonVo> drugCommonVos = new ArrayList<>();
            for(SolrDocument sd : childDocs){
                drugCommonVos.add(produceByDoc(sd));
            }
            query.clear();
            if(!drugCommonVos.isEmpty()){
                para = para+"(";
                for(int i=0;i<drugCommonVos.size();i++){
                    if(i!=drugCommonVos.size()-1){
                        para = para+"parentId:"+drugCommonVos.get(i).getId()+" OR ";
                    }else{
                        para = para+"parentId:"+drugCommonVos.get(i).getId()+")";
                    }
                }
            }
            if("".equals(para)){
                para = para+"content:"+content;
            }else{
                para = para+" AND content:"+content;
            }
        }else{//二级分类
            query.clear();
            para = " parentId:"+drugCommonVo.getId()+" AND content:"+content;
        }
        query.setQuery(para);
        if(perPage>0){
            query.setStart((currentPage-1)*perPage);
            query.setRows(perPage);
        }
        SolrDocumentList docList = httpSolrServer.query(query).getResults();
        drugCommonVoPage.setPerPage((long)perPage);
        drugCommonVoPage.setCounts((long)docList.size());
        for (SolrDocument sd : docList) {
            resultList.add(produceByDoc(sd));
        }
        drugCommonVoPage.setData(resultList);
        return drugCommonVoPage;
    }

    /**
     * 根据id查询分类信息 如果id为空查询一级分类不为空则查询其下的子分类
     * @param id
     * @return
     */
    public List<DrugCommonVo> searchDrugCommonVosById(String id) throws Exception{
        List<DrugCommonVo> resultList = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        if(StringUtils.isEmptyParam(id)){
            query.setQuery("parentId:"+"0");
        }else{
            query.setQuery("parentId:"+id);
        }
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        for (SolrDocument sd : docs) {
            resultList.add(produceByDoc(sd));
        }
        return resultList;
    }
}
