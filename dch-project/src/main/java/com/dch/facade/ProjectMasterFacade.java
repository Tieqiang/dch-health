package com.dch.facade;

import com.dch.entity.ProjectMaster;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMasterFacade extends BaseFacade {

    /**
     * 添加、删除、修改科研项目
     * @param projectMaster
     * @return
     */
    @Transactional
    public Response mergeProjectMaster(ProjectMaster projectMaster) {
        ProjectMaster merge = merge(projectMaster);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据条件获取科研项目
     * @param projectName
     * @param projectPerson
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<ProjectMaster> getProjectMasters(String projectName, String projectPerson, int perPage, int currentPage) {
        String hql=" from ProjectMaster where status <> '-1' ";
        String hqlCount="select count(*) from ProjectMaster where status <> '-1' ";

        if(projectName!=null&&!"".equals(projectName)){
            hql+="and projectName like '%"+projectName+"%' ";
            hqlCount+="and projectName like '%"+projectName+"%' ";
        }
        if(projectPerson!=null&&!"".equals(projectPerson)){
            hql+="and projectPerson ='"+ projectPerson +"'";
            hqlCount+="and projectPerson ='"+ projectPerson +"'";
        }
        TypedQuery<ProjectMaster> query = createQuery(ProjectMaster.class, hql, new ArrayList<Object>());
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
        List<ProjectMaster> projectMasterList = query.getResultList();
        page.setCounts(counts);
        page.setData(projectMasterList);
        return page;
    }

    /**
     * 获取具体的科研项目
     * @param projectId
     * @return
     */
    public ProjectMaster getProjectMaster(String projectId) throws Exception {
        String hql=" from ProjectMaster where status <> '-1' and id = '"+projectId+"'";
        List<ProjectMaster> projectMasterList = createQuery(ProjectMaster.class, hql, new ArrayList<Object>()).getResultList();
        if(projectMasterList!=null &&projectMasterList.size()>0){
            return projectMasterList.get(0);
        }else{
            throw new Exception("该科研项目不存在");
        }
    }

    /**
     * 根据用户id获取其创建的项目
     * @param userId
     * @return
     */
    public List<ProjectMaster> getProjectMastersByCreater(String userId) {
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        String hql = "from ProjectMaster where status <> '-1' and createBy = '"+userId+"'";
        List<ProjectMaster> projectMasterList = createQuery(ProjectMaster.class, hql, new ArrayList<Object>()).getResultList();
        return projectMasterList;
    }
}
