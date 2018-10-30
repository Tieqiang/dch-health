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
    public void whenCreateUserCustomTableWithOneTable() throws Exception{

        CreateTableVO createTableVO = new CreateTableVO();
        List<TableConfig> tableConfigs = tableFacade.getTableConfig("5e5d857c628fe2940162a2e82bf000a9","");
        System.out.println(tableConfigs.size());
        TableConfig tableConfig1 = tableConfigs.get(0);

        TableColVO tableColVO = tableFacade.getTableColVO(tableConfig1.getId(),20,1);
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


    @Test
    public void whenCreateUserCustomWithJson(){
        String json = "{\n" +
                "\t\"tableConfig\": {\n" +
                "\t\t\"formId\": \"5e5d857c628fe2940162a2e82bf000a9\",\n" +
                "\t\t\"tableDesc\": \"666\",\n" +
                "\t\t\"tableName\": \"666\"\n" +
                "\t},\n" +
                "\t\"userCustomTableVOs\": [{\n" +
                "\t\t\"tableColConfigs\": [\"8a80cb816614b079016614b1129801aa\", \"8a80cb816614b079016614b1129901b4\"],\n" +
                "\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801a8\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\"colDescription\": \"公益性卫生行业科研专项上报系统主键\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801a9\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\"colCode\": \"dch_1536300829591\",\n" +
                "\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801aa\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人姓名\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154958799\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人姓名\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801ab\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人职称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154967423\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人职称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801ac\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人电子邮件\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154977718\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人电子邮件\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801ad\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人学历\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154986926\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人学历\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801ae\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人联系电话\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154997544\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人联系电话\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801af\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目联系人姓名\",\n" +
                "\t\t\t\"colCode\": \"dch_1523155009748\",\n" +
                "\t\t\t\"colDescription\": \"项目联系人姓名\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b0\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目联系人职称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523155019901\",\n" +
                "\t\t\t\"colDescription\": \"项目联系人职称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b1\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目联系人电子邮件\",\n" +
                "\t\t\t\"colCode\": \"dch_1523155027501\",\n" +
                "\t\t\t\"colDescription\": \"项目联系人电子邮件\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b2\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目联系人联系电话\",\n" +
                "\t\t\t\"colCode\": \"dch_1523155038608\",\n" +
                "\t\t\t\"colDescription\": \"项目联系人联系电话\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b3\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目联系人手机\",\n" +
                "\t\t\t\"colCode\": \"dch_1523155051273\",\n" +
                "\t\t\t\"colDescription\": \"项目联系人手机\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b4\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"课题类别\",\n" +
                "\t\t\t\"colCode\": \"dch_1523170063538\",\n" +
                "\t\t\t\"colDescription\": \"课题类别\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b5\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"研究所属学科\",\n" +
                "\t\t\t\"colCode\": \"dch_1523260226473\",\n" +
                "\t\t\t\"colDescription\": \"研究所属学科\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b6\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目名称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154179240\",\n" +
                "\t\t\t\"colDescription\": \"项目名称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b7\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目编号\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154196755\",\n" +
                "\t\t\t\"colDescription\": \"项目编号\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b8\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"项目负责人\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154214456\",\n" +
                "\t\t\t\"colDescription\": \"项目负责人\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901b9\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"依托单位名称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154230711\",\n" +
                "\t\t\t\"colDescription\": \"依托单位名称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901ba\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154246779\",\n" +
                "\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129901bb\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210011455,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\"colCode\": \"dch_1523154257015\",\n" +
                "\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}],\n" +
                "\t\t\"tableConfig\": {\n" +
                "\t\t\t\"formId\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801a7\"\n" +
                "\t\t}\n" +
                "\t}, {\n" +
                "\t\t\"tableColConfigs\": [\"8a80cb816614b079016614b1129801af\", \"8a80cb816614b079016614b1129901bb\"],\n" +
                "\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0001\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\"colDescription\": \"取得知识产权列表主键\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0002\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"外键\",\n" +
                "\t\t\t\"colCode\": \"master_id\",\n" +
                "\t\t\t\"colDescription\": \"取得知识产权列表外键\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0003\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"申请号\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167996239\",\n" +
                "\t\t\t\"colDescription\": \"申请号\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4410004\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"授权号\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167999517\",\n" +
                "\t\t\t\"colDescription\": \"授权号\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4410005\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"登记号\",\n" +
                "\t\t\t\"colCode\": \"dch_1523168003725\",\n" +
                "\t\t\t\"colDescription\": \"登记号\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4410006\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"名称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523168064532\",\n" +
                "\t\t\t\"colDescription\": \"名称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4410007\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"取得知识产权类别\",\n" +
                "\t\t\t\"colCode\": \"dch_1523244016373\",\n" +
                "\t\t\t\"colDescription\": \"取得知识产权类别\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4420008\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"第一完成人\",\n" +
                "\t\t\t\"colCode\": \"dch_1523168084548\",\n" +
                "\t\t\t\"colDescription\": \"第一完成人\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4430009\",\n" +
                "\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\"modifyDate\": 1538210038438,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"申请人\",\n" +
                "\t\t\t\"colCode\": \"dch_1523168091405\",\n" +
                "\t\t\t\"colDescription\": \"申请人\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}],\n" +
                "\t\t\"tableConfig\": {\n" +
                "\t\t\t\"formId\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4310000\"\n" +
                "\t\t}\n" +
                "\t}, {\n" +
                "\t\t\"tableColConfigs\": [\"8a80cb816614b079016614b1114b0192\", \"8a80cb816614b079016614b1114c0193\"],\n" +
                "\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114b0190\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\"colDescription\": \"成果转化、应用列表主键\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114b0191\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"外键\",\n" +
                "\t\t\t\"colCode\": \"master_id\",\n" +
                "\t\t\t\"colDescription\": \"成果转化、应用列表外键\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114b0192\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"转化应用成果名称\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167222327\",\n" +
                "\t\t\t\"colDescription\": \"转化应用成果名称\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0193\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"知识产权编号\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167015014\",\n" +
                "\t\t\t\"colDescription\": \"知识产权编号\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0194\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"知识产权所属单位\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167032912\",\n" +
                "\t\t\t\"colDescription\": \"知识产权所属单位\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0195\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"知识产权转化形式\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167048894\",\n" +
                "\t\t\t\"colDescription\": \"知识产权转化形式\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0196\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"转化收益\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167106121\",\n" +
                "\t\t\t\"colDescription\": \"转化收益\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0197\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"完成人名单\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167152781\",\n" +
                "\t\t\t\"colDescription\": \"完成人名单\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114c0198\",\n" +
                "\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\"modifyDate\": 1538210026434,\n" +
                "\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\"status\": null,\n" +
                "\t\t\t\"colName\": \"转化途径\",\n" +
                "\t\t\t\"colCode\": \"dch_1523167155456\",\n" +
                "\t\t\t\"colDescription\": \"转化途径\",\n" +
                "\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"dataVersion\": 0\n" +
                "\t\t}],\n" +
                "\t\t\"tableConfig\": {\n" +
                "\t\t\t\"formId\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114b018f\"\n" +
                "\t\t}\n" +
                "\t}],\n" +
                "\t\"operationConditionVOS\": [{\n" +
                "\t\t\"firstTableColConfig\": {\n" +
                "\t\t\t\"colCode\": \"8a80cb816614b079016614b1129801aa\",\n" +
                "\t\t\t\"colName\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\"tableId\": \"\",\n" +
                "\t\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801a8\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\t\"colDescription\": \"公益性卫生行业科研专项上报系统主键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801a9\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1536300829591\",\n" +
                "\t\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801aa\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人姓名\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154958799\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人姓名\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801ab\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人职称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154967423\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人职称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801ac\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人电子邮件\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154977718\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人电子邮件\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801ad\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人学历\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154986926\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人学历\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801ae\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人联系电话\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154997544\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人联系电话\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129801af\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目联系人姓名\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523155009748\",\n" +
                "\t\t\t\t\"colDescription\": \"项目联系人姓名\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b0\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目联系人职称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523155019901\",\n" +
                "\t\t\t\t\"colDescription\": \"项目联系人职称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b1\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目联系人电子邮件\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523155027501\",\n" +
                "\t\t\t\t\"colDescription\": \"项目联系人电子邮件\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b2\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目联系人联系电话\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523155038608\",\n" +
                "\t\t\t\t\"colDescription\": \"项目联系人联系电话\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b3\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目联系人手机\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523155051273\",\n" +
                "\t\t\t\t\"colDescription\": \"项目联系人手机\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b4\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"课题类别\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523170063538\",\n" +
                "\t\t\t\t\"colDescription\": \"课题类别\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b5\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"研究所属学科\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523260226473\",\n" +
                "\t\t\t\t\"colDescription\": \"研究所属学科\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b6\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目名称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154179240\",\n" +
                "\t\t\t\t\"colDescription\": \"项目名称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b7\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目编号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154196755\",\n" +
                "\t\t\t\t\"colDescription\": \"项目编号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b8\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"项目负责人\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154214456\",\n" +
                "\t\t\t\t\"colDescription\": \"项目负责人\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901b9\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"依托单位名称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154230711\",\n" +
                "\t\t\t\t\"colDescription\": \"依托单位名称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901ba\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154246779\",\n" +
                "\t\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1129901bb\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210041282,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"未命名\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523154257015\",\n" +
                "\t\t\t\t\"colDescription\": \"未命名\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"secondTableColConfig\": {\n" +
                "\t\t\t\"colCode\": \"8a80cb816614b079016614b0f43f0002\",\n" +
                "\t\t\t\"colName\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"tableId\": \"\",\n" +
                "\t\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0001\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权列表主键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0002\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"外键\",\n" +
                "\t\t\t\t\"colCode\": \"master_id\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权列表外键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0003\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"申请号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167996239\",\n" +
                "\t\t\t\t\"colDescription\": \"申请号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410004\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"授权号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167999517\",\n" +
                "\t\t\t\t\"colDescription\": \"授权号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410005\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"登记号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168003725\",\n" +
                "\t\t\t\t\"colDescription\": \"登记号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410006\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"名称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168064532\",\n" +
                "\t\t\t\t\"colDescription\": \"名称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410007\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"取得知识产权类别\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523244016373\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权类别\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4420008\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"第一完成人\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168084548\",\n" +
                "\t\t\t\t\"colDescription\": \"第一完成人\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4430009\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210046722,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"申请人\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168091405\",\n" +
                "\t\t\t\t\"colDescription\": \"申请人\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"secondAddCheckBox\": [{\n" +
                "\t\t\t\t\"value\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"label\": \"取得知识产权列表\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"value\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"label\": \"成果转化、应用列表\"\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"inputKind\": \"term\",\n" +
                "\t\t\"inValues\": null,\n" +
                "\t\t\"thanValues\": null,\n" +
                "\t\t\"nextOperation\": \"and\",\n" +
                "\t\t\"operationEnum\": \"GRATER_THEN\"\n" +
                "\t}, {\n" +
                "\t\t\"firstTableColConfig\": {\n" +
                "\t\t\t\"colCode\": \"8a80cb816614b079016614b0f4410005\",\n" +
                "\t\t\t\"colName\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\"tableId\": \"\",\n" +
                "\t\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0001\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权列表主键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0002\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"外键\",\n" +
                "\t\t\t\t\"colCode\": \"master_id\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权列表外键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f43f0003\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"申请号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167996239\",\n" +
                "\t\t\t\t\"colDescription\": \"申请号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410004\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"授权号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167999517\",\n" +
                "\t\t\t\t\"colDescription\": \"授权号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410005\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"登记号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168003725\",\n" +
                "\t\t\t\t\"colDescription\": \"登记号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410006\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"名称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168064532\",\n" +
                "\t\t\t\t\"colDescription\": \"名称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4410007\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"取得知识产权类别\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523244016373\",\n" +
                "\t\t\t\t\"colDescription\": \"取得知识产权类别\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4420008\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"第一完成人\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168084548\",\n" +
                "\t\t\t\t\"colDescription\": \"第一完成人\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b0f4430009\",\n" +
                "\t\t\t\t\"createDate\": 1537945433000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210054905,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"申请人\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523168091405\",\n" +
                "\t\t\t\t\"colDescription\": \"申请人\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b0f4310000\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"secondTableColConfig\": {\n" +
                "\t\t\t\"colCode\": \"8a80cb816614b079016614b1114b0191\",\n" +
                "\t\t\t\"colName\": \"\",\n" +
                "\t\t\t\"id\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\"tableId\": \"\",\n" +
                "\t\t\t\"addCheckBox\": [{\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114b0190\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"主键\",\n" +
                "\t\t\t\t\"colCode\": \"id\",\n" +
                "\t\t\t\t\"colDescription\": \"成果转化、应用列表主键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114b0191\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"外键\",\n" +
                "\t\t\t\t\"colCode\": \"master_id\",\n" +
                "\t\t\t\t\"colDescription\": \"成果转化、应用列表外键\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114b0192\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"转化应用成果名称\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167222327\",\n" +
                "\t\t\t\t\"colDescription\": \"转化应用成果名称\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0193\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"知识产权编号\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167015014\",\n" +
                "\t\t\t\t\"colDescription\": \"知识产权编号\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0194\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"知识产权所属单位\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167032912\",\n" +
                "\t\t\t\t\"colDescription\": \"知识产权所属单位\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0195\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"知识产权转化形式\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167048894\",\n" +
                "\t\t\t\t\"colDescription\": \"知识产权转化形式\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0196\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"转化收益\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167106121\",\n" +
                "\t\t\t\t\"colDescription\": \"转化收益\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0197\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"完成人名单\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167152781\",\n" +
                "\t\t\t\t\"colDescription\": \"完成人名单\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"id\": \"8a80cb816614b079016614b1114c0198\",\n" +
                "\t\t\t\t\"createDate\": 1537945441000,\n" +
                "\t\t\t\t\"modifyDate\": 1538210059925,\n" +
                "\t\t\t\t\"createBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"modifyBy\": \"8a0300ab5d9c585f015da04754560019\",\n" +
                "\t\t\t\t\"status\": null,\n" +
                "\t\t\t\t\"colName\": \"转化途径\",\n" +
                "\t\t\t\t\"colCode\": \"dch_1523167155456\",\n" +
                "\t\t\t\t\"colDescription\": \"转化途径\",\n" +
                "\t\t\t\t\"tableId\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"dataVersion\": 0\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"secondAddCheckBox\": [{\n" +
                "\t\t\t\t\"value\": \"8a80cb816614b079016614b1129801a7\",\n" +
                "\t\t\t\t\"label\": \"公益性卫生行业科研专项上报系统数据\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"value\": \"8a80cb816614b079016614b1114b018f\",\n" +
                "\t\t\t\t\"label\": \"成果转化、应用列表\"\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"inputKind\": \"term\",\n" +
                "\t\t\"inValues\": null,\n" +
                "\t\t\"thanValues\": null,\n" +
                "\t\t\"nextOperation\": \"and\",\n" +
                "\t\t\"operationEnum\": \"GRATER_THEN\"\n" +
                "\t}]\n" +
                "}\n";
        WebResource webResource = resource();
        TableConfig response  =webResource.path("data-analysis/create-custom-table").type(MediaType.APPLICATION_JSON_TYPE).post(TableConfig.class,json);
        Assert.assertNotNull(response);
    }

}
