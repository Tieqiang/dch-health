package com.dch.service;


import com.dch.entity.DrugDiseaseTreatmentGuide;
import com.dch.facade.DrugDiseaseTreatmentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces("application/json")
@Controller
@Path("drug/drug-disease-treatment")
public class DrugDiseaseTreatmentService {


    @Autowired
    private DrugDiseaseTreatmentFacade drugDiseaseTreatmentFacade;

    /**
     * 疾病治疗指南、添加、删除修改
     * @param drugDiseaseTreatmentGuide
     * @return
     */
    @POST
    @Path("merge-treatment")
    @Transactional
    public Response mergeTreatment(DrugDiseaseTreatmentGuide drugDiseaseTreatmentGuide){
        return drugDiseaseTreatmentFacade.mergeTreatment(drugDiseaseTreatmentGuide);

    }

    /**
     * 获取疾病治疗指南
     * @param guideName
     * @return
     */
    @GET
    @Path("get-treatments")
    public List<DrugDiseaseTreatmentGuide> getTreatments(@QueryParam("guideName") String guideName){
        return drugDiseaseTreatmentFacade.getTreatments(guideName);
    }

    /**
     * 获取单个疾病治疗指南
     * @param treatmentId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-treatment")
    public DrugDiseaseTreatmentGuide getTreatment(@QueryParam("treatmentId") String treatmentId) throws Exception {
        return drugDiseaseTreatmentFacade.getTreatment(treatmentId);
    }

}
