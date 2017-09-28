package com.dch.facade;

import com.dch.entity.ProjectInfomation;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Component
public class ProjectInfomationFacade extends BaseFacade{

    /**
     * 添加、删除、修改项目动态
     * @param projectInfomation
     * @return
     */
    @Transactional
    public Response mergeProjectInfomation(ProjectInfomation projectInfomation) {
        return Response.status(Response.Status.OK).entity(merge(projectInfomation)).build();
    }

    /**
     * 获取项目动态
     * @param projectId
     * @param perPage
     * @param currentPage
     */
    public Page<ProjectInfomation> getProjectInfoMations(String projectId, int perPage, int currentPage) {
        String hql = " from ProjectInfomation where status<>'-1' and projectId = '"+projectId+"'";
        return getPageResult(ProjectInfomation.class,hql,perPage,currentPage);
    }
    /**
     *获取具体的项目动态
     * @param infoId
     */
    public ProjectInfomation getProjectInfoMation(String infoId) throws Exception{
        String hql=" from ProjectInfomation where status <> '-1' and id='"+ infoId +"'";
        List<ProjectInfomation> projectInfomationList = createQuery(ProjectInfomation.class,hql,new ArrayList<Object>()).getResultList();
        if(projectInfomationList!=null && !projectInfomationList.isEmpty()){
            return projectInfomationList.get(0);
        }else{
            throw new Exception("该项目动态信息不存在！");
        }
    }
}
