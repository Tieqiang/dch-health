package com.dch.facade;

import com.dch.entity.DrugInstruction;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Component
public class DrugInstructionsFacade extends BaseFacade {

    /**
     * 添加、删除、修改药品说明书
     * @param drugInstruction
     * @return
     */
    @Transactional
    public Response mergeDrugInstruction(DrugInstruction drugInstruction) {

        DrugInstruction merge = merge(drugInstruction);

        return Response.status(Response.Status.OK).entity(merge).build();

    }

    /**
     *获取药品说明书
     * @param drugId
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugInstruction> getDrugInstructions(String drugId, int perPage, int currentPage) {
        String hql=" from DrugInstruction where status <> '-1' ";
        if(drugId!=null && !"".equals(drugId)){
            hql+="and drugId ='" + drugId + "' ";
        }
        TypedQuery<DrugInstruction> query = createQuery(DrugInstruction.class, hql, new ArrayList<>());

        Page page =new Page();
        if (perPage > 0) {
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugInstruction> drugInstructionList = query.getResultList();
        page.setCounts((long) drugInstructionList.size());
        page.setData(drugInstructionList);
        return page;
    }

    /**
     * 获取单独药品说明书
     * @param instructionId
     * @return
     * @throws Exception
     */
    public DrugInstruction getDrugInstruction(String instructionId) throws Exception {
        String hql="from DrugInstruction where status <> '-1' and id = '"+instructionId+"'";
        List<DrugInstruction> instructionList = createQuery(DrugInstruction.class, hql, new ArrayList<>()).getResultList();
        if(instructionList!=null && instructionList.size()>0){
            return instructionList.get(0);
        }else{
            throw new Exception("该药品说明书不存在！");
        }
    }
}
