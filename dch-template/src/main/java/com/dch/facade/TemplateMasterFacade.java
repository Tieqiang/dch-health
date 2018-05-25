package com.dch.facade;

import com.dch.entity.TemplateDataGroup;
import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplatePage;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.SolrVo;
import com.dch.vo.TemplateMasterModuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Component
public class TemplateMasterFacade extends BaseFacade{

    @Autowired
    private BaseSolrFacade baseSolrFacade;
    /**
     * 添加、删除、修改表单名称
     * @param templateMaster
     * @return
     */
    @Transactional
    public Response mergeTemplateMaster(TemplateMaster templateMaster) throws Exception{
        //删除表单模板 检查其下是否关联有 元数据组，表单页,表单数据结果 如果有 则先删除 元数据组，表单页,表单数据结果
        if("-1".equals(templateMaster.getStatus())){
            List<TemplateDataGroup> templateDataGroupList = getTemplateDataGroupsByTemplateId(templateMaster.getId());
            if(templateDataGroupList!=null && !templateDataGroupList.isEmpty()){
                throw new Exception("该表单模板下有关联的元数据组，请先删除元数据组信息");
            }
            List<TemplatePage> templatePageList = getTemplatePagesByTemplateId(templateMaster.getId());
            if(templatePageList!=null && !templatePageList.isEmpty()){
                throw new Exception("该表单模板下有关联的表单页信息，请先删除表单页信息");
            }
            List<TemplateResult> templateResultList = getTemplateResultsByTemplateId(templateMaster.getId());
            if(templateResultList!=null && !templateResultList.isEmpty()){
                throw new Exception("该表单模板下有关联的表单数据结果信息，请先删除表单数据结果信息");
            }
        }

        String sql = "select id from template_master where status<>'-1' and template_name = '"+templateMaster.getTemplateName()+"' and id<>'"+templateMaster.getId()+"' ";
        if(StringUtils.isEmptyParam(templateMaster.getProjectId())){
            sql = sql + " and project_id is null";
        }else{
            sql = sql + " and project_id = '"+templateMaster.getProjectId()+"'";
        }
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            throw new Exception("该表单名称已存在，请重新填写");
        }
        TemplateMaster merge = merge(templateMaster);
        if("2".equals(templateMaster.getPublishStatus())){//2全站发布
            SolrVo solrVo=new SolrVo();
            solrVo.setId(merge.getId());
            solrVo.setTitle(merge.getTemplateName());
            solrVo.setDesc(merge.getTemplateDesc());
            solrVo.setCategoryCode("bdsj001");
            solrVo.setLabel(merge.getTemplateLevel());
            solrVo.setCategory(PinYin2Abbreviation.cn2py(merge.getTemplateName()));
            solrVo.setFirstPy(PinYin2Abbreviation.getFirstPy(solrVo.getCategory()));
            baseSolrFacade.addObjectMessageToMq(solrVo);
        }
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据表单模板id获取元数据组信息
     * @param templateId
     * @return
     */
    public List<TemplateDataGroup> getTemplateDataGroupsByTemplateId(String templateId){
        String hql = "from TemplateDataGroup where status<>'-1' and templateId = '"+templateId+"'";
        return createQuery(TemplateDataGroup.class,hql,new ArrayList<Object>()).getResultList();
    }
    /**
     * 根据表单模板id获取表单页信息
     * @param templateId
     * @return
     */
    public List<TemplatePage> getTemplatePagesByTemplateId(String templateId){
        String hql = "from TemplatePage where status<>'-1' and templateId = '"+templateId+"'";
        return createQuery(TemplatePage.class,hql,new ArrayList<Object>()).getResultList();
    }
    /**
     * 根据表单模板id获取表单数据结果信息
     * @param templateId
     * @return
     */
    public List<TemplateResult> getTemplateResultsByTemplateId(String templateId){
        String hql = "from TemplateResult where status<>'-1' and templateId = '"+templateId+"'";
        return createQuery(TemplateResult.class,hql,new ArrayList<Object>()).getResultList();
    }
    /**
     *获取表单
     * @param projectId 项目Id（选传）
     * @param templateLevel 表单级别（选传）
     * @param templateStauts 表单状态（注意区分stauts)
     * @param whereHql 前端拼接条件
     * @param perPage 每页显示条数，默认15条
     * @param currentPage 当前第几页，默认为1
     * @return
     */
    public Page<TemplateMaster> getTemplateMasters(String projectId, String templateLevel, String templateStauts, String whereHql, int perPage, int currentPage,String publishStatus) {
        String hql = " from TemplateMaster where status<>'-1'";
        if(!StringUtils.isEmptyParam(projectId)){
            hql += " and projectId = '"+projectId+"'";
        }
        if(!StringUtils.isEmptyParam(templateLevel)){
            hql += " and templateLevel = '"+templateLevel+"'";
        }
        if(!StringUtils.isEmptyParam(templateStauts)){
            hql += " and templateStatus = '"+templateStauts+"'";
        }
        if(!StringUtils.isEmptyParam(whereHql)){
            hql += " and "+whereHql;
        }
        if(!StringUtils.isEmptyParam(publishStatus)){
            hql += " and publishStatus = '"+publishStatus+"'";
        }
        if(perPage<=0){
            perPage = 15;
        }
        hql += " order by createDate desc";
        return getPageResult(TemplateMaster.class,hql,perPage,currentPage);
    }

