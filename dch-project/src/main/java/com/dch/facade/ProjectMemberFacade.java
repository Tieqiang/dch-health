package com.dch.facade;

import com.dch.entity.ProjectMember;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.projectMemberVo;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMemberFacade extends BaseFacade {

    /**
     * 添加、删除、修改科员项目成员
     * @param projectMember
     * @return
     */
    @Transactional
    public Response mergeProjectMember(ProjectMember projectMember) throws Exception {
        if(!"-1".equals(projectMember.getStatus())){
            String hql=" from ProjectMember where status <> '-1' " +
                    "and projectId='"+projectMember.getProjectId()+"' " +
                    "and personId='"+projectMember.getPersonId()+"' and personStatus='"+projectMember.getPersonStatus()+"'";
            List<ProjectMember> memberList = createQuery(ProjectMember.class, hql, new ArrayList<Object>()).getResultList();
            if(memberList!=null && memberList.size()>0){
                throw new Exception("该用户已是此项目的成员！");
            }
        }
        ProjectMember merge = merge(projectMember);
        projectMemberVo memberVo = getProjectMember(merge.getId());

        return Response.status(Response.Status.OK).entity(memberVo).build();
    }

    /**
     * 获取具体的项目组成员
     * @param projectId
     * @param personStatus
     * @return
     */
    public List<projectMemberVo> getProjectMembers(String projectId, String personStatus) {
        String hql="select new com.dch.vo.projectMemberVo(m.id,m.personId,(select userName from User u where u.status <> '-1' and u.id = m.personId) as " +
                "userName,(select loginName from User u where u.status <> '-1' and u.id = m.personId) as loginName," +
                "m.projectId,m.personStatus,m.createDate,m.modifyDate,m.createBy,m.modifyBy,m.status) from ProjectMember as m where m.status <> '-1' and projectId='"+projectId+"'";
        if(!StringUtils.isEmptyParam(personStatus)){
            hql+=" and personStatus='"+personStatus+"'";
        }
        List<projectMemberVo> projectMemberList = createQuery(projectMemberVo.class, hql, new ArrayList<Object>()).getResultList();
        return projectMemberList;
    }


    public projectMemberVo getProjectMember(String id) {
        String hql="select new com.dch.vo.projectMemberVo(m.id,m.personId,(select userName from User u where u.status <> '-1' and u.id = m.personId) as " +
                "userName,(select loginName from User u where u.status <> '-1' and u.id = m.personId) as loginName," +
                "m.projectId,m.personStatus,m.createDate,m.modifyDate,m.createBy,m.modifyBy,m.status) from ProjectMember as m where m.status <> '-1' " +
                "and id='"+id+"'";

        List<projectMemberVo> projectMemberList = createQuery(projectMemberVo.class, hql, new ArrayList<Object>()).getResultList();
        if(projectMemberList!=null && projectMemberList.size()>0){
            return projectMemberList.get(0);
        }
        return null;
    }
}
