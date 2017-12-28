package com.dch.service;


import com.dch.entity.DrugFirm;
import com.dch.entity.PolicyResources;
import com.dch.facade.PolicyResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("front/policy-resources")
@Produces("application/json")
@Controller
public class PolicyResourceService {
    @Autowired
    PolicyResourceFacade policyResourceFacade;

    /**
     * 根据id获取详细政策资源
     * @param policyId
     * @return
     */
    @GET
    @Path("get-policy-resource-by-id")
    public PolicyResources getPolicyResourceById(@QueryParam("policyId") String policyId) throws Exception {
        return policyResourceFacade.getPolicyResourceById(policyId);
    }

}
