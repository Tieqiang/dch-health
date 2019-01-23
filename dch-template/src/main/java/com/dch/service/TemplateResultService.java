package com.dch.service;


import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.entity.User;
import com.dch.facade.TemplateResultFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.*;
import com.dch.vo.QueryCondition;
import com.dch.vo.TemplateMasterVo;
import com.dch.vo.TemplateResultMasterVo;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Path("template/template-result")
@Produces("application/json")
@Controller
public class TemplateResultService {
    public static final String tempCollectionName = "templateResult";
    public static final String tempFillName = "templateFilling";
    public static final String templateResultList = "templateResultList";

    private final String specialChar = "@";
    private final String inputSpeChar = "$";
    private static Map<String,Map> initMap = new HashMap<>();
    //public static final Logger logger = LogManager.getLogger(TemplateResultService.class);
//    static {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
//        rootLogger.setLevel(Level.OFF);
//    }

    @Autowired
    private TemplateResultFacade templateResultFacade;

    @Autowired
    private MongoTemplate mongoTemplate ;


    @POST
    @Path("save-all-template-result")
    @Transactional
    @ResourceFilters(com.dch.security.LoginFilter.class)
    public Response saveAllTemplateResult(@QueryParam("templateId") String templateId,@QueryParam("masterId") String masterId,@QueryParam("type")String type) throws Exception {
        List<String> list = new ArrayList<>();
        String hql = "select templateResult from TemplateResult where status<>'-1' and templateId = '"+templateId+"' " +
                "and masterId = '"+masterId+"'";
        List<String> resultList = templateResultFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(resultList!=null && !resultList.isEmpty()){
            StringBuffer stringBuffer = new StringBuffer("{").append("\"templateId\":").append("\"").append(templateId).append("\"")
                    .append(",").append("\"masterId\":").append("\"").append(masterId).append("\"");
            int i=0;
            for(String result:resultList){
                if(!"null".equals(result) && !StringUtils.isEmptyParam(result) && !"{}".equals(result)){
                    result = getNewResult(result);
                    if(i==0 && !StringUtils.isEmptyParam(result)){
                        result = result.substring(1,result.length()-1);
                    }else if(i>0 && !StringUtils.isEmptyParam(result)){
                        result = result.substring(1,result.length()-1);
                    }
                    stringBuffer.append(",").append(result);
                    i++;
                }
            }
            stringBuffer.append("}");
            String toMongoResult = stringBuffer.toString();
            TemplateResultMaster templateResultMaster = templateResultFacade.get(TemplateResultMaster.class,masterId);
            toMongoResult = toMongoResult.replace(inputSpeChar,specialChar);
            try{
                removeTemplateResultByMasterId(masterId,type);
                if("fill".equals(type)){
                    mongoTemplate.insert(toMongoResult,tempFillName);
                }else{
                    mongoTemplate.insert(toMongoResult,tempCollectionName);
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("表单数据保存异常");
            }
            try{
                templateResultMaster.setStatus("2");//表单状态设置为已保存
                templateResultFacade.merge(templateResultMaster);
            }catch (Exception e){
                e.printStackTrace();
                removeTemplateResultByMasterId(masterId,type);//保存异常 清除mongo中已保存的数据
                throw new Exception("表单信息保存异常");
            }
        }else{
            throw new Exception("录入表单数据为空，请填写表单");
        }
        list.add(templateId);
        return Response.status(Response.Status.OK).entity(list).build();
    }

    public String getNewResult(String result){
        try {
            if(!"null".equals(result)){
                Map map = (Map) JSONUtil.JSONToObj(result,Map.class);
                for(Object obj:map.keySet()){
                    String value =  map.get(obj)==null?"":map.get(obj).toString();
                    try {
                        if(value.startsWith("[{") && value.endsWith("}]") && value.contains(inputSpeChar)){
                            List<Map> mapList = (List<Map>)map.get(obj);
                            for(Map inMap:mapList){
                                for(Object key:inMap.keySet()){
                                    String inValue = inMap.get(key)==null?"":inMap.get(key).toString();
                                    boolean isNum = isNumeric(inValue);
                                    if(isNum){
                                        inMap.put(key,new BigDecimal(inValue));
                                    }
                                }
                            }
                        }else if(value.startsWith("[") && value.endsWith("]") && !value.contains("{")){
                            List list = (List)map.get(obj);
                            List newList = new ArrayList();
                            if(list!=null && !list.isEmpty()){
                                for(int i=0;i<list.size();i++){
                                    String inValue = list.get(i)==null?"":list.get(i).toString();
                                    boolean isNum = isNumeric(inValue);
                                    if(isNum){
                                        newList.add(new BigDecimal(inValue));
                                    }else{
                                        newList.add(inValue);
                                    }
                                }
                            }
                            map.put(obj,newList);
                        }else if(value.startsWith("{") && value.endsWith("}") && !value.contains("[")){
                            Map inMap = (Map)map.get(obj);
                            for(Object key:inMap.keySet()){
                                String invalue = inMap.get(key)==null?"": inMap.get(key).toString();
                                boolean isNum = isNumeric(invalue);
                                if(isNum){
                                    inMap.put(key,new BigDecimal(invalue));
                                }
                            }
                        }else if(value.startsWith("{") && value.endsWith("}") && value.contains("[")){//Map里面 嵌套集合

                        }else{
                            if(!value.startsWith("0")||"0".equals(value)){//不是以0开头的数字
                                boolean isNum = isNumeric(value);
                                if(isNum){
                                    map.put(obj,new BigDecimal(value));
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                result = JSONUtil.objectToJsonString(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 判断是否为数字
     * @param input
     * @return
     */
    public boolean isNumeric(String input){
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(input);
        Boolean isMach = isNum.matches();
        if(isMach && input.length()>18){
            isMach = false;
        }
        return isMach;
    }
    /**
     * 根据templaeId删除mongodb数据库中已存在的表单信息
     * @param masterId
     */
    public void removeTemplateResultByMasterId(String masterId,String type){
        Query query = new Query();
        // query.addCriteria(where("age").gt(22));
        Criteria criteria = where("masterId").is(masterId);
        // 删除年龄大于22岁的用户
        query.addCriteria(criteria);
        if("fill".equals(type)){
            mongoTemplate.remove(query,tempFillName);
        }else{
            mongoTemplate.remove(query,tempCollectionName);
        }
    }
    /**
     * 保存表单数据
     * @param templateResult
     * @return
     */
    @POST
    @Path("merge-template-result")
    @Transactional
    @ResourceFilters(com.dch.security.LoginFilter.class)
    public Response mergeTemplateResult(TemplateResult templateResult) throws Exception {
        TemplateResult templateResultMerge =null;
        try{
            //如果是保存的第一个页面，则自动生成
            String masterId = templateResult.getMasterId();
            String userId = UserUtils.getCurrentUser().getId();
            if(StringUtils.isEmptyParam(templateResult.getId())){
                if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                    String templateId = templateResult.getTemplateId();
                    TemplateMaster templateMaster = templateResultFacade.get(TemplateMaster.class, templateId);
                    Integer limit = templateMaster.getFillLimit();
                    if(limit>0){
                        String countHql = "select masterId from TemplateResult where status<>'-1' and templateId = '"+templateId+"' and createBy = '"+userId+"' group by masterId";
                        List<String> ctList = templateResultFacade.createQuery(String.class,countHql,new ArrayList<Object>()).getResultList();
                        if(ctList!=null && ctList.size()==limit){
                            throw new Exception("表单录入数超过表单填报限制次数，不允许录入数据");
                        }
                    }
                    TemplateResultMaster master = new TemplateResultMaster();
                    master.setTemplateId(templateId);
                    master.setTemplateName(templateMaster.getTemplateName());
                    master.setStatus("1");
                    TemplateResultMaster merge = templateResultFacade.merge(master);
                    templateResult.setMasterId(merge.getId());
                    masterId = merge.getId();
                }
            }else{
                System.out.println(templateResult.getMasterId());
                if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                    throw new Exception("masterId不能为空");
                }
            }
            templateResultMerge = templateResultFacade.merge(templateResult);
            String jsonTemplateResult = JSONUtil.objectToJsonString(templateResult);
            mongoTemplate.insert(jsonTemplateResult,templateResultList);
            String totalHql = "select id from TemplatePage where status<>'-1' and templatePageContent is not null and templateId = '"+templateResultMerge.getTemplateId()+"'";
            List<String> IdList = templateResultFacade.createQuery(String.class,totalHql,new ArrayList<Object>()).getResultList();
            Long total = Long.valueOf(IdList.size()+"");
            for(String id:IdList){
                Boolean isHaveEmptyChildren = judgeIsHaveAllEmptyChildren(id);
                if(isHaveEmptyChildren){
                    total = total-1;
                }
            }

            String doneHql = "select count(distinct pageId) from TemplateResult where status<>'-1' and templateResult is not null and masterId = '"+masterId+"'";
            Long doneNum = templateResultFacade.createQuery(Long.class,doneHql,new ArrayList<Object>()).getSingleResult();
            String completeRate = "0.0";
            if(total>0){
                String doneNumStr = doneNum+"";
                String totalStr = total+"";
                DecimalFormat df = new DecimalFormat("#0.00");
                completeRate =df.format(Double.valueOf(doneNumStr)/Double.valueOf(totalStr));
                TemplateResultMaster templateResultMaster = templateResultFacade.get(TemplateResultMaster.class,masterId);
                templateResultMaster.setCompleteRate(Double.valueOf(completeRate));
                templateResultFacade.merge(templateResultMaster);
            }
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(templateResultMerge).build();
    }

    public Boolean judgeIsHaveAllEmptyChildren(String id){
        Boolean isAllEmpty = false;
        String hql = "select count(*) from TemplatePage where parentId = '"+id+"' and status<>'-1' ";
        Long number = templateResultFacade.createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
        if(number>0){
            String hqlIn = "select id from TemplatePage where parentId = '"+id+"' and templatePageContent is not null and status<>'-1' ";
            List<String> IdList = templateResultFacade.createQuery(String.class,hqlIn,new ArrayList<Object>()).getResultList();
            if(IdList==null || IdList.isEmpty()){
                isAllEmpty = true;
            }else{
                for(String tid:IdList){
                    isAllEmpty = judgeIsHaveAllEmptyChildren(tid);
                    if(!isAllEmpty){
                        return isAllEmpty;
                    }
                }
            }
        }
        return isAllEmpty;
    }
    /**
     * 获取表单结果
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-mastervos")
    public Page<TemplateMasterVo> getTemplateMasterVos(@QueryParam("projectId") String projectId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateMasterVos(projectId,perPage,currentPage);

    }

    /**
     * 获取某一具体表单结果
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-template-results")
    public Page<TemplateResultMaster> getTemplateResults(@QueryParam("templateId") String templateId,@QueryParam("masterId")String masterId, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage){
        return templateResultFacade.getTemplateResults(templateId,masterId,perPage,currentPage);
    }


    /**
     * 获取某个人填写的表单项目
     * @param templateId 模板的ID
     * @param perPage    单页显示数量
     * @param userId     用户的ID，不传则表示获取这个表单的所有录入的数据
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-template-result-masters-bak")
    public Page<TemplateResultMaster> getTemplateResultMasters(@QueryParam("templateId")String templateId, @QueryParam("perPage")int perPage,
                                                                 @QueryParam("userId")String userId, @QueryParam("currentPage")int currentPage,
                                                                 @QueryParam("status")String status){
        return  templateResultFacade.getTemplateResultMasters(templateId,perPage,currentPage,userId,status);
    }

    /**
     * 获取某个人填写的表单项目
     * @param templateId 模板的ID
     * @param perPage    单页显示数量
     * @param userId     用户的ID，不传则表示获取这个表单的所有录入的数据
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-template-result-masters")
    public Page<TemplateResultMasterVo> getTemplateResultMastersNew(@QueryParam("templateId")String templateId, @QueryParam("perPage")int perPage,
                                                                    @QueryParam("userId")String userId, @QueryParam("currentPage")int currentPage,
                                                                    @QueryParam("status")String status,@QueryParam("field")String field,
                                                                    @QueryParam("fieldValue")String fieldValue){
        return  templateResultFacade.getTemplateResultMastersNew(templateId,perPage,currentPage,userId,status,field,fieldValue,mongoTemplate);
    }
    /**
     * 驳回用户填写表单数据，状态重置改为1
     * @param masterId
     * @param status
     * @return
     */
    @POST
    @Path("reset-template-result-master")
    @Transactional
    public Response resetTemplateResultMaster(@QueryParam("masterId")String masterId,@QueryParam("status")String status){
        TemplateResultMaster templateResultMaster = templateResultFacade.get(TemplateResultMaster.class,masterId);
        if(!StringUtils.isEmptyParam(status)){
            templateResultMaster.setStatus(status);
        }
        TemplateResultMaster merge = templateResultFacade.merge(templateResultMaster);
        return Response.status(Response.Status.OK).entity(merge).build();
    }


    @GET
    @Path("init-user-page")
    @Transactional
    public List<String> initUserPageResult(){
        List<String> list = new ArrayList<>();
        try {
            List<TemplateResult> templateResults = new ArrayList<>();

            String hql = "from User where status = '3'";
            List<User> userList = templateResultFacade.createQuery(User.class,hql,new ArrayList<Object>()).getResultList();
            if(userList!=null && !userList.isEmpty()){
                TemplateMaster templateMaster = getTemplateMasterByName("公益性卫生行业科研专项上报系统");
                String pageHql = "select id from TemplatePage where status<>'-1' and templateId = '"+templateMaster.getId()+"' and templatePageName='封面'";
                List<String> pageNameList = templateResultFacade.createQuery(String.class,pageHql,new ArrayList<Object>()).getResultList();
                String pageId = pageNameList.isEmpty()?"":pageNameList.get(0);
                if(templateMaster!=null){
                    for(User user:userList){
                        TemplateResultMaster templateResultMaster = new TemplateResultMaster();
                        templateResultMaster.setStatus("1");
                        templateResultMaster.setCompleteRate(0.14);
                        templateResultMaster.setTemplateId(templateMaster.getId());
                        templateResultMaster.setTemplateName(templateMaster.getTemplateName());
                        templateResultMaster.setCreateBy(user.getId());
                        templateResultMaster.setModifyBy(user.getId());
                        String initTempResult = getInitTempResult(user.getLoginName());
                        //templateResultMaster.setTemplateResult(initTempResult);
                        TemplateResultMaster merge = templateResultFacade.merge(templateResultMaster);
                        TemplateResult templateResult = new TemplateResult();
                        templateResult.setMasterId(merge.getId());
                        templateResult.setPageId(pageId);
                        templateResult.setTemplateId(templateMaster.getId());
                        templateResult.setStatus("1");
                        templateResult.setCreateBy(user.getId());
                        templateResult.setModifyBy(user.getId());
                        templateResult.setTemplateResult(initTempResult);
                        templateResults.add(templateResult);
                    }
                }
                templateResultFacade.batchInsert(templateResults);
            }
            list.add("success");
        }catch (Exception e){
            list.add("failed");
            e.printStackTrace();
        }
        return list;
    }

    public TemplateMaster getTemplateMasterByName(String name){
        String hql = " from TemplateMaster where templateName = '"+name+"' and status<>'-1'";
        List<TemplateMaster> templateMasters = templateResultFacade.createQuery(TemplateMaster.class,hql,new ArrayList<Object>()).getResultList();
        return templateMasters.isEmpty()?null:templateMasters.get(0);
    }

    public String getInitTempResult(String loginName) throws Exception{
        String proNumber = loginName.substring(0,loginName.indexOf("_"));
        if(initMap.isEmpty()){
            Properties properties = new Properties();
            InputStream inputStream ;
            inputStream=this.getClass().getClassLoader().getResourceAsStream("pan.properties");
            properties.load(inputStream);
            String excelDir = properties.getProperty("excelDir");
            List<Map> list = ReadExcelToDb.readDirExcel(excelDir);
            for(Map map:list){
                String projectNum = map.get("col_0")==null?"":map.get("col_0").toString();
                initMap.put(projectNum,map);
            }
        }
        Map resultMap = initMap.get(proNumber);
        Map reMap = new HashMap();
        reMap.put("dch_1523154179240",resultMap.get("col_1"));
        reMap.put("dch_1523154196755",resultMap.get("col_0"));
        reMap.put("dch_1523154214456",resultMap.get("col_3"));
        reMap.put("dch_1523154230711",resultMap.get("col_2"));
        reMap.put("dch_1523154246779","");
        reMap.put("dch_1523154257015","2018-04-09");
        return JSONUtil.objectToJsonString(reMap);
    }

    /**
     * 删除表单填报数据
     * @param id
     * @param userId
     * @param type
     * @return
     * @throws Exception
     */
    @POST
    @Path("delete-template-result-master")
    @Transactional
    public Response deleteTemplateResultMaster(@QueryParam("id")String id,@QueryParam("userId")String userId,@QueryParam("type")String type) throws Exception{
        return  templateResultFacade.deleteTemplateResultMaster(id,userId,type,mongoTemplate);
    }

    /**
     * 获取某个人填写的表单项目
     * @param queryCondition 传入的参数条件
     * @return
     */
    @GET
    @Path("get-template-result-mastervo-by-param")
    public Page<TemplateResultMasterVo> getTemplateResultMasterVoByParam(QueryCondition queryCondition){
        Map<String,Object> paramMap = queryCondition.getParamMap();
        return  templateResultFacade.getTemplateResultMasterVoByParam(paramMap,mongoTemplate);
    }

    /**
     * 根据用户id及模板id查询表单填报信息
     * @param userId
     * @param templateId
     * @return
     */
    @GET
    @Path("get-user-fill-info")
    public Response getUserFillInfoByParam(@QueryParam("userId")String userId,@QueryParam("templateId")String templateId,
                                            @QueryParam("masterId")String masterId){
        return  templateResultFacade.getUserFillInfoByParam(userId,templateId,masterId);
    }
//    @GET
//    @Path("init-template-result-list")
//    public List<String> initTemplateResultList(){
//        List<String> list = new ArrayList<>();
//        String hql = " from TemplateResult where status<>'-1'";
//        List<TemplateResult> templateResults= templateResultFacade.createQuery(TemplateResult.class,hql,new ArrayList<Object>()).getResultList();
//        for (TemplateResult result:templateResults){
//            SolrVo solrVo=new SolrVo();
//            solrVo.setId(result.getId());
//            solrVo.setTitle(result.getMasterId());
//            solrVo.setDesc(result.getTemplateResult());
//            solrVo.setCategoryCode("templateResult");
//            solrVo.setLabel(result.getTemplateId());
//            solrVo.setCategory("");
//            solrVo.setFirstPy("");
//            baseSolrFacade.addObjectMessageToMq(solrVo);
//        }
//        list.add("成功");
//        return list;
//    }
//
//    @GET
//    @Path("get-my")
//    public List<SolrVo> getTemplateResultListByIds() throws Exception{
//        String param = "categoryCode:templateResult" ;
//        param += " AND title:('8aa183c362e6d0520163164cdf820049','8aa183c362a94b5b0162a94c00180082','8aa183c363b1d7b80163f1965b820054'," +
//                "'8aa183c362a94b5b0162a94c0028008c','8aa183c362e6d0520163194fac9e004e' , '8aa183c362a94b5b0162a94c0028008b' , '8aa183c362a94b5b0162a94c0028008a' ," +
//                " , '8aa183c362e6d0520162fa96b9a30012' , '8aa183c362e6d052016300923b500026' , '8aa183c362a94b5b0162a94c00280085')";
//        List<SolrVo> list = baseSolrFacade.getSolrObjectByParam(param,SolrVo.class);
//        return list;
//    }
}
