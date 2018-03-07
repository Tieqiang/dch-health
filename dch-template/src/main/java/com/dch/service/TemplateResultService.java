package com.dch.service;

import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.facade.TemplateResultFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.JSONUtil;
import com.dch.util.StringUtils;
import com.dch.vo.TemplateMasterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Path("template/template-result")
@Produces("application/json")
@Controller
public class TemplateResultService {
    private static final String tempCollectionName = "templateResult";

    @Autowired
    private TemplateResultFacade templateResultFacade;

    @Autowired
    private MongoTemplate mongoTemplate ;

    @POST
    @Path("save-all-template-result")
    @Transactional
    public Response saveAllTemplateResult(@QueryParam("templateId") String templateId,@QueryParam("masterId") String masterId) throws Exception {
        List<String> list = new ArrayList<>();
        String hql = "select templateResult from TemplateResult where status<>'-1' and templateId = '"+templateId+"' " +
                "and masterId = '"+masterId+"'";
        List<String> resultList = templateResultFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(resultList!=null && !resultList.isEmpty()){
            StringBuffer stringBuffer = new StringBuffer("{").append("\"templateId\":").append("\"").append(templateId).append("\"")
                    .append(",").append("\"masterId\":").append("\"").append(masterId).append("\"");
            int i=0;
            for(String result:resultList){
                if(!"null".equals(result)){
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
            try{
                removeTemplateResultByMasterId(masterId);
                mongoTemplate.insert(toMongoResult,tempCollectionName);
            }catch (Exception e){
                throw new Exception("表单数据保存异常");
            }
            try{
                templateResultMaster.setStatus("2");//表单状态设置为已保存
                templateResultFacade.merge(templateResultMaster);
            }catch (Exception e){
                removeTemplateResultByMasterId(masterId);//保存异常 清除mongo中已保存的数据
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
                    if(!value.startsWith("0")){//不是以0开头的数字
                        boolean isNum = isNumeric(value);
                        if(isNum){
                            map.put(obj,map.put(obj,new BigDecimal(value)));
                        }
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
        return isNum.matches();
    }
    /**
     * 根据templaeId删除mongodb数据库中已存在的表单信息
     * @param masterId
     */
    public void removeTemplateResultByMasterId(String masterId){
        Query query = new Query();
        // query.addCriteria(where("age").gt(22));
        Criteria criteria = where("masterId").is(masterId);
        // 删除年龄大于22岁的用户
        query.addCriteria(criteria);
        mongoTemplate.remove(query,tempCollectionName);
    }
    /**
     * 保存表单数据
     * @param templateResult
     * @return
     */
    @POST
    @Path("merge-template-result")
    @Transactional
    public Response mergeTemplateResult(TemplateResult templateResult) throws Exception {
        TemplateResult templateResultMerge =null;
        try{
            //如果是保存的第一个页面，则自动生成
            String masterId = templateResult.getMasterId();
            if(StringUtils.isEmptyParam(templateResult.getId())){
                if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                    String templateId = templateResult.getTemplateId();
                    TemplateMaster templateMaster = templateResultFacade.get(TemplateMaster.class, templateId);
                    Integer limit = templateMaster.getFillLimit();
                    if(limit>0){
                        String countHql = "select masterId from TemplateResult where status<>'-1' and templateId = '"+templateId+"' group by masterId";
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
                if(StringUtils.isEmptyParam(templateResult.getMasterId())){
                    throw new Exception("masterId不能为空");
                }
            }
            templateResultMerge = templateResultFacade.merge(templateResult);
            String totalHql = "select count(*) from TemplatePage where status<>'-1' and templatePageContent is not null and templateId = '"+templateResultMerge.getTemplateId()+"'";
            Long total = templateResultFacade.createQuery(Long.class,totalHql,new ArrayList<Object>()).getSingleResult();
            String doneHql = "select count(pageId) from TemplateResult where status<>'-1' and templateResult is not null and masterId = '"+masterId+"'";
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
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(templateResultMerge).build();
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
    @Path("get-template-result-masters")
    public Page<TemplateResultMaster> getTemplateResultMasters(@QueryParam("templateId")String templateId, @QueryParam("perPage")int perPage,
                                                                 @QueryParam("userId")String userId, @QueryParam("currentPage")int currentPage,
                                                                 @QueryParam("status")String status){
        return  templateResultFacade.getTemplateResultMasters(templateId,perPage,currentPage,userId,status);
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
}
