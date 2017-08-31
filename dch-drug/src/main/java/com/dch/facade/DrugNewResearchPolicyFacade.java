package com.dch.facade;

import com.dch.entity.DrugInstruction;
import com.dch.entity.DrugNewResearchPolicy;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugNewResearchPolicyFacade extends BaseFacade {

    /**
     * 添加、删除、修改新药政策
     * @param drugNewResearchPolicy
     * @return
     */
    @Transactional
    public Response mergeNewResearchPolicy(DrugNewResearchPolicy drugNewResearchPolicy) {

        DrugNewResearchPolicy merge = merge(drugNewResearchPolicy);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取研发政策
     * @param policyTypeFlag
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugNewResearchPolicy> getNewResearchPolicys(String policyTypeFlag, int perPage, int currentPage) {

        String hql=" from DrugNewResearchPolicy where status <> '-1' and policyTypeFlag= '" +policyTypeFlag+ "'";

        TypedQuery<DrugNewResearchPolicy> query = createQuery(DrugNewResearchPolicy.class, hql, new ArrayList<>());
        Page page=new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugNewResearchPolicy> drugNewResearchPolicyList = query.getResultList();
        page.setCounts((long) drugNewResearchPolicyList.size());
        page.setData(drugNewResearchPolicyList);
        return page;

    }

    /**
     * 获取单个研发政策
     * @param policyId
     * @return
     * @throws Exception
     */
    public DrugNewResearchPolicy getNewResearchPolicy(String policyId) throws Exception {
        String hql=" from DrugNewResearchPolicy where status <> '-1' and id= '" +policyId+ "'";
        List<DrugNewResearchPolicy> newResearchPolicyList = createQuery(DrugNewResearchPolicy.class, hql, new ArrayList<>()).getResultList();
        if(newResearchPolicyList!=null && newResearchPolicyList.size()>0){
            return newResearchPolicyList.get(0);
        }else{
            throw new Exception("该药研发政策还未颁布！");
        }
    }
}
