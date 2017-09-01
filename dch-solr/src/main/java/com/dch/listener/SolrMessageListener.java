package com.dch.listener;

import com.dch.facade.BaseSolrFacade;
import com.dch.vo.DrugCommonVo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by Administrator on 2017/8/25.
 */
public class SolrMessageListener implements MessageListener {

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 监听传递的vo消息对象 并同步solr索引库
     * @param message
     */
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage= (ObjectMessage) message;
            //DrugCommonVo drugCommonVo= (DrugCommonVo) objectMessage.getObject();
            //等待1秒钟
            Thread.sleep(1000);
            //同步索引库
            baseSolrFacade.sendIndexToSolr(objectMessage.getObject());
        } catch (Exception e) {
            e.printStackTrace();
    }

    }
}
