package com.dch.service;

import com.dch.entity.MrFile;
import com.dch.facade.MrFileFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

@Controller
@Path("mr/mr-file")
@Produces("application/json")
public class MrFileService {
    @Autowired
    private MrFileFacade mrFileFacade;

    /**
     * 添加、删除、修改病例内容
     * @param mrFile
     * @return
     */
    @Path("merge-mr-file")
    @POST
    @Transactional
    public Response mergeMrFileContent(MrFile mrFile){
        return Response.status(OK).entity(mrFileFacade.mergeMrFileContent(mrFile)).build();
    }

    /**
     * 查询文章内容
     * @param fileId
     * @return
     * @throws Exception
     */
    @Path("get-mr-file")
    @GET
    public MrFile getMrFile(@QueryParam("fileId") String fileId) throws Exception {
        return mrFileFacade.getMrFile(fileId);
    }

    /**
     * 获取知识库列表
     * @param subjectCode
     * @param title
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-mr-files")
    public Page<MrFile> getMrFiles(@QueryParam("subjectCode") String subjectCode,
                                   @QueryParam("title") String title,
                                   @QueryParam("perPage") int perPage,
                                   @QueryParam("currentPage") int currentPage){
        return mrFileFacade.getMrFiles(subjectCode,title,perPage,currentPage);
    }
}
