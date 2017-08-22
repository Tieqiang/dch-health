package com.dch.facade;

import com.dch.entity.DrugInstruction;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

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
     * @return
     */
    public List<DrugInstruction> getDrugInstructions(String drugId) {
        String hql=" from DrugInstruction where status <> '-1' ";
        if(drugId!=null && !"".equals(drugId)){
            hql+="and drugId ='" + drugId + "' ";
        }
        return createQuery(DrugInstruction.class, hql, new ArrayList<>()).getResultList();
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
