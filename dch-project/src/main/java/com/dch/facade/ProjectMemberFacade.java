package com.dch.facade;

import com.dch.entity.ProjectMember;
import com.dch.facade.common.BaseFacade;
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
    public Response mergeProjectMember(ProjectMember projectMember) {
        ProjectMember merge = merge(projectMember);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取具体的项目组成员
     * @param projectId
     * @return
     */
    public List<ProjectMember> getProjectMembers(String projectId) {
        String hql=" from ProjectMember where status <> '-1' and id = ' "+projectId+" '";
        List<ProjectMember> projectMemberList = createQuery(ProjectMember.class, hql, new ArrayList<>()).getResultList();
        return projectMemberList;
    }
}
