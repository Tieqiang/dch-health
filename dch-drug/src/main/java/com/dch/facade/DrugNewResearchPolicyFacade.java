package com.dch.facade;

import com.dch.entity.DrugInstruction;
import com.dch.entity.DrugNewResearchPolicy;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

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
     * @return
     */
    public List<DrugNewResearchPolicy> getNewResearchPolicys(String policyTypeFlag) {

        String hql=" from DrugNewResearchPolicy where status <> '-1' and policyTypeFlag= '" +policyTypeFlag+ "'";

        return createQuery(DrugNewResearchPolicy.class, hql, new ArrayList<>()).getResultList();

    }

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
