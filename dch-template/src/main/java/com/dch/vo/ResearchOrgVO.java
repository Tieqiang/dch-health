package com.dch.vo;

import com.dch.entity.OrgInfo;
import com.dch.entity.ResearchProject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchOrgVO implements Serializable {

    private OrgInfo orgInfo ;

    private List<ResearchProject> researchProjects= new ArrayList<>();


    public OrgInfo getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(OrgInfo orgInfo) {
        this.orgInfo = orgInfo;
    }

    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    public void setResearchProjects(List<ResearchProject> researchProjects) {
        this.researchProjects = researchProjects;
    }
}
