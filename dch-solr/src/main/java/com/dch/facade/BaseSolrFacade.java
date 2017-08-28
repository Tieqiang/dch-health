package com.dch.facade;

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
    public List<DrugCommonVo> searchDrugCommonVos(String content,int perPage,int currentPage) throws SolrServerException {
        List<DrugCommonVo> drugCommonVos = new ArrayList<>();
        SolrQuery query = new SolrQuery();// 查询
        query.setQuery("content:"+content);
        //query.setRows(20);
        if(currentPage>0){
            query.setStart((currentPage-1)*perPage);
            query.setRows(perPage);
        }
        SolrDocumentList docs = httpSolrServer.query(query).getResults();
        for (SolrDocument sd : docs) {
            drugCommonVos.add(produceByDoc(sd));
        }
        return drugCommonVos;
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
}
