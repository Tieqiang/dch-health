package com.dch.facade;

import com.dch.entity.OrgInfo;
import com.dch.entity.ResearchProject;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import com.dch.vo.ResearchOrgVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 *
 * 获取科研单位以及科研项目信息
 *
 */
@Service
public class ResearchProjectFacade extends BaseFacade {


    private List<ResearchOrgVO> allResearchOrg;

    public ResearchOrgVO getResearchOrg(String orgId) {

        if (StringUtils.isEmptyParam(orgId)) {
            return null;
        } else {
            OrgInfo orgInfo = get(OrgInfo.class, orgId);


            ResearchOrgVO vo = new ResearchOrgVO();
            vo.setOrgInfo(orgInfo);
            vo.setResearchProjects(this.getPorjects(orgId));
            return vo;
        }
    }

    public List<ResearchOrgVO> getAllResearchOrg() {

        String hql = "from OrgInfo as o where status<>'-1'";
        List<OrgInfo> orgInfos = createQuery(OrgInfo.class,hql,new ArrayList<>()).getResultList();
        List<ResearchOrgVO> vos= new ArrayList<>();
        for (OrgInfo info:orgInfos){
            ResearchOrgVO vo = new ResearchOrgVO();
            vo.setOrgInfo(info);
            vo.setResearchProjects(this.getPorjects(info.getId()));
            vos.add(vo);
        }
        return vos;
    }

    private List<ResearchProject> getPorjects(String orgId){
        String hql = "from ResearchProject as p where p.orgId = '" + orgId + "' and status<>'-1'";
        List<ResearchProject> projects = createQuery(ResearchProject.class, hql, new ArrayList<>()).getResultList();
        return projects;
    }
}
