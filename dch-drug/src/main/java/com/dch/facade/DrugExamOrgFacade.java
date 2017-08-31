package com.dch.facade;

import com.dch.entity.DrugDiseaseTreatmentGuide;
import com.dch.entity.DrugExamOrg;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugExamOrgFacade extends BaseFacade {

    /**
     * 添加、删除、修改临床药物试验机构
     * @param drugExamOrg
     * @return
     */
    @Transactional
    public Response mergeDrugExamOrg(DrugExamOrg drugExamOrg) {
        return Response.status(Response.Status.OK).entity(merge(drugExamOrg)).build();
    }

    /**
     * 获取临床药物试验机构
     * @param orgName
     * @param wherehql
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugExamOrg> getDrugExamOrgs(String orgName, String wherehql, int perPage, int currentPage) {
        String hql="from DrugExamOrg where status <> '-1' ";
        if(null!=orgName&&!"".equals(orgName)){
            hql += "and medicalOrgName like '%"+orgName+"%'";
        }
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += " and "+wherehql;
        }
        TypedQuery<DrugExamOrg> query = createQuery(DrugExamOrg.class, hql, new ArrayList<>());
        Page page=new Page();
        if (perPage > 0) {
            query.setFirstResult(currentPage * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugExamOrg> examOrgList = query.getResultList();
        page.setCounts((long) examOrgList.size());
        page.setData(examOrgList);
        return page;
    }

    /**
     * 获取单一的临床药物试验机构
     * @param examOrgId
     * @return
     * @throws Exception
     */
    public DrugExamOrg getDrugExamOrg(String examOrgId) throws Exception {
        String hql="from DrugExamOrg where status <> '-1' and id = '"+examOrgId+"'";
        List<DrugExamOrg> drugExamOrgList = createQuery(DrugExamOrg.class, hql, new ArrayList<>()).getResultList();
        if(drugExamOrgList!=null && !drugExamOrgList.isEmpty()){
            return drugExamOrgList.get(0);
        }else{
            throw new Exception("临床药试验机构不存在");
        }
    }
}
