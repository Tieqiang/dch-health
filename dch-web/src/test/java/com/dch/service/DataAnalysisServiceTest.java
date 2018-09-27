package com.dch.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.WebResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class DataAnalysisServiceTest extends BaseTest {





    @Test
    public void whenGetTableConfigs(){
        WebResource webResource = resource();
        String list = webResource.path("data-analysis/get-tables").queryParam("templateId","5e5d857c628fe2940162a2e82bf000a9").get(String.class);
//        System.out.println(list);
        List tableConfigs = JSONObject.parseObject(list, List.class);
        Assert.assertTrue(tableConfigs.size()>0);
    }

}
