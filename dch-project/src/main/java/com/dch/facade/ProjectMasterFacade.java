package com.dch.facade;

import com.dch.entity.ProjectInfomation;
import com.dch.entity.ProjectMaster;
import com.dch.entity.ProjectMember;
import com.dch.entity.User;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.UserVo;
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
    public Response mergeProjectMaster(ProjectMaster projectMaster) throws Exception {
        if(!"-1".equals(projectMaster.getStatus())){
            String hql=" from ProjectMaster where projectName='"+projectMaster.getProjectName()+"' and id <>'"+projectMaster.getId()+"'";
            List<ProjectMaster> projectMasters = createQuery(ProjectMaster.class, hql, new ArrayList<Object>()).getResultList();
            if(projectMasters!=null && projectMasters.size()>0){
                throw new Exception("该项目名称已经存在！");
            }
        }
        ProjectMaster merge = merge(projectMaster);
        if(StringUtils.isEmptyParam(projectMaster.getId())){
            ProjectMember projectMember = new ProjectMember();
            projectMember.setProjectId(merge.getId());
            projectMember.setPersonStatus("1");
            projectMember.setPersonId(UserUtils.getCurrentUser().getId());
            projectMember.setStatus("1");
            merge(projectMember);
            //创建科研项目时添加项目动态
            UserVo userVo = UserUtils.getCurrentUser();
            User user = get(User.class,userVo.getId());
            String modeContent = StringUtils.getStringByKey("projectContent");
            ProjectInfomation projectInfomation = new ProjectInfomation();
            projectInfomation.setInfoTitle(merge.getProjectName());
            modeContent = modeContent.replace("user",user.getUserName());
            if(!StringUtils.isEmptyParam(merge.getProjectDesc())){
                modeContent = modeContent.replace("desc",merge.getProjectDesc());
            }else{
                modeContent.replace("desc",projectMaster.getProjectName());
            }
            projectInfomation.setInfoContent(modeContent);
            projectInfomation.setProjectId(merge.getId());
            projectInfomation.setStatus("0");
            merge(projectInfomation);
        }
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
    public Page<ProjectMaster> getProjectMasters(String projectName, String projectPerson,String createrId,String type, int perPage, int currentPage) {
        String hql=" from ProjectMaster as m where m.status <> '-1' ";
        String hqlCount="select count(*) from ProjectMaster as m where m.status <> '-1' ";

        if(projectName!=null&&!"".equals(projectName)){
            hql+="and m.projectName like '%"+projectName+"%' ";
            hqlCount+="and m.projectName like '%"+projectName+"%' ";
        }
        if(projectPerson!=null&&!"".equals(projectPerson)){
            hql+="and m.projectPerson ='"+ projectPerson +"'";
            hqlCount+="and m.projectPerson ='"+ projectPerson +"'";
        }
        String userId = createrId;
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        if(!StringUtils.isEmptyParam(userId)){
            if("all".equals(type)){
                hql += " and (m.createBy = '"+userId+"' or exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1' and personStatus ='1'))";
                hqlCount += " and (m.createBy = '"+userId+"' or exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1' and personStatus ='1'))";
            }else if("act".equals(type)){
                hql += " and (m.createBy <> '"+userId+"' and exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1' and personStatus ='1')) ";
                hqlCount += " and (m.createBy <> '"+userId+"' and exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1' and personStatus ='1')) ";
            }else{
                hql += " and m.createBy = '"+userId+"' ";
                hqlCount += " and m.createBy = '"+userId+"' ";
            }
        }
        hql += " order by m.createDate desc ";
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
        String hql = "select p from ProjectMaster as p where p.status <> '-1' and  exists(select 1 from ProjectMember where status<>'-1' and personStatus = '1' and personId = '"+userId+"' and projectId = p.id)";
        List<ProjectMaster> projectMasterList = createQuery(ProjectMaster.class, hql, new ArrayList<Object>()).getResultList();
        return projectMasterList;
    }

    /**
     * 根据项目名称模糊匹配创建者未参与的项目
     * @param projectName
     * @param createrId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<ProjectMaster> getNotJoinProjectMasters(String projectName, String createrId, int perPage, int currentPage) {
        String hql=" from ProjectMaster as m where m.status <> '-1' ";
        String hqlCount="select count(*) from ProjectMaster as m where m.status <> '-1' ";

        if(projectName!=null&&!"".equals(projectName)){
            hql+="and m.projectName like '%"+projectName+"%' ";
            hqlCount+="and m.projectName like '%"+projectName+"%' ";
        }
        String userId = createrId;
        if(StringUtils.isEmptyParam(userId)){
            userId = UserUtils.getCurrentUser().getId();
        }
        if(!StringUtils.isEmptyParam(userId)){
                hql += " and (m.createBy not in ('"+userId+"') and not exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1'))";
                hqlCount += " and (m.createBy not in ('"+userId+"') and not exists(select 1 from ProjectMember where projectId = m.id and personId = '"+userId+"' " +
                        "and status<>'-1'))";
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
     * 申请退出项目
     */
    public Response quitProjectMaster(ProjectMaster projectMaster) {
        //projectMaster.setStatus("-1");
        //ProjectMaster merge = merge(projectMaster);
        String hql=" from ProjectMember where projectId='"+projectMaster.getId()+"' and personId='"+UserUtils.getCurrentUser().getId()+"'";
        List<ProjectMember> projectMembers = createQuery(ProjectMember.class, hql, new ArrayList<Object>()).getResultList();
        if(projectMembers!=null && !projectMembers.isEmpty()){
            for(ProjectMember projectMember:projectMembers){
                projectMember.setStatus("-1");
                merge(projectMember);
            }
        }
        return Response.status(Response.Status.OK).entity(projectMaster).build();
    }



}