    public TemplateMaster getTemplateMaster(String templateMasterId) throws Exception{
        String hql = " from TemplateMaster where status<>'-1' and id = '"+templateMasterId+"'";
        List<TemplateMaster> templateMasterList = createQuery(TemplateMaster.class,hql,new ArrayList<Object>()).getResultList();
        if(templateMasterList!=null && !templateMasterList.isEmpty()){
            return templateMasterList.get(0);
        }else{
            throw new Exception("该表单信息不存在！");
        }
    }

    /**
     *根据用户id获取所属表单
     * @param userId
     * @return
     */
    public List<TemplateMaster> getTemplateMastersByCreater(String userId) {
        String hql = "from TemplateMaster where status<>'-1' and createBy = '"+userId+"'";
        List<TemplateMaster> templateMasterList = createQuery(TemplateMaster.class,hql,new ArrayList<Object>()).getResultList();
        return templateMasterList;
    }

    /**
     * 根据项目获取表单数据
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<TemplateMaster> getTemplateMasterByProjectId(String projectId, int perPage, int currentPage) {
        String hql=" from TemplateMaster where status <> '-1' and projectId='"+projectId+"'";
        Page<TemplateMaster> templateMasterPage = getPageResult(TemplateMaster.class, hql, perPage, currentPage);
        return templateMasterPage;
    }

    public Page<TemplateMasterModuleVo> getTemplateMastersByModel(String modelId, String templateLevel, String templateStauts, String whereHql, int perPage, int currentPage, String publishStatus) {
        String userId = UserUtils.getCurrentUser().getId();//获取当前登陆用户id
        String hql="select new com.dch.vo.TemplateMasterModuleVo(m.id,m.templateName,m.templateLevel,m.templateStatus,m.projectId,m.fillLimit,m.templateDesc,m.publishStatus,m.modelId," +
                "m.displayConfig,m.createDate,m.modifyDate,m.createBy,m.modifyBy,m.status,(SELECT COUNT(distinct masterId) FROM TemplateResult r WHERE r.templateId = m.id and r.status<>'-1' and r.createBy='"+userId+"') as fillNum," +
                "(SELECT COUNT(id) FROM TemplateResultMaster rm WHERE rm.templateId = m.id and rm.status='2') as commitNum) from TemplateMaster as m where m.status <> '-1' ";

        String hqlCount="select count(*) from TemplateMaster m where m.status <> '-1' ";

        if(!StringUtils.isEmptyParam(modelId)){
            hql += " and modelId = '"+modelId+"'";
            hqlCount += " and modelId = '"+modelId+"'";
        }
        if(!StringUtils.isEmptyParam(templateLevel)){
            hql += " and templateLevel = '"+templateLevel+"'";
            hqlCount += " and templateLevel = '"+templateLevel+"'";
        }
        if(!StringUtils.isEmptyParam(templateStauts)){
            hql += " and templateStatus = '"+templateStauts+"'";
            hqlCount += " and templateStatus = '"+templateStauts+"'";
        }
        if(!StringUtils.isEmptyParam(whereHql)){
            hql += " and "+whereHql;
            hqlCount += " and "+whereHql;
        }
        if(!StringUtils.isEmptyParam(publishStatus)){
            hql += " and publishStatus = '"+publishStatus+"'";
            hqlCount += " and publishStatus = '"+publishStatus+"'";
        }
        hql += " order by createDate desc";

        TypedQuery<TemplateMasterModuleVo> query = createQuery(TemplateMasterModuleVo.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=15;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<TemplateMasterModuleVo> templateMasterVoList = query.getResultList();
        page.setCounts(counts);
        page.setData(templateMasterVoList);
        return page;
    }
}
