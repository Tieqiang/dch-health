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


    public ResearchOrgVO getResearchOrg(String orgId) {

        if (StringUtils.isEmptyParam(orgId)) {
            return null;
        } else {
            OrgInfo orgInfo = get(OrgInfo.class, orgId);

            String hql = "from ResearchProject as p where p.orgId = '" + orgId + "'";
            List<ResearchProject> projects = createQuery(ResearchProject.class, hql, new ArrayList<>()).getResultList();
            ResearchOrgVO vo = new ResearchOrgVO();
            vo.setOrgInfo(orgInfo);
            vo.setResearchProjects(projects);
            return vo;
        }
    }
}
