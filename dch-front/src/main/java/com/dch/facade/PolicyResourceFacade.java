package com.dch.facade;

import com.dch.entity.PolicyResources;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PolicyResourceFacade extends BaseFacade {

    public PolicyResources getPolicyResourceById(String policyId) throws Exception {
        String hql=" from PolicyResources where id='"+policyId+"' and status <> '-1'";
        List<PolicyResources> policyResourcesList = createQuery(PolicyResources.class, hql, new ArrayList<>()).getResultList();
        if(policyResourcesList!=null && policyResourcesList.size()>0){
            return policyResourcesList.get(0);
        }else{
            throw new Exception("该政策不存在！");
        }

    }
}
