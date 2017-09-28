package com.dch.facade;

import com.dch.entity.TemplateDataGroup;
import com.dch.entity.TemplateMaster;
import com.dch.entity.TemplatePage;
import com.dch.entity.TemplateResult;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Component
public class TemplateMasterFacade extends BaseFacade{
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
        return Response.status(Response.Status.OK).entity(merge(templateMaster)).build();
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
    public Page<TemplateMaster> getTemplateMasters(String projectId, String templateLevel, String templateStauts, String whereHql, int perPage, int currentPage) {
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
        if(perPage<=0){
            perPage = 15;
        }
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
}
