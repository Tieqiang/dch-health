package com.dch.facade;

import com.dch.entity.ProjectInfo;
import com.dch.entity.ProjectSummaryPage;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Service
public class ProjectFacade extends BaseFacade {


    /**
     *
     * 获取项目信息
     * @param type 1表示数据库，0表示工具
     * @param showFlag 1显示在首页，0不显示在首页 不传递不加以判断
     * @return
     */
    public List<ProjectInfo> getProjects(String type, String showFlag) {

        String hql = "from ProjectInfo as i where i.status<>'-1'" ;

        if(type!=null&&!"".equals(type)){
            hql +=" and  i.type='"+type+"'";
        }

        if(showFlag!=null&&!"".equals(showFlag)){
            hql +=" and i.showFlag = '"+showFlag+"'";
        }
        return createQuery(ProjectInfo.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取所有的页面
     * @param projectId
     * @return
     */
    public List<ProjectSummaryPage> getProjectPages(String projectId) {
        String hql = "from ProjectSummaryPage where status<>'-1' and projectId = '"+projectId+"'";
        return createQuery(ProjectSummaryPage.class,hql,new ArrayList<Object>()).getResultList();
    }
}
