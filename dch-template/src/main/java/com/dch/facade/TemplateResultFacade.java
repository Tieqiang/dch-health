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

    public Page<TemplateResultMasterVo> getTemplateResultMastersNew(String templateId, int perPage, int currentPage, String userId, String status) {
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
        fillMapResult(templatejsonMap);
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

    public void fillMapResult(Map<String,String> templatejsonMap){
        Map<String,List<TemplateResult>> map = new HashMap<>();
        StringBuffer sb = new StringBuffer("");
        for(String key:templatejsonMap.keySet()){
            sb.append("'").append(key).append("',");
        }
        String ids = sb.toString();
        ids = ids.length()>1?ids.substring(0,ids.length()-1):"";
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
}
