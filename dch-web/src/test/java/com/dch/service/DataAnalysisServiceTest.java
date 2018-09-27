package com.dch.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dch.entity.TableColConfig;
import com.dch.entity.TableConfig;
import com.dch.facade.TableFacade;
import com.dch.test.base.TestUtils;
import com.dch.vo.*;
import com.sun.jersey.api.client.WebResource;
import org.hibernate.metamodel.domain.Entity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class DataAnalysisServiceTest extends BaseTest {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private TableFacade tableFacade;

    @Before
    public void init(){
        tableFacade = (TableFacade) TestUtils.applicationContext.getBean("tableFacade");
        System.out.println(tableFacade);
    }

    /**
     * 测试获取所有的额表模型测试
     *
     */
    @Test
    public void whenGetTableConfigs(){
        WebResource webResource = resource();
        String list = webResource.path("data-analysis/get-tables").queryParam("templateId","5e5d857c628fe2940162a2e82bf000a9").get(String.class);
//        System.out.println(list);
        List tableConfigs = JSONObject.parseObject(list, List.class);
        Assert.assertTrue(tableConfigs.size()>0);
    }

    @Test
    public void whenCreateUserCustomTableWithOneTable(){

        CreateTableVO createTableVO = new CreateTableVO();
        List<TableConfig> tableConfigs = tableFacade.getTableConfig("5e5d857c628fe2940162a2e82bf000a9");
        System.out.println(tableConfigs.size());
        TableConfig tableConfig1 = tableConfigs.get(0);

        TableColVO tableColVO = tableFacade.getTableColVO(tableConfig1.getId());
        List<TableColConfig> tableColConfigs = tableColVO.getTableColConfigs();

        TableConfig tableConfig = new TableConfig();

        tableConfig.setTableName("测试表");
        tableConfig.setTableDesc("第一张测试记录表");


        createTableVO.setTableConfig(tableConfig);

        List<UserCustomTableVO> userCustomTableVOS  = new ArrayList<UserCustomTableVO>();
        UserCustomTableVO vo = new UserCustomTableVO();
        vo.setTableConfig(tableConfig1);
        List<TableColConfig> tableColConfigList = new ArrayList<TableColConfig>();
        tableColConfigList.add(tableColConfigs.get(2));
        tableColConfigList.add(tableColConfigs.get(3));
        vo.setTableColConfigs(tableColConfigList);
        userCustomTableVOS.add(vo);

        createTableVO.setUserCustomTableVOs(userCustomTableVOS);
        List<OperationConditionVO> oprations = new ArrayList<OperationConditionVO>();
        OperationConditionVO conditionVO = new OperationConditionVO();
        conditionVO.setFirstTableColConfig(tableColConfigs.get(2));
        List<Object[]> datas = tableColVO.getDatas();
        List<String> values = new ArrayList<String>();
        int i =0;
        for(Object[] data:datas){
            if(i>10){
                break;
            }
            values.add(data[2].toString());
            i++;
        }
        conditionVO.setInValues(values);
        conditionVO.setOperationEnum(OperationEnum.IN);
        conditionVO.setNextOperation(" and ");
        oprations.add(conditionVO);
        createTableVO.setOperationConditionVOS(oprations);

        WebResource webResource = this.client().resource("http://localhost:9998/api/data-analysis/create-custom-table");
        String entity = JSONObject.toJSONString(createTableVO, SerializerFeature.DisableCircularReferenceDetect);
        logger.info("post json :");
        logger.info(entity);

        TableConfig response  =webResource.type(MediaType.APPLICATION_JSON_TYPE).post(TableConfig.class,entity);
//        webResource.type(MediaType.APPLICATION_JSON).post(entity);

        Assert.assertNotNull(response);

    }

}
