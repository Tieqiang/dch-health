package com.dch.service;

import com.dch.entity.TemplateResultSupport;
import com.dch.facade.TemplateSupportFacade;
import com.dch.util.LogHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/19.
 */
@Produces("application/json")
@Path("template/support")
@Controller
public class TemplateSupportService {

    @Autowired
    private TemplateSupportFacade templateSupportFacade;

    /**
     * 保存修改，删除表单佐证材料
     * @param templateResultSupport
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response mergeTemplateSupport(TemplateResultSupport templateResultSupport){
        TemplateResultSupport merge = templateSupportFacade.merge(templateResultSupport);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据填报表单获取上传的表单
     * @param masterId
     * @return
     */
    @GET
    @Path("find-one")
    public TemplateResultSupport getTemplateResultSupport(@QueryParam("masterId") String masterId) throws Exception{
        return templateSupportFacade.getTemplateResultSupport(masterId);
    }
    @GET
    @Path("down-load")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downLoadFile(@QueryParam("masterId") String masterId) throws IOException {
         Map<String,String> mapFile = templateSupportFacade.getFileStorePath(masterId);
         String fileTitle = mapFile.get("file_name");
         final String fileStorePath = mapFile.get("store_path");
        StreamingOutput streamingOutput = new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                FileInputStream fileInputStream = new FileInputStream(fileStorePath);
                int length =  0 ;
                byte[] bytes = new byte[1024];
                while(-1!=(length=fileInputStream.read(bytes))){
                    outputStream.write(bytes);
                }
                outputStream.flush();
                outputStream.close();
                fileInputStream.close();
            }
        };
        String fileName = URLEncoder.encode(fileTitle, "UTF-8");
        LogHome.getLog().info(fileName);
        return Response.status(Response.Status.OK).entity(streamingOutput).header("Content-disposition","attachment;filename="+ fileName)
                .header("Cache-Control","no-cache").build();
    }

    /**
     * 根据表单数据id判断是否上传了佐证材料
     * @param masterId
     * @return
     */
    @GET
    @Path("judge-if-upload")
    public Response ifUpload(@QueryParam("masterId")String masterId){
        Boolean isUpload = false;
        Map<String,String> mapFile = templateSupportFacade.getFileStorePath(masterId);
        if(mapFile!=null && !mapFile.isEmpty()){
            isUpload = true;
        }
        return Response.status(Response.Status.OK).entity(isUpload).build();
    }
}
