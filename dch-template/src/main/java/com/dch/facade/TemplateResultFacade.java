package com.dch.facade;

import com.dch.entity.TemplateResult;
import com.dch.entity.TemplateResultMaster;
import com.dch.entity.User;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.TemplateMasterVo;
import com.dch.vo.TemplateResultMasterVo;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                templateResultMaster.setTemplateResult(getTemplateResultJSON(templateResultMaster.getId()));
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
        if(templateResultMasterVoList!=null && !templateResultMasterVoList.isEmpty()){
            for(TemplateResultMasterVo templateResultMaster:templateResultMasterVoList){
                templateResultMaster.setTemplateResult(getTemplateResultJSON(templateResultMaster.getId()));
            }
        }
        page.setCounts(counts);
        page.setData(templateResultMasterVoList);
        long ptime = System.currentTimeMillis();
        System.out.println("===="+(ptime-etime));
        return page;
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
     * @param id
     * @return
     */
    private String getTemplateResultJSON(String id) {
        StringBuffer jsonBuffer = new StringBuffer() ;
        jsonBuffer.append("{");

        String hql = "from TemplateResult a where a.masterId= '"+id+"'" ;
        List<TemplateResult> templateResults= createQuery(TemplateResult.class,hql,new ArrayList<Object>()).getResultList();
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
}
