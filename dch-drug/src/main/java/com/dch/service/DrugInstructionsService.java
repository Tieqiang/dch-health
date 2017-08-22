package com.dch.service;

import com.dch.entity.DrugInstruction;
import com.dch.facade.DrugInstructionsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drug/drug-instruction")
@Produces("application/json")
@Controller
public class DrugInstructionsService {
    @Autowired
    private DrugInstructionsFacade drugInstructionsFacade;

    /**
     * 添加、删除、修改药品说明书
     * @param drugInstruction
     * @return
     */
    @POST
    @Path("merge-drug-instruction")
    @Transactional
    public Response mergeDrugInstruction(DrugInstruction drugInstruction){
        return drugInstructionsFacade.mergeDrugInstruction(drugInstruction);
    }

    /**
     *获取药品说明书
     * @param drugId
     * @return
     */
    @GET
    @Path("get-drug-instructions")
    public List<DrugInstruction> getDrugInstructions(@QueryParam("drugId") String drugId){
        return drugInstructionsFacade.getDrugInstructions(drugId);
    }

    /**
     * 获取单独药品说明书
     * @param instructionId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-drug-instruction")
    public DrugInstruction getDrugInstruction(@QueryParam("instructionId") String instructionId) throws Exception {
        return drugInstructionsFacade.getDrugInstruction(instructionId);
    }

}
