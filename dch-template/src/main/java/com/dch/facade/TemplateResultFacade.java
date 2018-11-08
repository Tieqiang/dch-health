package com.dch.facade;

import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.entity.User;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.service.TemplateResultService;
import com.dch.util.JSONUtil;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.SolrVo;
import com.dch.vo.TemplateMasterVo;
import com.dch.vo.TemplateResultMasterVo;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class TemplateResultFacade extends BaseFacade {

    /**
     * 获取表单结果
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateMasterVo> getTemplateMasterVos(String projectId, int perPage, int currentPage) {
        String hql="select new com.dch.vo.TemplateMasterVo(m.id,m.templateName,m.templateLevel,m.templateStatus,m.projectId,m.templateDesc" +
                ",m.createDate,m.modifyDate,m.createBy,m.modifyBy,m.status,(SELECT COUNT(distinct masterId) FROM TemplateResult r WHERE r.templateId = m.id) as num) from TemplateMaster as m where m.status <> '-1' and projectId='"+projectId+"'";

        String hqlCount="select count(*) from TemplateMaster m where m.status <> '-1' and projectId='"+projectId+"'";


        TypedQuery<TemplateMasterVo> query = createQuery(TemplateMasterVo.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateMasterVo> templateMasterVoList = query.getResultList();
        page.setCounts(counts);
        page.setData(templateMasterVoList);
        return page;
    }

    /**
     * 获取某一具体表单结果
     * @param templateId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateResultMaster> getTemplateResults(String templateId,String masterId, int perPage, int currentPage) {
        String hql=" from TemplateResultMaster where status <> '-1' and templateId='"+templateId+"'";
//        if(!StringUtils.isEmptyParam(masterId)){
//            hql += " and masterId = '"+masterId+"'";
//        }
        Page<TemplateResultMaster> pageResult = getPageResult(TemplateResultMaster.class, hql, perPage, currentPage);
        List<TemplateResultMaster> results = pageResult.getData();

        List<User> users = findAll(User.class);
        for (TemplateResultMaster result:results){
            String userid = result.getCreateBy();
            for(User user:users){
                if(userid.equals(user.getId())){
                    result.setCreateBy(user.getUserName());
                }
            }
        }
        return pageResult;
    }

    public Page<TemplateResultMaster> getTemplateResultMasters(String templateId, int perPage, int currentPage, String userId,String status) {
        String hql=" from TemplateResultMaster where  templateId='"+templateId+"'";
        if(!StringUtils.isEmptyParam(status)){
            hql += " and status='"+status+"'";
        }else{
            hql += " and status <> '-1' ";
        }
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        boolean isAdmin = judgeIfAdmin(userId);
        if(!isAdmin){//管理员查看用户填报的数据不用添加create_by条件
            hql += " and createBy = '"+userId+"'";
        }
        hql += " order by createDate desc ";
        Page<TemplateResultMaster> templateResultMasterPage = getPageResult(TemplateResultMaster.class,hql,perPage,currentPage);
        List<TemplateResultMaster> templateResultMasterList = templateResultMasterPage.getData();
        //设置创建人
        if(templateResultMasterList!=null && !templateResultMasterList.isEmpty()){
            Map<String,String> map = getUserNameMap(templateResultMasterList);
            for(TemplateResultMaster templateResultMaster:templateResultMasterList){
                //templateResultMaster.setTemplateResult(getTemplateResultJSON(templateResultMaster.getId()));
                templateResultMaster.setCreateBy(map.get(templateResultMaster.getCreateBy()));
            }
            templateResultMasterPage.setData(templateResultMasterList);
        }
        return templateResultMasterPage;
    }

    public Page<TemplateResultMasterVo> getTemplateResultMastersNew(String templateId, int perPage, int currentPage, String userId, String status,
                                                                    String field,String fieldValue,MongoTemplate mongoTemplate) {
        long stime = System.currentTimeMillis();
        String hql="select new com.dch.vo.TemplateResultMasterVo(m.id,m.templateId,m.templateName,m.completeRate,''" +
                   ",m.createDate,m.modifyDate,(select userName from User where id = m.createBy) as createBy,m.createBy as modifyBy,m.status,(select distinct status FROM TemplateResultSupport  " +
                   "WHERE relatedMasterId = m.id) as flag) from TemplateResultMaster as m where templateId='"+templateId+"'";

        String hqlCount="select count(*) from TemplateResultMaster m where m.templateId='"+templateId+"'";
        //如果不为空 则查询mongo中的数据先进行过滤
        if(!StringUtils.isEmptyParam(field) && !StringUtils.isEmptyParam(fieldValue)){
            List<String> masterIdList = getMasterIdsByQueryMongo(templateId,field,fieldValue,status,mongoTemplate);
            if(!masterIdList.isEmpty()){
                String masterIds = StringUtils.getQueryIdsString(masterIdList);
                hql += " and m.id in (" + masterIds + ")";
                hqlCount += " and m.id in (" + masterIds + ")";
            }else {
                hql += " and 1=2 ";
                hqlCount += " and 1=2 ";
            }
        }
        if(!StringUtils.isEmptyParam(status)){
            hql += " and m.status='"+status+"'";
            hqlCount += " and m.status='"+status+"'";
        }else{
            hql += " and m.status <> '-1' ";
            hqlCount += " and m.status <> '-1' ";
        }
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        boolean isAdmin = judgeIfAdmin(userId);
        if(!isAdmin){//管理员查看用户填报的数据不用添加create_by条件
            hql += " and m.createBy = '"+userId+"'";
            hqlCount += " and m.createBy = '"+userId+"'";
        }
        hql += " order by m.createDate desc ";

        TypedQuery<TemplateResultMasterVo> query = createQuery(TemplateResultMasterVo.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateResultMasterVo> templateResultMasterVoList = query.getResultList();
        long etime = System.currentTimeMillis();
        //System.out.println("===="+(etime-stime));
        Map<String,String> templatejsonMap = new HashMap<>();
        if(templateResultMasterVoList!=null && !templateResultMasterVoList.isEmpty()){
            for(TemplateResultMasterVo templateResultMaster:templateResultMasterVoList){
                templatejsonMap.put(templateResultMaster.getId(),"");
            }
        }
        fillMapResult(templatejsonMap);
        if(templateResultMasterVoList!=null && !templateResultMasterVoList.isEmpty()){
            for(TemplateResultMasterVo templateResultMaster:templateResultMasterVoList){
                templateResultMaster.setTemplateResult(templatejsonMap.get(templateResultMaster.getId()));
            }
        }
        page.setCounts(counts);
        page.setData(templateResultMasterVoList);
        long ptime = System.currentTimeMillis();
        //System.out.println("===="+(ptime-etime));
        return page;
    }

    public void fillMapResult(Map<String,String> templatejsonMap){
        Map<String,List<TemplateResult>> map = new HashMap<>();
        StringBuffer sb = new StringBuffer("");
        for(String key:templatejsonMap.keySet()){
            sb.append("'").append(key).append("',");
        }
        String ids = sb.toString();
        ids = ids.length()>1?ids.substring(0,ids.length()-1):"''";
        try{
            String hql = "from TemplateResult a where a.masterId in ("+ids+")" ;
            List<TemplateResult> templateResults= createQuery(TemplateResult.class,hql,new ArrayList<Object>()).getResultList();
//            String param = "categoryCode:templateResult" ;
//            param += " AND title:("+ids+") ";
//            List<SolrVo> list = baseSolrFacade.getSolrObjectByParam(param,SolrVo.class);
            for (TemplateResult result:templateResults){
                if(map.containsKey(result.getMasterId())){
                    List<TemplateResult> innerTempList = map.get(result.getMasterId());
                    innerTempList.add(result);
                }else{
                    List<TemplateResult> innerTempList = new ArrayList<>();
                    innerTempList.add(result);
                    map.put(result.getMasterId(),innerTempList);
                }
            }
            for(String key:map.keySet()){
                templatejsonMap.put(key,getTemplateResultJSON(map.get(key)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean judgeIfAdmin(String userId){
        boolean isAdmin = false;
        String sql = "select r.role_name from user_vs_role as ur,role as r where ur.role_id = r.id and r.role_name in ('表单管理者') and ur.user_id = " +
                "'"+userId+"'";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            isAdmin = true;
        }
        return isAdmin;
    }
    /**
     * 获取表单填写的值
     * @param templateResults
     * @return
     */
    private String getTemplateResultJSON(List<TemplateResult> templateResults) {
        StringBuffer jsonBuffer = new StringBuffer() ;
        jsonBuffer.append("{");
        for (TemplateResult result:templateResults){
            String templateResult = result.getTemplateResult();
            if(templateResult.startsWith("{")&&templateResult.endsWith("}") && !"{}".equals(templateResult)){
                jsonBuffer.append(templateResult.substring(1,templateResult.length()-1));
                jsonBuffer.append(",");
            }else{
                continue;
            }
        }
        jsonBuffer.delete(jsonBuffer.lastIndexOf(","),jsonBuffer.lastIndexOf(",")+1);
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }

    /**
     * 获取用户列表Map
     * @param templateResultMasterList
     * @return
     */
    public Map<String,String> getUserNameMap(List<TemplateResultMaster> templateResultMasterList){
        Map<String,String> map = new HashMap<String,String>();
        StringBuffer stringBuffer = new StringBuffer("");
        for(TemplateResultMaster templateResultMaster:templateResultMasterList){
            stringBuffer.append("'").append(templateResultMaster.getCreateBy()).append("',");
        }
        String userIds = stringBuffer.toString();
        if(!StringUtils.isEmptyParam(userIds)){
            userIds = userIds.substring(0,userIds.length()-1);
        }
        String sql = "select id,user_name from user where id in ("+userIds+")";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                map.put(params[0].toString(),params[1].toString());
            }
        }
        return map;
    }

    private String getTemplateResultJsonBySolr(List<SolrVo> solrVos) {
        StringBuffer jsonBuffer = new StringBuffer() ;
        jsonBuffer.append("{");
        for (SolrVo result:solrVos){
            String templateResult = result.getDesc();
            if(templateResult.startsWith("{")&&templateResult.endsWith("}") && !"{}".equals(templateResult)){
                jsonBuffer.append(templateResult.substring(1,templateResult.length()-1));
                jsonBuffer.append(",");
            }else{
                continue;
            }
        }
        jsonBuffer.delete(jsonBuffer.lastIndexOf(","),jsonBuffer.lastIndexOf(",")+1);
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }

    /**
     * 删除填报的表单及mongo中存储的数据
     * @param id
     * @param userId
     * @param type
     * @param mongoTemplate
     * @return
     * @throws Exception
     */
    @Transactional
    public Response deleteTemplateResultMaster(String id, String userId,String type,MongoTemplate mongoTemplate) throws Exception{
        TemplateResultMaster templateResultMaster = null;
        try{
            templateResultMaster = get(TemplateResultMaster.class,id);
            String status = templateResultMaster.getStatus();
            Boolean isAdmin = judgeIfAdmin(userId);
            if(!isAdmin){
                throw new Exception("您不是表单管理者，不能删除表单！");
            }
            Query query = new Query();
            Criteria criteria = where("masterId").is(id);
            query.addCriteria(criteria);
            if("fill".equals(type)){
                if("2".equals(status)){
                    mongoTemplate.remove(query, TemplateResultService.tempFillName);
                }
                mongoTemplate.remove(query, TemplateResultService.templateResultList);
            }else{
                mongoTemplate.remove(query,TemplateResultService.tempCollectionName);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        if(templateResultMaster!=null){
            remove(templateResultMaster);//删除表单
            removeTemplateResult(id);
            removeTemplateResultSupport(id);
        }
        return Response.status(Response.Status.OK).entity(templateResultMaster).build();
    }

    @Transactional
    public void removeTemplateResult(String masterId){
        String hql = "delete from TemplateResult where masterId = '"+masterId+"'";
        excHql(hql);
    }

    @Transactional
    public void removeTemplateResultSupport(String masterId){
        String hql = "delete from TemplateResultSupport where relatedMasterId = '"+masterId+"'";
        excHql(hql);
    }

    public Page<TemplateResultMasterVo> getTemplateResultMasterVoByParam(Map<String,Object> paramMap,MongoTemplate mongoTemplate) {
        String templateId = (String)paramMap.get("templateId");
        String userId = (String)paramMap.get("userId");
        String status = (String)paramMap.get("status");
        int perPage = paramMap.get("perPage")==null?0:((int)paramMap.get("perPage"));
        int currentPage = paramMap.get("currentPage")==null?0:((int)paramMap.get("currentPage"));
        List<String> mongoFields = (List<String>)paramMap.get("fields");
        long stime = System.currentTimeMillis();
        String hql="select new com.dch.vo.TemplateResultMasterVo(m.id,m.templateId,m.templateName,m.completeRate,''" +
                ",m.createDate,m.modifyDate,(select userName from User where id = m.createBy) as createBy,m.modifyBy,m.status,(select distinct status FROM TemplateResultSupport  " +
                "WHERE relatedMasterId = m.id) as flag) from TemplateResultMaster as m where templateId='"+templateId+"'";

        String hqlCount="select count(*) from TemplateResultMaster m where m.templateId='"+templateId+"'";
        if(!StringUtils.isEmptyParam(status)){
            hql += " and m.status='"+status+"'";
            hqlCount += " and m.status='"+status+"'";
        }else{
            hql += " and m.status <> '-1' ";
            hqlCount += " and m.status <> '-1' ";
        }
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        boolean isAdmin = judgeIfAdmin(userId);
        if(!isAdmin){//管理员查看用户填报的数据不用添加create_by条件
            hql += " and m.createBy = '"+userId+"'";
            hqlCount += " and m.createBy = '"+userId+"'";
        }
        hql += " order by m.createDate desc ";

        TypedQuery<TemplateResultMasterVo> query = createQuery(TemplateResultMasterVo.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateResultMasterVo> templateResultMasterVoList = query.getResultList();
        long etime = System.currentTimeMillis();
        System.out.println("===="+(etime-stime));
        Map<String,String> templatejsonMap = new HashMap<>();
        if(templateResultMasterVoList!=null && !templateResultMasterVoList.isEmpty()){
            for(TemplateResultMasterVo templateResultMaster:templateResultMasterVoList){
                templatejsonMap.put(templateResultMaster.getId(),"");
            }
        }
        getMongoQueryResult(templatejsonMap,mongoFields,mongoTemplate);
        if(templateResultMasterVoList!=null && !templateResultMasterVoList.isEmpty()){
            for(TemplateResultMasterVo templateResultMaster:templateResultMasterVoList){
                templateResultMaster.setTemplateResult(templatejsonMap.get(templateResultMaster.getId()));
            }
        }
        page.setCounts(counts);
        page.setData(templateResultMasterVoList);
        long ptime = System.currentTimeMillis();
        System.out.println("===="+(ptime-etime));
        return page;
    }

    public void getMongoQueryResult(Map<String,String> templatejsonMap,List<String> mongoFields,MongoTemplate mongoTemplate){
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("masterId").in(templatejsonMap.keySet()));
            query.fields().include("masterId");
            for(String field:mongoFields){
                query.fields().include(field);
            }
//            query.fields().include("dch_1523154179240");
//            query.fields().include("dch_1523154196755");
//            query.fields().include("dch_1523154214456");
//            query.fields().include("dch_1523179199291.dch_1523179560994");
            List<Document> resultList = mongoTemplate.find(query,Document.class,"templateFilling");
            for (Document result:resultList){
                String masterId = result.get("masterId")+"";
                if(templatejsonMap.containsKey(masterId)){
                    templatejsonMap.put(masterId, JSONUtil.objectToJsonString(result));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * mongodb模糊查询指定字段 并返回结果
     * @param templateId
     * @param field
     * @param fieldValue
     * @param mongoTemplate
     * @param <T>
     * @return
     */
    public <T> List<T> getMasterIdsByQueryMongo(String templateId,String field,String fieldValue,String status,MongoTemplate mongoTemplate){
        List<T> returnList = new ArrayList<>();
        try{
            if(StringUtils.isEmptyParam(field) && !StringUtils.isEmptyParam(fieldValue)){//全文检索
               List templateList = getTemplateResultList(templateId,fieldValue,status);
                return getResultListByJudege(templateList,field,fieldValue,true);
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("templateId").is(templateId));
            Pattern pattern=Pattern.compile("^.*"+fieldValue+".*$", Pattern.CASE_INSENSITIVE);
            Boolean isNumber = judgeFieldIsNumber(field,fieldValue);
            if(!"2".equals(status)){//查询录入的数据
                List templateList = getTemplateResultList(templateId,field,status);
                return getResultListByJudege(templateList,field,fieldValue,false);
            }
            if(!isNumber){
                query.addCriteria(Criteria.where(field).regex(pattern));
            }
            query.fields().include("masterId");
            query.fields().include(field);
            List<Document> resultList = mongoTemplate.find(query,Document.class,TemplateResultService.tempFillName);
            for (Document result:resultList){
                String masterId = result.get("masterId")+"";
                if(isNumber){
                    String mvalue = result.get(field) + "";
                    if(mvalue.contains(fieldValue) && !returnList.contains(masterId)){
                        returnList.add((T)masterId);
                    }
                }else{
                    if(!returnList.contains(masterId)){
                        returnList.add((T)masterId);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnList;
    }

    public Boolean judgeFieldIsNumber(String field,String fieldValue){
        Boolean isNumber = true;
        try {
            Integer result = Integer.valueOf(fieldValue);
        }catch (NumberFormatException e){
            isNumber = false;
        }
        return isNumber;
    }

    public List getTemplateResultList(String templateId,String field,String status){
        StringBuffer sb = new StringBuffer("select m.id,t.template_result from template_result t," +
                "template_result_master m where t.master_id = m.id");
        if(!"2".equals(status)){
            sb.append(" and m.status<>'-1' ");
        }else{
            sb.append(" and m.status ='2' ");
        }
        sb.append("and t.template_id = '").append(templateId).append("' and t.template_result like '%").append(field).append("%'");
        List list = createNativeQuery(sb.toString()).getResultList();
        return list;
    }

    /**
     * 判断字段的值模糊匹配
     * @param templateResult
     * @param field
     * @param fieldValue
     * @return
     */
    public Boolean judgeFieldValueExist(String templateResult,String field,String fieldValue,Boolean matchAll){
        Boolean isExist = false;
        if(matchAll){//全文匹配
            String templateNewResult = templateResult.replaceAll(".{1,3}^?dch_[\\d]*.[\\d]$?","");
            if(templateNewResult.contains(fieldValue)){
                return true;
            }
        }
        int index = templateResult.indexOf(field);
        String replaceResult = templateResult.replace(field,"");
        String subReplaceResult = replaceResult.substring(index+1);
        int index_real = subReplaceResult.indexOf("dch_");
        String real_result = index_real<1?subReplaceResult:subReplaceResult.substring(0,index_real);
        if(real_result.contains("$")){
            real_result = real_result.replaceAll(".{1,3}^?[$][\\d]$?","");
        }
        if(real_result.contains(fieldValue)){
            isExist = true;
        }
        return isExist;
    }

    public <T> List<T> getResultListByJudege(List templateList,String field,String fieldValue,Boolean matchAll){
        List<T> returnList = new ArrayList<>();
        if(templateList!=null && !templateList.isEmpty()){
            templateList.stream().forEach(tl -> {
                Object[] innerParams = (Object[])tl;
                String masterId = innerParams[0].toString();
                String templateResult = innerParams[1].toString();
                Boolean isExist = judgeFieldValueExist(templateResult,field,fieldValue,matchAll);
                if(isExist){returnList.add((T)masterId);}
            });
        }
        return returnList;
    }
}
