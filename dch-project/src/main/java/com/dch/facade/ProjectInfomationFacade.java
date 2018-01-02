package com.dch.facade;

import com.dch.entity.ProjectInfomation;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.UserVo;
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

    /**
     * 获取最新的N条项目动态信息
     * @param playNum
     * @return
     */
    public List<ProjectInfomation> getRecentProjectInformations(int playNum) {
        UserVo userVo =  UserUtils.getCurrentUser();
        String phql = "select id from ProjectMaster where status <> '-1' and createBy ='"+userVo.getId()+"'";
        List<String> list = createQuery(String.class,phql,new ArrayList<Object>()).getResultList();
        String otherHql = "select projectId from ProjectMember where status <> '-1' and personId = '"+userVo.getId()+"' " +
                          " and createBy<>'"+userVo.getId()+"'";
        List<String> otherList = createQuery(String.class,otherHql,new ArrayList<Object>()).getResultList();
        list.addAll(otherList);
        String projectIds = getProjectIds(list);
        if(playNum<=0){//默认展示10条
            playNum = 10;
        }
        String hql=" from ProjectInfomation where status <> '-1' ";
        if(StringUtils.isEmptyParam(projectIds)){
            hql += " and 1=2";
        }else{
            hql += " and projectId in ("+projectIds+")";
        }
        hql += " order by createDate desc ";
        Page<ProjectInfomation> projectInfomationPage = getPageResult(ProjectInfomation.class,hql,playNum,0);
        return projectInfomationPage.getData();
    }

    private String getProjectIds(List<String> list){
        StringBuffer projectIdsBf = new StringBuffer("");
        if(list!=null && !list.isEmpty()){
            for(String projectId:list){
                projectIdsBf.append("'").append(projectId).append("',");
            }
        }
        String projectIds = projectIdsBf.toString();
        if(StringUtils.isEmptyParam(projectIds)){
            return "";
        }else{
            return projectIds.substring(0,projectIds.length()-1);
        }
    }
}
