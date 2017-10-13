package com.dch.service;

import com.dch.entity.MrSubject;
import com.dch.facade.MrSubjectFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;

@Produces("application/json")
@Path("mr/mr-subject")
@Controller
public class MrSubjectService {

    @Autowired
    private MrSubjectFacade mrSubjectFacade;

    /**
     *添加、删除、修改病例
     * @param mrSubject
     * @return
     */
    @Path("merge-mr-subject")
    @POST
    @Transactional
    public Response mergeMrSubject(MrSubject mrSubject){
        return Response.status(OK).entity(mrSubjectFacade.mergeMrSubject(mrSubject)).build();
    }

    /**
     *查询学科信息
     * @return
     */
    @Path("get-mr-subjects")
    @GET
    public List<MrSubject> getMrSubjects(){
        return mrSubjectFacade.getMrSubjects();
    }

    /**
     *获取具体的学科信息
     * @param subjectId
     * @return
     * @throws Exception
     */
    @Path("get-mr-subject")
    @GET
    public MrSubject getMrSubject(@QueryParam("subjectId") String subjectId,
                                  @QueryParam("subjectCode") String subjectCode) throws Exception {
        return mrSubjectFacade.getMrSubject(subjectId,subjectCode);
    }

    /**
     *获取学科的子学科信息
     * @param subjectId
     * @return
     */
    @Path("get-sub-mr-subjects")
    @GET
    public List<MrSubject> getSubMrSubjects(@QueryParam("subjectId") String subjectId){
        return mrSubjectFacade.getSubMrSubjects(subjectId);
    }
}
